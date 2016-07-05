package org.freeware.monakhov.game3d.map;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * Класс для хранения изображений
 *
 * @author Vasily Monakhov
 */
public class Image {

    /**
     * Буферизированное изображение
     */
    private final BufferedImage image;

    /**
     * Содайт изображение
     * @param fileName имя файла
     * @throws IOException 
     */
    Image(String fileName) throws IOException {
        BufferedImage bi = ImageIO.read(Image.class.getResourceAsStream(fileName));
        GraphicsConfiguration gfx_config = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();
        image = gfx_config.createCompatibleImage(bi.getWidth(), bi.getHeight(), bi.getColorModel().getTransparency());
        image.setAccelerationPriority(1);        
        Graphics2D g = (Graphics2D) image.createGraphics();
        g.drawImage(bi, 0, 0, null);
        g.dispose();
    }

    /**
     * Возвращает буферизированное изображение
     * @return the image
     */
    public BufferedImage getImage() {
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

    /**
     * Возвращает изображение по идентификатору
     * @param id идентификатори изображения
     * @return изображение
     */
    public static Image get(String id) {
        Image img = images.get(id);
        if (img == null) {
            throw new IllegalArgumentException("Image " + id + " not exists");
        }
        return img;
    }

}
