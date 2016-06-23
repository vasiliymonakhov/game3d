/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.map;

import java.awt.image.BufferedImage;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 * 
 * @author Vasily Monakhov 
 */
public class SpriteTest {

    @Before
    public void setUp() throws Exception {
        XMLResourceLoader xmlrl = new XMLResourceLoader();
        xmlrl.parse(XMLWorldLoaderTest.class.getResourceAsStream("/org/freeware/monakhov/game3d/resources.xml"));
    }    
    
    /**
     * Test of getImage method, of class Sprite.
     */
    @Test
    public void testGetImage() {
        Sprite spr = Sprite.get("green_barrel");
        assertNotNull(spr);
        BufferedImage bi = spr.getImage(0);
        assertNotNull(bi);
        assertEquals(80, bi.getWidth());
        assertEquals(132, bi.getHeight());
    }

    /**
     * Test of getSubImage method, of class Sprite.
     */
    @Test
    public void testGetSubImage() {
        Sprite spr = Sprite.get("green_barrel");
        assertNotNull(spr);
        BufferedImage bi = spr.getSubImage(10, 0, 340);
        assertNotNull(bi);
        assertEquals(1, bi.getWidth());
        assertEquals(132, bi.getHeight());        
        bi = spr.getSubImage(10, 0, 200);
        assertNotNull(bi);
        assertEquals(1, bi.getWidth());
        assertEquals(66, bi.getHeight());                
    }

    /**
     * Test of get method, of class Sprite.
     */
    @Test
    public void testGet1() {
        Sprite spr1 = Sprite.get("green_barrel");
        assertNotNull(spr1);
        Sprite spr2 = Sprite.get("tree");
        assertNotNull(spr2);
        assertTrue(spr1 != spr2);
        Sprite spr3 = Sprite.get("green_barrel");
        assertNotNull(spr3);        
        assertTrue(spr1 == spr3);        
    }

    /**
     * Test of get method, of class Sprite.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testGet2() {
        Sprite spr1 = Sprite.get(null);
    }    
    
    /**
     * Test of get method, of class Sprite.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testGet3() {
        Sprite spr1 = Sprite.get("");
    }    
    
    /**
     * Test of get method, of class Sprite.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testGet4() {
        Sprite spr1 = Sprite.get("asdaaccsvknvfdsnvakjvnfdvvkasv");
    }        
    
    /**
     * Test of getWidth method, of class Sprite.
     */
    @Test
    public void testGetParams() {
        Sprite spr = Sprite.get("green_barrel");
        assertNotNull(spr);
        assertEquals(80, spr.getWidth());
        assertEquals(132, spr.getHeight());  
        assertEquals(124, spr.getYOffset());  
    }

}