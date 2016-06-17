/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This is a screen 
 * @author Vasily Monakhov 
 */
class Screen {

    private BufferedImage screenImage;
    private BufferedImage drawImage;
    
    private final int width;
    private final int height;
    
    Screen (int width, int height) {
        this.width = width;
        this.height = height;
        GraphicsConfiguration gfx_config = GraphicsEnvironment.
		getLocalGraphicsEnvironment().getDefaultScreenDevice().
		getDefaultConfiguration();
        screenImage = gfx_config.createCompatibleImage(width, height, Transparency.OPAQUE);
        drawImage = gfx_config.createCompatibleImage(width, height, Transparency.OPAQUE);
    }
    
    private final ReentrantLock lock = new ReentrantLock();
    
    void paint(Graphics g, int screenX, int screenY, int screenW, int screenH) {
        try {
            lock.lock();
            g.drawImage(screenImage, screenX, screenY, screenW, screenH, null);
        } 
        finally {
            lock.unlock();
        }
    }

    public void swapBuffers() {
        try {
            lock.lock();
            BufferedImage bi = screenImage;
            screenImage = drawImage;
            drawImage = bi;        
        } 
        finally {
            lock.unlock();
        }        
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

    /**
     * @return the Image
     */
    public BufferedImage getImage() {
        return drawImage;
    }
 
    
}
