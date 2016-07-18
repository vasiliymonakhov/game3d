package org.freeware.monakhov.game3d.resources;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vasily Monakhov
 */
public class AudioFile {

    private byte[] bytes;

    AudioFile(String fileName) {
        try {
            InputStream is = AudioFile.class.getResourceAsStream(fileName);
            BufferedInputStream bis = new BufferedInputStream(is);
            bytes = new byte[bis.available()];
            bis.read(bytes);
        } catch (IOException ex) {
            Logger.getLogger(AudioFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public byte[] getBytes() {
        return bytes;
    }

    private final static Map<String, AudioFile> audios = new LinkedHashMap<>();

    public static void add(String id, String fileName) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Audiofile id is null or empty");
        }
        audios.put(id, new AudioFile(fileName));
    }

    public static AudioFile get(String id) {
        AudioFile af = audios.get(id);
        if (af == null) {
            throw new IllegalArgumentException("Audiofile " + id + " not exists");
        }
        return af;
    }

}
