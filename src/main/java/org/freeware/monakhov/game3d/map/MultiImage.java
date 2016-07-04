package org.freeware.monakhov.game3d.map;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.freeware.monakhov.game3d.Screen;

/**
 *
 * @author Vasily Monakhov 
 */
public class MultiImage {
    
    private class Node {
        private final Image i;
        private final int x;
        private final int y;
        Node(Image i, int x, int y) {
            this.i = i;
            this.x = x;
            this.y = y;
        }
    }
    
    private final int width;
    private final int height;
    
    public MultiImage(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    private final List<Node> nodes = new ArrayList<>();
    
    void addImage(Image i, int x, int y) {
        if (i == null) {
            throw new IllegalArgumentException("Image is null or empty");
        }        
        nodes.add(new Node(i, x, y));
    }
    
    /**
     * Карта для хранения изображений
     */
    private final static Map<String, MultiImage> multiImages = new LinkedHashMap<>();
    
    public static MultiImage add(String id, int width, int height) throws IllegalArgumentException {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("MultiImage id is null or empty");
        }
        MultiImage mi = new MultiImage(width, height);
        multiImages.put(id, mi);
        return mi;
    }

    public static MultiImage get(String id) {
        MultiImage img = multiImages.get(id);
        if (img == null) {
            throw new IllegalArgumentException("MultiImage " + id + " not exists");
        }
        return img;
    }    
    
    public class ImageToDraw {
        
        private final BufferedImage i;
        private final int x;
        private final int y;   
        
        ImageToDraw (Node n, Screen screen) {
            GraphicsConfiguration gfx_config = GraphicsEnvironment.
                    getLocalGraphicsEnvironment().getDefaultScreenDevice().
                    getDefaultConfiguration();
            x = (int)Math.round((double)n.x * screen.getWidth() / width);
            y = (int)Math.round((double)n.y * screen.getHeight() / height);
            int w = (int)Math.round((double)n.i.getImage().getWidth() * screen.getWidth() / width);
            int h = (int)Math.round((double)n.i.getImage().getHeight() * screen.getHeight() / height);                        
            i = gfx_config.createCompatibleImage(w, h, n.i.getImage().getColorModel().getTransparency());
            Graphics2D g = (Graphics2D) i.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);                    
            g.drawImage(n.i.getImage(), 0, 0, w, h, null);            
            g.dispose();            
        }

        /**
         * @return the i
         */
        public BufferedImage getI() {
            return i;
        }

        /**
         * @return the x
         */
        public int getX() {
            return x;
        }

        /**
         * @return the y
         */
        public int getY() {
            return y;
        }
        
    }
    
    private List<ImageToDraw> imagesToDraw;
    private int sw = 0, sh = 0;
    
    public List<ImageToDraw> getImagesToDraw(Screen screen) {
        if (imagesToDraw == null) {
            imagesToDraw = new ArrayList<>();
        }
        if (sw != screen.getWidth() || sh != screen.getHeight()) {
            imagesToDraw.clear();
            sw = screen.getWidth();
            sh = screen.getHeight();
            for (Node n : nodes) {
                imagesToDraw.add(new ImageToDraw(n, screen));
            }
        }
        return imagesToDraw;
    }

}
