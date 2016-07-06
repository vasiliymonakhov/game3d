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
    public final static int HEIGHT = 256;
    /**
     * Таблицы для определения, какое изображение нужно использовать для вывода на экран в зависимости от высоты
     */
    private final static double min[] = {256, 128, 64, 32, 0};
    private final static double max[] = {Double.MAX_VALUE, 256, 128, 64, 32};

    /**
     * Ширина текстуры
     */
    private final int width;

    /**
     * Создаёт текстуру
     * @param fileName имя файла
     * @throws IOException
     */
    Texture(String fileName) throws IOException {
        images = new BufferedImage[min.length][];
        BufferedImage bi = ImageIO.read(Texture.class.getResourceAsStream(fileName));
        this.width = bi.getWidth();
        addMipMappedImage(0, bi.getWidth(), bi.getHeight(), bi);
        for (int i = 1; i < min.length; i++) {
            int nw = width >> i;
            if (nw < 1) nw = 1;
            int nh = HEIGHT >> i;
            if (nh < 1) nh = 1;
            int bs = 1 << i;
            BufferedImage mmi = new BufferedImage(nw, nh, bi.getType());
            for (int x = 0; x < nw; x ++) {
                for (int y = 0; y < nh; y ++) {
                    mmi.setRGB(x, y, averagePixels(bi, x << i, y << i, bs));
                }
            }
            addMipMappedImage(i, nw, nh, mmi);
        }
    }

    /**
     * Вычисляет среднее значение пикселя
     * @param bi изображение
     * @param x горизонтальная координата
     * @param y вертикальная координата
     * @param blockSize размер блока в пикселях
     * @return усреднённое значение пикселя
     */
    static int averagePixels(BufferedImage bi, int x, int y, int blockSize) {
        double a = 0, r = 0, g = 0, b = 0;
        for (int ox = 0; ox < blockSize; ox++) {
            for (int oy = 0; oy < blockSize; oy++) {
                int argb = bi.getRGB(x + ox, y + oy);
                a += 0xFF & (argb >> 24);
                r += 0xFF & (argb >> 16);
                g += 0xFF & (argb >> 8);
                b += 0xFF & argb;
            }
        }
        double d = blockSize * blockSize;
        int na = (int)Math.round(a / d);
        if (na > 255) na = 255;
        int nr = (int)Math.round(r / d);
        if (nr > 255) nr = 255;
        int ng = (int)Math.round(g / d);
        if (ng > 255) ng = 255;
        int nb = (int)Math.round(b / d);
        if (nb > 255) nb = 255;
        return na << 24 | nr << 16 | ng << 8 | nb;
    }

    /**
     * Добавляет уменьшенное изображение
     * @param index индекс
     * @param nw новая ширина
     * @param ns новая высота
     * @param bi откуда взять данные для изображений
     */
    private void addMipMappedImage(int index, int nw, int ns, BufferedImage bi) {
        GraphicsConfiguration gfx_config = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();
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
        int index = min.length - 1;
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
     * @param fileName имя файла с текстурой
     * @return созданная текстура
     * @throws IOException
     */
    public static Texture add(String id, String fileName) throws IOException {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Texture id is null or empty");
        }
        Texture tex = new Texture(fileName);
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
