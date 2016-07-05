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
 * Класс для хранения текстур
 *
 * @author Vasily Monakhov
 */
public class Texture {

    /**
     * Изображения текстур. Для уменьшения ряби при прорисовке текстур используются несколько заранее 
     * подготовленных изображений с разными размерами.
     */
    private final BufferedImage[][] images;

    /**
     * Высота текстуры
     */
    public final static int SIZE = 256;
    /**
     * Таблицы для определения, какое изображение нужно использовать для вывода на экран в зависимости от высоты
     */
    private final static double min[] = {256, 128, 64, 32, 16};
    private final static double max[] = {Double.MAX_VALUE, 256, 128, 64, 32};

    /**
     * Ширина текстуры
     */
    private final int width;

    /**
     * Создайт текстуру
     * @param width ширина
     * @param count количество изображений
     */
    Texture(int width, int count) {
        images = new BufferedImage[count][];
        this.width = width;
    }

    /**
     * Добавляет в текстуру новое изображение
     * @param index индекс изображения 
     * @param fileName имя файла
     * @throws IOException 
     */
    void addFile(int index, String fileName) throws IOException {
        BufferedImage bi = ImageIO.read(Texture.class.getResourceAsStream(fileName));
        GraphicsConfiguration gfx_config = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();
        int nw = width >> index;
        int ns = SIZE >> index;
        BufferedImage[] newImages = new BufferedImage[nw];
        for (int i = 0; i < nw; i++) {
            BufferedImage ni = gfx_config.createCompatibleImage(1, ns, bi.getColorModel().getTransparency());
            ni.setAccelerationPriority(1);
            Graphics2D g = (Graphics2D) ni.createGraphics();
            g.drawImage(bi.getSubimage(i, 0, 1, bi.getHeight()), null, null);
            g.dispose();
            newImages[i] = ni;
        }
        images[index] = newImages;
    }

    /**
     * Вовзращает участок изображения шириной 1 пиксель
     * @param x смещение от нечала
     * @param height выста текстуры на буфере экрана
     * @return участок изображения
     */
    public BufferedImage getSubImage(int x, double height) {
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
        return images[index][(x % width) >> index];
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

    /**
     * Возвращает текстуру по идентификатору
     * @param id идентификатор текстуры
     * @return текстура
     */
    public static Texture get(String id) {
        Texture tex = textures.get(id);
        if (tex == null) {
            throw new IllegalArgumentException("Texture " + id + " not exists");
        }
        return tex;
    }

}
