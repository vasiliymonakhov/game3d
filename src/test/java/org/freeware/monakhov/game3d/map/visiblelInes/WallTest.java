/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.map.visiblelInes;

import org.freeware.monakhov.game3d.map.visiblelines.Wall;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import org.freeware.monakhov.game3d.map.Line;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Texture;
import org.freeware.monakhov.game3d.map.XMLResourceLoader;
import org.freeware.monakhov.game3d.map.XMLWorldLoaderTest;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of class Wall
 * @author Vasily Monakhov 
 */
public class WallTest {

    final static double EPSILON = 0.0001d;    
    
    @Before
    public void setUp() throws Exception {
        XMLResourceLoader xmlrl = new XMLResourceLoader();
        try (InputStream is = XMLWorldLoaderTest.class.getResourceAsStream("/org/freeware/monakhov/game3d/resources.xml")) {
            xmlrl.parse(is);
        }
    }    
    
    /**
     * Test of getStart method, of class Wall.
     */
    @Test
    public void testGetStart() {
        Line w = new Wall(new Point(10, 20), new Point(30, 40), null, null);
        assertEquals(10, w.getStart().getX(), EPSILON);
        assertEquals(20, w.getStart().getY(), EPSILON);
    }

    /**
     * Test of getEnd method, of class Wall.
     */
    @Test
    public void testGetEnd() {
        Line w = new Wall(new Point(10, 20), new Point(30, 40), null, null);
        assertEquals(30, w.getEnd().getX(), EPSILON);
        assertEquals(40, w.getEnd().getY(), EPSILON);
    }

    /**
     * Test of isVisible method, of class Wall.
     */
    @Test
    public void testIsVisible() {
        Wall w = new Wall(null, null, null, null);
        assertTrue(w.isVisible());
    }

    /**
     * Test of getSubImage method, of class Wall.
     * @throws java.io.IOException
     */
    @Test
    public void testGetSubImage() throws IOException {
        Wall w = new Wall(new Point(0, 0), new Point(2000, 0), Texture.get("brick01"), null);
        Point p = new Point(10, 0);
        BufferedImage bi = w.getSubImage(p, 256);
        assertNotNull(bi);
        assertEquals(1, bi.getWidth());
        assertEquals(256, bi.getHeight());
        bi = w.getSubImage(p, 100);
        assertNotNull(bi);
        assertEquals(1, bi.getWidth());
        assertEquals(64, bi.getHeight());        
    }

}