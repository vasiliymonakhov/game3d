package org.freeware.monakhov.game3d;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import org.freeware.monakhov.game3d.resources.AudioFile;

/**
 *
 * @author Vasily Monakhov
 */
public class SoundSystem {

    private final static PriorityBlockingQueue<AudioFile> bq = new PriorityBlockingQueue<>(64, new Comparator<AudioFile>() {
        @Override
        public int compare(AudioFile o1, AudioFile o2) {
            return o2.getPriority() - o1.getPriority();
        }
    });

    public static void play(final String id) {
        bq.add(AudioFile.get(id));
    }

    public static void init() {
        Thread t = new Thread(new Player());
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    private static final int MAX_SOUNDS = 16;

    static class Player implements Runnable {

        private final Set<AudioFile> nowPlaying = new HashSet<>();

        private final Executor EXECUTOR = Executors.newFixedThreadPool(MAX_SOUNDS);

        ReentrantLock lock = new ReentrantLock();

        int soundsCount;

        @Override
        public void run() {
            SoundRunnable nsr;
            cycle: while (true) {
                try {
                    AudioFile af = bq.take();
                    try {
                        lock.lock();
                        if (soundsCount > MAX_SOUNDS) {
                            int priority = af.getPriority();
                            for (AudioFile npaf : nowPlaying) {
                                if (npaf == af || npaf.getPriority() > priority) {
                                    continue cycle;
                                }
                            }
                        }
                        nsr = new SoundRunnable(af, this);
                        nowPlaying.add(af);
                        soundsCount++;
                    } finally {
                        lock.unlock();
                    }
                    EXECUTOR.execute(nsr);
                } catch (InterruptedException | LineUnavailableException ex) {
                    Logger.getLogger(SoundSystem.class.getName()).log(Level.SEVERE, "Can't play audiofile", ex);
                }
            }
        }

        synchronized void onEndPlaying(SoundRunnable sound) {
            try {
                lock.lock();
                soundsCount--;
                nowPlaying.remove(sound.getAf());
            } finally {
                lock.unlock();
            }
        }

    }
}
