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

    private final BufferedImage[] images;
    private final int width;
    private final int yOffset, height;

    private final static double min[] = {256, 128, 64, 32, 16, 8, 4, 2, 1, 0};
    private final static double max[] = {Double.MAX_VALUE, 256, 128, 64, 32, 16, 8, 4, 2, 1};

    public Sprite(int count, int width, int height, int yOffset) throws IOException {
        images = new BufferedImage[count];
        this.width = width;
        this.height = height;
        this.yOffset = yOffset;
    }

    void addFile(int index, String fileName) throws IOException {
        BufferedImage bi = ImageIO.read(Sprite.class.getResourceAsStream(fileName));
        GraphicsConfiguration gfx_config = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();
        images[index] = gfx_config.createCompatibleImage(width, height, bi.getColorModel().getTransparency());
        Graphics g = images[index].getGraphics();
        g.drawImage(bi, 0, 0, null);
        g.dispose();
    }

    /**
     * @param index
     * @return the image
     */
    public BufferedImage getImage(int index) {
        return images[index];
    }

    public BufferedImage getSubImage(int x, int y, int height) {
        return getSubImage(x, y, 1, height);
    }

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

    private final static Map<String, Sprite> sprites = new LinkedHashMap<>();

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
