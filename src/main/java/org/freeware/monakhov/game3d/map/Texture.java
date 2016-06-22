package org.freeware.monakhov.game3d.map;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * Класс для хранения текстур
 *
 * @author Vasily Monakhov
 */
public class Texture {

    private final BufferedImage image;

    private final static int SIZE = 256;
    private final int width;

    Texture(String fileName) throws IOException {
        BufferedImage bi = ImageIO.read(Texture.class.getResourceAsStream(fileName));
        GraphicsConfiguration gfx_config = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();
        width = bi.getWidth();
        image = gfx_config.createCompatibleImage(width, SIZE, Transparency.OPAQUE);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(bi, 0, 0, width, SIZE, null);
        g.dispose();
    }

    /**
     * @return the image
     */
    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage getSubImage(int x) {
        return image.getSubimage(x % width, 0, 1, SIZE);
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
     * @throws IOException
     */
    public static void add(String id, String fileName) throws IOException {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Texture id is null or empty");
        }
        textures.put(id, new Texture(fileName));
    }

    public static Texture get(String id) {
        Texture tex = textures.get(id);
        if (tex == null) {
            throw new IllegalArgumentException("Texture " + id + " not exists");
        }
        return tex;
    }

}
