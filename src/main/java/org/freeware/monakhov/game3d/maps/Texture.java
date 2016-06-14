package org.freeware.monakhov.game3d.maps;

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
        image = ImageIO.read(Texture.class.getResourceAsStream(fileName));
        width = image.getWidth();
        height = image.getHeight();
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
