package org.freeware.monakhov.game3d.map;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Класс для хранения текстур
 *
 * @author Vasily Monakhov
 */
public class Texture {

    private final BufferedImage[] images;
    private final String[] fileNames;
    
    public final static int SIZE = 256;
    private final static double min[] = {256, 128, 64, 32, 16, 8, 4, 2, 1, 0};
    private final static double max[] = {Double.MAX_VALUE, 256, 128, 64, 32, 16, 8, 4, 2, 1};
    
    private final int width;

    Texture(int width, int count) {
        fileNames = new String[count];
        images = new BufferedImage[count];
        this.width = width;
    }

    void addFile(int index, String fileName) {
        fileNames[index] = fileName;
    }
    
    /**
     * @param index
     * @return the image
     */
    public BufferedImage getImage(int index) {
        if (images[index] == null) {
            try {
                BufferedImage bi = ImageIO.read(Texture.class.getResourceAsStream(fileNames[index]));
                GraphicsConfiguration gfx_config = GraphicsEnvironment.
                        getLocalGraphicsEnvironment().getDefaultScreenDevice().
                        getDefaultConfiguration();
                images[index] = gfx_config.createCompatibleImage(width >> index, SIZE >> index, Transparency.OPAQUE);
                Graphics2D g = (Graphics2D) images[index].getGraphics();
                g.drawImage(bi, 0, 0, null);            
                g.dispose();
            } catch (IOException ex) {
                Logger.getLogger(Texture.class.getName()).log(Level.SEVERE, "Can not load image", ex);
            }
        }
        return images[index];
    }

    public BufferedImage getSubImage(int x, double height) {
        int index = 0;
        for (int i = 0; i < min.length; i++) {
            if (height < max[i] && height > min[i]) {
                index = i;
                break;
            }
        }
        if (index >= images.length) index = images.length - 1;
        return getImage(index).getSubimage((x % width) >> index, 0, 1, SIZE >> index);
    }

    /**
     * Карта для хранения текстур
     */
    private final static Map<String, Texture> textures = new LinkedHashMap<>();

    /**
     * Добавляет новую текстуру
     *
     * @param id идентификатор текстуры
     * @param count
     * @param width
     * @return 
     * @throws IOException
     */
    public static Texture add(String id, int width, int count) throws IOException {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Texture id is null or empty");
        }
        Texture tex = new Texture(width, count);
        textures.put(id, tex);
        return tex;
    }

    public static Texture get(String id) {
        Texture tex = textures.get(id);
        if (tex == null) {
            throw new IllegalArgumentException("Texture " + id + " not exists");
        }
        return tex;
    }

}
