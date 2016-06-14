/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This is a screen 
 * @author Vasily Monakhov 
 */
class Screen {

    private final BufferedImage doubleImage;
    
    private final BufferedImage screenImage;
    
    private final int width;
    private final int height;
    
    Screen (int width, int height) {
        this.width = width;
        this.height = height;
        screenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        doubleImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }
    
    private final ReentrantLock lock = new ReentrantLock();
    
    void paint(Graphics g, int screenX, int screenY) {
        try {
            lock.lock();
            g.drawImage(screenImage, screenX, screenY, null);
        } 
        finally {
            lock.unlock();
        }
    }

    void doubleBufferToScreen() {
        try {
            lock.lock();
            screenImage.setData(getDoubleImage().getData());
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
     * @return the doubleImage
     */
    public BufferedImage getDoubleImage() {
        return doubleImage;
    }
    
    
}
