/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.maps;

import java.awt.image.BufferedImage;
import java.io.IOException;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Test of class Wall
 * @author Vasily Monakhov 
 */
public class WallTest {

    final static double EPSILON = 0.0001d;    
    
    /**
     * Test of getStart method, of class Wall.
     */
    @Test
    public void testGetStart() {
        Line w = new Wall(new Point(10, 20), new Point(30, 40), null);
        assertEquals(10, w.getStart().getX(), EPSILON);
        assertEquals(20, w.getStart().getY(), EPSILON);
    }

    /**
     * Test of getEnd method, of class Wall.
     */
    @Test
    public void testGetEnd() {
        Line w = new Wall(new Point(10, 20), new Point(30, 40), null);
        assertEquals(30, w.getEnd().getX(), EPSILON);
        assertEquals(40, w.getEnd().getY(), EPSILON);
    }

    /**
     * Test of isVisible method, of class Wall.
     */
    @Test
    public void testIsVisible() {
        Wall w = new Wall(null, null, null);
        assertTrue(w.isVisible());
    }

    /**
     * Test of getSubImage method, of class Wall.
     */
    @Test
    public void testGetSubImage() throws IOException {
        Wall w = new Wall(new Point(0, 0), new Point(2000, 0), new Texture("/org/freeware/monakhov/game3d/maps/brickwall.jpg"));
        Point p = new Point(10, 0);
        BufferedImage bi = w.getSubImage(p);
        assertNotNull(bi);
        assertEquals(1, bi.getWidth());
        assertEquals(128, bi.getHeight());
    }

}