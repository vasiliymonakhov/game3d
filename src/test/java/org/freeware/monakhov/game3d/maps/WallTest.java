/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.maps;

import static junit.framework.Assert.assertEquals;
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
        Line w = new Wall(new Point(10, 20), new Point(30, 40));
        assertEquals(10, w.getStart().getX(), EPSILON);
        assertEquals(20, w.getStart().getY(), EPSILON);
    }

    /**
     * Test of getEnd method, of class Wall.
     */
    @Test
    public void testGetEnd() {
        Line w = new Wall(new Point(10, 20), new Point(30, 40));
        assertEquals(30, w.getEnd().getX(), EPSILON);
        assertEquals(40, w.getEnd().getY(), EPSILON);
    }

}