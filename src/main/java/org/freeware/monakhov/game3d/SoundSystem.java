package org.freeware.monakhov.game3d;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import org.freeware.monakhov.game3d.resources.AudioFile;

/**
 *
 * @author Vasily Monakhov
 */
public class SoundSystem {

    private final static BlockingQueue<String> bq = new LinkedBlockingQueue<>();

    public static void play(final String id) {
        bq.add(id);
    }

    public static void init() {
        Thread t = new Thread(new Player());
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    private static final int MAX_SOUNDS = 16;

    private static class Player implements Runnable {

        private final Executor EXECUTOR = Executors.newFixedThreadPool(MAX_SOUNDS);
            @Override
            public void run() {
                while (true) {
                    try {
                        String id = bq.take();
                        if (soundsCount.get() > MAX_SOUNDS) {
                            continue;
                        }
                        AudioFile af = AudioFile.get(id);
                        EXECUTOR.execute(new Sound(af, this));
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SoundSystem.class.getName()).log(Level.SEVERE, "Can't play audiofile", ex);
                    }
                }
            }

            private final AtomicInteger soundsCount = new AtomicInteger();

            synchronized void  incSoundsCount(Sound sound) {
                soundsCount.incrementAndGet();
            }

            synchronized void  decSoundsCount(Sound sound) {
                soundsCount.decrementAndGet();
            }

    }

    private final static AudioFormat auf = new AudioFormat(44100, 16, 1, true, false);

    private static class Sound implements Runnable, LineListener {

        Sound(AudioFile af, Player player) {
            this.af = af;
            this.player = player;
        }

        private final AudioFile af;
        private final Player player;

        @Override
        public void run() {
            try (Clip clip = AudioSystem.getClip()) {
                player.incSoundsCount(this);
                clip.addLineListener(this);
                clip.open(auf, af.getBytes(), 0, af.getBytes().length);
                clip.setMicrosecondPosition(0);
                clip.start();
                waitUntilDone();
                clip.stop();
            } catch (LineUnavailableException | InterruptedException ex) {
                Logger.getLogger(SoundSystem.class.getName()).log(Level.SEVERE, "Can't play audiofile", ex);
            } finally {
                player.decSoundsCount(this);
            }
        }

        private boolean done = false;

        @Override
        public synchronized void update(LineEvent event) {
            Type eventType = event.getType();
            if (eventType == Type.STOP || eventType == Type.CLOSE) {
                done = true;
                notifyAll();
            }
        }

        public synchronized void waitUntilDone() throws InterruptedException {
            while (!done) {
                wait();
            }
        }
    }

}
