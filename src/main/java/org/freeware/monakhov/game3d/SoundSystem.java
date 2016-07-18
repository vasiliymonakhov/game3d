package org.freeware.monakhov.game3d;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
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

    private final static Executor EXECUTOR = Executors.newCachedThreadPool();

    public static void start() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String id = bq.take();
                        AudioFile af = AudioFile.get(id);
                        EXECUTOR.execute(new Player(af));
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SoundSystem.class.getName()).log(Level.SEVERE, "Can't play audiofile", ex);
                    }
                }
            }
        });
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    static class Player implements Runnable, LineListener {

        Player(AudioFile af) {
            this.af = af;
        }

        private final AudioFile af;

        @Override
        public void run() {
            try (Clip clip = AudioSystem.getClip();
                    ByteArrayInputStream bais = new ByteArrayInputStream(af.getBytes())) {
                clip.addLineListener(this);
                AudioInputStream stream = AudioSystem.getAudioInputStream(bais);
                clip.open(stream);
                clip.setMicrosecondPosition(0);
                clip.start();
                waitUntilDone();
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException | InterruptedException ex) {
                Logger.getLogger(SoundSystem.class.getName()).log(Level.SEVERE, "Can't play audiofile", ex);
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
