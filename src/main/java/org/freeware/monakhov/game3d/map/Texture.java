package org.freeware.monakhov.game3d.map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Класс для хранения текстур
 * @author Vasily Monakhov 
 */
public class Texture {
    
    private final BufferedImage image;
    
    private final static int SIZE = 256;
    
    Texture(String fileName) throws IOException {
        BufferedImage bi = ImageIO.read(Texture.class.getResourceAsStream(fileName));
        GraphicsConfiguration gfx_config = GraphicsEnvironment.
		getLocalGraphicsEnvironment().getDefaultScreenDevice().
		getDefaultConfiguration();
            image = gfx_config.createCompatibleImage(SIZE, SIZE, Transparency.OPAQUE);        
            Graphics2D g = (Graphics2D)image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.drawImage(bi, 0, 0, SIZE, SIZE, null);
            g.dispose();
    }

    /**
     * @return the image
     */
    public BufferedImage getImage() {
        return image;
    }
    
    public BufferedImage getSubImage(int x) {
        return image.getSubimage(x % SIZE, 0, 1, SIZE);
    }

    /**
     * Карта для хранения текстур
     */
    private final static Map<String, Texture> textures = new LinkedHashMap<>();
    
    /**
     * Добавляет новую текстуру
     * @param id идентификатор текстуры
     * @param fileName имя файла с текстурой
     * @throws IOException 
     */
    public static void add(String id, String fileName) throws IOException {
        if (textures.containsKey(id)) {
            throw new IllegalArgumentException("Texture " + id + " already exists");             
        }
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Texture id is null or empty");             
        }        
        textures.put(id, new Texture(fileName));
    }
    
    public static Texture get(String id) {
        Texture tex = textures.get(id);
        if (tex== null) {
            throw new IllegalArgumentException("Texture " + id + " not exists"); 
        }
        return tex;
    }
    
    static {
        try {
            add("brick01", "/org/freeware/monakhov/game3d/map/brick01.jpg");
            add("brick02", "/org/freeware/monakhov/game3d/map/brick02.jpg");    
            add("brick03", "/org/freeware/monakhov/game3d/map/brick03.jpg");
            add("floor-ceiling", "/org/freeware/monakhov/game3d/map/floor-ceiling.png");
        } catch (IOException ex) {
            Logger.getLogger(Texture.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
