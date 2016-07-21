package org.freeware.monakhov.game3d;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import org.freeware.monakhov.game3d.resources.AudioFile;

/**
 *
 * @author Vasily Monakhov
 */
class SoundRunnable implements Runnable, LineListener {

    private final static AudioFormat auf = new AudioFormat(44100, 16, 1, true, false);

    SoundRunnable(AudioFile af, SoundSystem.Player player) throws LineUnavailableException {
        this.af = af;
        this.player = player;
    }

    private final AudioFile af;
    private final SoundSystem.Player player;

    @Override
    public void run() {
        try (Clip clip = AudioSystem.getClip()) {
            clip.addLineListener(this);
            clip.open(auf, getAf().getBytes(), 0, getAf().getBytes().length);
            clip.setMicrosecondPosition(0);
            clip.start();
            waitUntilDone();
        } catch (LineUnavailableException | InterruptedException ex) {
            Logger.getLogger(SoundSystem.class.getName()).log(Level.SEVERE, "Can't play audiofile", ex);
        } finally {
            player.onEndPlaying(this);
        }
    }

    private boolean done = false;

    @Override
    public synchronized void update(LineEvent event) {
        LineEvent.Type eventType = event.getType();
        if (eventType == LineEvent.Type.STOP) {
            done = true;
            notify();
        }
    }

    public synchronized void waitUntilDone() throws InterruptedException {
        while (!done) {
            wait();
        }
    }

    /**
     * @return the af
     */
    public AudioFile getAf() {
        return af;
    }

}
