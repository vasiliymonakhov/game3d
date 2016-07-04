package org.freeware.monakhov.game3d.map;

import java.awt.Graphics2D;
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
    
    public void draw(Screen screen, int dx, int dy) {
        Graphics2D g = (Graphics2D)screen.getImage().getGraphics();        
        for (Node n : nodes) {
            int x = (int)Math.round((double)(dx + n.x) * screen.getWidth() / width);
            int y = (int)Math.round((double)(dy + n.y) * screen.getHeight() / height);
            int w = (int)Math.round((double)n.i.getImage().getWidth() * screen.getWidth() / width);
            int h = (int)Math.round((double)n.i.getImage().getHeight() * screen.getHeight() / height);            
            g.drawImage(n.i.getImage(), x, y, w, h, null);            
        }
    }

}
