package org.freeware.monakhov.game3d.map;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * Спрайт
 * @author Vasily Monakhov 
 */
public class Sprite {
    
    private final BufferedImage image;
    private final int width;
    private final int yOffset, height;
    
    public Sprite (String fileName, int yOffset) throws IOException {
        BufferedImage bi = ImageIO.read(Texture.class.getResourceAsStream(fileName));
        GraphicsConfiguration gfx_config = GraphicsEnvironment.
		getLocalGraphicsEnvironment().getDefaultScreenDevice().
		getDefaultConfiguration();
        width = bi.getWidth();
        height = bi.getHeight();        
        this.yOffset = yOffset;
        image = gfx_config.createCompatibleImage(width, height, Transparency.TRANSLUCENT);        
        Graphics g = image.getGraphics();
        g.drawImage(bi, 0, 0, null);
        g.dispose();
    }
    
    /**
     * @return the image
     */
    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage getSubImage(int x, int y) {
        return image.getSubimage(x, y, 1, height);
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
     * @param fileName имя файла со спрайтом
     * @param yOffset смещение спрайта от верха по вертикали
     * @throws IOException
     */
    public static void add(String id, String fileName, int yOffset) throws IOException {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Sprite id is null or empty");
        }
        sprites.put(id, new Sprite(fileName, yOffset));
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
