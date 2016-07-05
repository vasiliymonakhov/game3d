package org.freeware.monakhov.game3d.map;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * Спрайт
 *
 * @author Vasily Monakhov
 */
public class Sprite {

    /**
     * Буферризированные изображения 
     */
    private final BufferedImage[] images;
    /**
     * Ширина спрайта
     */
    private final int width;
    /**
     * Смещение спрайта по вертикали и его высота
     */
    private final int yOffset, height;

    /**
     * Таблицы для определения, какое изображение нужно использовать для вывода на экран в зависимости от высоты
     */
    private final static double min[] = {256, 128, 64, 32, 16};
    private final static double max[] = {Double.MAX_VALUE, 256, 128, 64, 32};

    /**
     * Создаёт новый спрайт
     * @param count количество изображений
     * @param width ширина
     * @param height высота
     * @param yOffset смещение спрайта по вертикали
     */
    public Sprite(int count, int width, int height, int yOffset) {
        images = new BufferedImage[count];
        this.width = width;
        this.height = height;
        this.yOffset = yOffset;
    }

    /**
     * Добавляет изображение в спрайт
     * @param index индекс изображения
     * @param fileName имя файла
     * @throws IOException 
     */
    void addFile(int index, String fileName) throws IOException {
        BufferedImage bi = ImageIO.read(Sprite.class.getResourceAsStream(fileName));
        GraphicsConfiguration gfx_config = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();
        images[index] = gfx_config.createCompatibleImage(width, height, bi.getColorModel().getTransparency());
        images[index].setAccelerationPriority(1);        
        Graphics g = images[index].createGraphics();
        g.drawImage(bi, 0, 0, null);
        g.dispose();
    }

    /**
     * Возвращает буферизированное изображение по индексу
     * @param index индекс
     * @return the image
     */
    public BufferedImage getImage(int index) {
        return images[index];
    }

    /**
     * Возвращает участок изображения
     * @param x горизонталная координата
     * @param y вертикальная координата
     * @param vwidth ширина
     * @param height высота
     * @return участок изображения
     */
    public BufferedImage getSubImage(int x, int y, int vwidth, int height) {
        int index = 0;
        for (int i = 0; i < min.length; i++) {
            if (height < max[i] && height >= min[i]) {
                index = i;
                break;
            }
        }
        if (index >= images.length) {
            index = images.length - 1;
        }
        BufferedImage bi = getImage(index);
        int svwidth = vwidth >> index;
        if (svwidth < 1) {
            svwidth = 1;
        }
        return bi.getSubimage((x % width) >> index, y >> index, svwidth, bi.getHeight() >> index);
    }

    /**
     * Карта спрайтов
     */
    private final static Map<String, Sprite> sprites = new LinkedHashMap<>();

    /**
     * Вовзращает спрайт по его идентификатору
     * @param id идентификатор спрайта
     * @return спрайт
     */
    public static Sprite get(String id) {
        Sprite spr = sprites.get(id);
        if (spr == null) {
            throw new IllegalArgumentException("Sprite " + id + " not exists");
        }
        return spr;
    }

    /**
     * Добавляет новый спрайт
     *
     * @param id идентификатор спрайта
     * @param count
     * @param width
     * @param height
     * @param yOffset смещение спрайта от верха по вертикали
     * @return
     * @throws IOException
     */
    public static Sprite add(String id, int count, int width, int height, int yOffset) throws IOException {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Sprite id is null or empty");
        }
        Sprite spr = new Sprite(count, width, height, yOffset);
        sprites.put(id, spr);
        return spr;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the yOffset
     */
    public int getYOffset() {
        return yOffset;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

}
