/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.map;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 * 
 * @author Vasily Monakhov 
 */
public class TextureTest {

    @Before
    public void setUp() throws Exception {
        XMLResourceLoader xmlrl = new XMLResourceLoader();
        try (InputStream is = XMLWorldLoaderTest.class.getResourceAsStream("/org/freeware/monakhov/game3d/resources.xml")) {
            xmlrl.parse(is);
        }
        
    }    
    
    /**
     * Test of getImage method, of class Texture.
     */
    @Test
    public void testGetImage() {
        Texture tex = Texture.get("brick01");
        BufferedImage bi = tex.getImage(0);
        assertNotNull(bi);
        assertEquals(256, bi.getWidth());
        assertEquals(256, bi.getHeight());
        bi = tex.getImage(1);
        assertNotNull(bi);
        assertEquals(128, bi.getWidth());
        assertEquals(128, bi.getHeight());
        bi = tex.getImage(2);
        assertNotNull(bi);
        assertEquals(64, bi.getWidth());
        assertEquals(64, bi.getHeight());
    }

    /**
     * Test of getSubImage method, of class Texture.
     */
    @Test
    public void testGetSubImage() {
        Texture tex = Texture.get("brick01");        
        BufferedImage bi = tex.getSubImage(0, 350);
        assertNotNull(bi);
        assertEquals(1, bi.getWidth());
        assertEquals(256, bi.getHeight());
        bi = tex.getSubImage(0, 100);
        assertNotNull(bi);
        assertEquals(1, bi.getWidth(), 2);
        assertEquals(64, bi.getHeight());
    }

}