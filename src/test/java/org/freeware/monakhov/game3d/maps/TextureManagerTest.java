/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.maps;

import java.awt.image.BufferedImage;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author Vasily Monakhov 
 */
public class TextureManagerTest {

    /**
     * Test of add method, of class TextureManager.
     * @throws java.lang.Exception
     */
    @Test
    public void testAdd1() throws Exception {
        TextureManager manager = new TextureManager();
        manager.add("brick01", "/org/freeware/monakhov/game3d/maps/brick01.jpg");
        manager.add("brick02", "/org/freeware/monakhov/game3d/maps/brick02.jpg");
        manager.add("brick03", "/org/freeware/monakhov/game3d/maps/brick03.jpg");        
    }

    /**
     * Test of add method, of class TextureManager.
     * @throws java.lang.Exception
     */
    @Test (expected = Exception.class)
    public void testAdd2() throws Exception {
        TextureManager manager = new TextureManager();
        manager.add("brick01", "/org/freeware/monakhov/game3d/maps/brick01.jpg");
        manager.add("brick01", "/org/freeware/monakhov/game3d/maps/brick02.jpg");
    }    
    
    /**
     * Test of add method, of class TextureManager.
     * @throws java.lang.Exception
     */
    @Test (expected = Exception.class)
    public void testAdd3() throws Exception {
        TextureManager manager = new TextureManager();
        manager.add("brick01", "/org/freeware/monakhov/game3d/maps/brick01.jpg");
        manager.add("", "/org/freeware/monakhov/game3d/maps/brick02.jpg");
    }
    
    /**
     * Test of add method, of class TextureManager.
     * @throws java.lang.Exception
     */
    @Test (expected = Exception.class)
    public void testAdd4() throws Exception {
        TextureManager manager = new TextureManager();
        manager.add("brick01", "/org/freeware/monakhov/game3d/maps/brick01.jpg");
        manager.add(null, "/org/freeware/monakhov/game3d/maps/brick02.jpg");
    }    
    
    /**
     * Test of get method, of class TextureManager.
     */
    @Test
    public void testGet1() throws IOException {
        TextureManager manager = new TextureManager();
        manager.add("brick01", "/org/freeware/monakhov/game3d/maps/brick01.jpg");
        manager.add("brick02", "/org/freeware/monakhov/game3d/maps/brick02.jpg");
        manager.add("brick03", "/org/freeware/monakhov/game3d/maps/brick03.jpg");        
        Texture t1 = manager.get("brick01");
        assertNotNull(t1);
        Texture t2 = manager.get("brick02");
        assertNotNull(t2);
        assertTrue(t1 != t2);
        Texture t3 = manager.get("brick03");
        assertNotNull(t3);
        assertTrue(t1 != t3);
        assertTrue(t2 != t3);
        Texture t4 = manager.get("brick01");
        assertNotNull(t4);        
        assertTrue(t1 == t4);
        assertEquals(t1.getImage(), t4.getImage());
    }
    
    public void assertImageEquals(BufferedImage bi1, BufferedImage bi2) {
        assertEquals(bi1.getWidth(), bi2.getWidth());
        assertEquals(bi1.getHeight(), bi2.getHeight());
        for (int x = 0; x < bi1.getWidth(); x++) {
            for (int y = 0; y < bi1.getHeight(); y++) {
                assertEquals(bi1.getRGB(x, y), bi2.getRGB(x, y));
            }
        }
    }    

    /**
     * Test of get method, of class TextureManager.
     */
    @Test
    public void testGet2() throws IOException {
        TextureManager manager = new TextureManager();
        manager.add("brick01", "/org/freeware/monakhov/game3d/maps/brickwall.jpg");
        manager.add("brick02", "/org/freeware/monakhov/game3d/maps/brickwall.jpg");
        Texture t1 = manager.get("brick01");
        assertNotNull(t1);
        Texture t2 = manager.get("brick02");
        assertNotNull(t2);
        assertTrue(t1 != t2);
        assertImageEquals(t1.getImage(), t2.getImage());
    }    
    
    /**
     * Test of get method, of class TextureManager.
     */
    @Test  (expected = Exception.class)
    public void testGet3() throws IOException {
        TextureManager manager = new TextureManager();
        manager.add("brick01", "/org/freeware/monakhov/game3d/maps/brickwall.jpg");
        manager.add("brick02", "/org/freeware/monakhov/game3d/maps/brickwall.jpg");
        Texture t1 = manager.get("brick01");
        Texture t2 = manager.get("brick02");
        Texture t3 = manager.get("brick03");
    }        
    
    /**
     * Test of get method, of class TextureManager.
     */
    @Test  (expected = Exception.class)
    public void testGet4() throws IOException {
        TextureManager manager = new TextureManager();
        manager.add("brick01", "/org/freeware/monakhov/game3d/maps/brickwall.jpg");
        manager.add("brick02", "/org/freeware/monakhov/game3d/maps/brickwall.jpg");
        Texture t1 = manager.get(null);
    }    
    
}