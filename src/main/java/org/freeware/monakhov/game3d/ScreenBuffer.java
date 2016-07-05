package org.freeware.monakhov.game3d;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Буфер экрана с двойной буферизацией
 *
 * @author Vasily Monakhov
 */
public class ScreenBuffer {

    /**
     * Текущее изображение, которое показывается пользователю
     */
    private BufferedImage screenImage;
    /**
     * Изображение, которое создаётся
     */
    private BufferedImage drawImage;

    /**
     * ширина буфера экрана
     */
    private final int width;
    /**
     * высота буфера экрана
     */
    private final int height;

    /**
     * Создайт буфер
     * @param width ширина
     * @param height высота
     */
    ScreenBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        GraphicsConfiguration gfx_config = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();
        screenImage = gfx_config.createCompatibleImage(width, height, Transparency.OPAQUE);
        screenImage.setAccelerationPriority(1);        
        drawImage = gfx_config.createCompatibleImage(width, height, Transparency.OPAQUE);
        drawImage.setAccelerationPriority(1);        
    }

    /**
     * Блокировка для исключения одновременного рисования и отображения
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * Рисует содержимое буфера в графическом контексте
     * @param g графический контекст
     * @param screenX горизонтальная координата изображения
     * @param screenY вертикальная координата изображения
     * @param screenW ширина изображения
     * @param screenH высота изображения
     */
    void paint(Graphics g, int screenX, int screenY, int screenW, int screenH) {
        try {
            lock.lock();
            g.drawImage(screenImage, screenX, screenY, screenW, screenH, null);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Меняет местами изображения. 
     */
    public void swapBuffers() {
        try {
            lock.lock();
            BufferedImage bi = screenImage;
            screenImage = drawImage;
            drawImage = bi;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Возвращает ширину буфера
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * возыращает высоту буфера
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Возвращает изображение для рисования
     * @return the Image
     */
    public BufferedImage getImage() {
        return drawImage;
    }

    /**
     * Счётчик порядковых но меров для скриншотов
     */
    private int screenShotCounter = 1;
    
    /**
     * Делает скриншот - сохраняет изображение из буфера в файл
     */
    public void makeScreenShot() {
        try {
            lock.lock();
            final BufferedImage bi = new BufferedImage(width, height, Transparency.OPAQUE);
            Graphics g = bi.getGraphics();
            g.drawImage(screenImage, 0, 0, null);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ImageIO.write(bi, "PNG", new File(String.format("screen%05d.png", screenShotCounter++)));
                    } catch (IOException ex) {
                        Logger.getLogger(ScreenBuffer.class.getName()).log(Level.SEVERE, "Can't save screenshot", ex);
                    }
                }
            });
            t.start();
        }
        finally {
            lock.unlock();
        }        
    }
    
}
