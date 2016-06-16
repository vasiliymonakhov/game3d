package org.freeware.monakhov.game3d.map;

import java.awt.Graphics;
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
        if (id== null) {
            throw new IllegalArgumentException("Sprite " + id + " not exists"); 
        }
        return spr;
    }
    
    static {
        try {
            sprites.put("tree", new Sprite("/org/freeware/monakhov/game3d/map/tree.png", 0));
            sprites.put("green_barrel", new Sprite("/org/freeware/monakhov/game3d/map/green_barrel.png", 124));
            sprites.put("milton", new Sprite("/org/freeware/monakhov/game3d/map/milton.png", 46));
            sprites.put("lamp", new Sprite("/org/freeware/monakhov/game3d/map/lamp.png", 0));
            sprites.put("key", new Sprite("/org/freeware/monakhov/game3d/map/key.png", 122));
            
        } catch (IOException ex) {
            Logger.getLogger(Sprite.class.getName()).log(Level.SEVERE, null, ex);
        }
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
