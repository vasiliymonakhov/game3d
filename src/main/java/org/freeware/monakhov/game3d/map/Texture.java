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

    private BufferedImage image;

    private final String fileName;
    private final static int SIZE = 256;
    private final int width;

    Texture(String fileName, int width) {
        this.fileName = fileName;
        this.width = width;
    }

    /**
     * @return the image
     */
    public BufferedImage getImage() {
        if (image == null) {
            try {
                BufferedImage bi = ImageIO.read(Texture.class.getResourceAsStream(fileName));
                GraphicsConfiguration gfx_config = GraphicsEnvironment.
                        getLocalGraphicsEnvironment().getDefaultScreenDevice().
                        getDefaultConfiguration();
                image = gfx_config.createCompatibleImage(width, SIZE, Transparency.OPAQUE);
                Graphics2D g = (Graphics2D) image.getGraphics();
                g.drawImage(bi, 0, 0, width, SIZE, null);            
                g.dispose();
            } catch (IOException ex) {
                Logger.getLogger(Texture.class.getName()).log(Level.SEVERE, "Can not load image", ex);
            }
        }
        return image;
    }

    public BufferedImage getSubImage(int x) {
        return getImage().getSubimage(x % width, 0, 1, SIZE);
    }

    /**
     * Карта для хранения текстур
     */
    private final static Map<String, Texture> textures = new LinkedHashMap<>();

    /**
     * Добавляет новую текстуру
     *
     * @param id идентификатор текстуры
     * @param fileName имя файла с текстурой
     * @param width
     * @throws IOException
     */
    public static void add(String id, String fileName, int width) throws IOException {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Texture id is null or empty");
        }
        textures.put(id, new Texture(fileName, width));
    }

    public static Texture get(String id) {
        Texture tex = textures.get(id);
        if (tex == null) {
            throw new IllegalArgumentException("Texture " + id + " not exists");
        }
        return tex;
    }

}
