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
public class TextureTest {

    private final Texture tex;
    
    public TextureTest() throws IOException {
        tex = new Texture("/org/freeware/monakhov/game3d/maps/brickwall.jpg");
    }

    /**
     * Test of getImage method, of class Texture.
     */
    @Test
    public void testGetImage() {
        BufferedImage bi = tex.getImage();
        assertNotNull(bi);
        assertEquals(256, bi.getWidth());
        assertEquals(128, bi.getHeight());
    }

    /**
     * Test of getSubImage method, of class Texture.
     */
    @Test
    public void testGetSubImage() {
        BufferedImage bi = tex.getSubImage(0, 0, 10, 20);
        assertNotNull(bi);
        assertEquals(10, bi.getWidth());
        assertEquals(20, bi.getHeight());
    }

    /**
     * Test of getWidth method, of class Texture.
     */
    @Test
    public void testGetWidth() {
        assertEquals(256, tex.getWidth());
    }

    /**
     * Test of getHeight method, of class Texture.
     */
    @Test
    public void testGetHeight() {
        assertEquals(128, tex.getHeight());
    }

}