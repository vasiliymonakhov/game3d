/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.resources;

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
        assertEquals(64, bi.getWidth());
        assertEquals(64, bi.getHeight());
    }

    /**
     * Test of getSubImage method, of class Sprite.
     */
    @Test
    public void testGetSubImage() {
        Sprite spr = Sprite.get("green_barrel");
        assertNotNull(spr);
        BufferedImage bi = spr.getSubImage(10, 0, 1, 16);
        assertNotNull(bi);
        assertEquals(1, bi.getWidth());
        assertEquals(4, bi.getHeight());
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
        assertEquals(64, spr.getWidth());
        assertEquals(64, spr.getHeight());
        assertEquals(192, spr.getYOffset());
    }

}