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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Класс для хранения изображений
 *
 * @author Vasily Monakhov
 */
public class Image {

    private final String fileName;
    private BufferedImage image;

    Image(String fileName) throws IOException {
        this.fileName = fileName;
    }

    /**
     * @return the image
     * @throws java.io.IOException
     */
    public BufferedImage getImage() throws IOException {
        if (image == null) {
            BufferedImage bi = ImageIO.read(Image.class.getResourceAsStream(fileName));
            GraphicsConfiguration gfx_config = GraphicsEnvironment.
                    getLocalGraphicsEnvironment().getDefaultScreenDevice().
                    getDefaultConfiguration();
            image = gfx_config.createCompatibleImage(bi.getWidth(), bi.getHeight(),
                    bi.isAlphaPremultiplied() ? Transparency.TRANSLUCENT : Transparency.OPAQUE);
            Graphics2D g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.drawImage(bi, 0, 0, null);
            g.dispose();
        }
        return image;
    }

    /**
     * Карта для хранения изображений
     */
    private final static Map<String, Image> images = new LinkedHashMap<>();

    /**
     * Добавляет новое изображение
     *
     * @param id идентификатор изображения
     * @param fileName имя файла с изображением
     * @throws IOException
     */
    public static void add(String id, String fileName) throws IOException, IllegalArgumentException {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Image id is null or empty");
        }
        images.put(id, new Image(fileName));
    }

    public static Image get(String id) {
        Image img = images.get(id);
        if (img == null) {
            throw new IllegalArgumentException("Image " + id + " not exists");
        }
        return img;
    }

}
