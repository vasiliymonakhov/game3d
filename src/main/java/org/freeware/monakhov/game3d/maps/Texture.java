package org.freeware.monakhov.game3d.maps;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Класс для хранения текстур
 * @author Vasily Monakhov 
 */
public class Texture {
    
    private final BufferedImage image;
    private final int width, height;
    
    Texture(String fileName) throws IOException {
        BufferedImage bi = ImageIO.read(Texture.class.getResourceAsStream(fileName));
        GraphicsConfiguration gfx_config = GraphicsEnvironment.
		getLocalGraphicsEnvironment().getDefaultScreenDevice().
		getDefaultConfiguration();
        width = bi.getWidth();
        height = bi.getHeight();        
        image = gfx_config.createCompatibleImage(width, height, Transparency.OPAQUE);        
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
    
    public BufferedImage getSubImage(int x, int y, int w, int h) {
        return image.getSubimage(x, y, w, h);
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }
    
}
