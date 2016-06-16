/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */
package org.freeware.monakhov.game3d.map;

import org.freeware.monakhov.game3d.map.Point;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Test for a class Point
 * @author Vasily Monakhov 
 */
public class PointTest  extends TestCase {
    
    final static double EPSILON = 0.0001d;

    /**
     * Test of moveTo method, of class Point.
     */
    @Test
    public void testMoveTo() {
        Point p = new Point(10, 20);
        p.moveTo(100, 200);
        assertEquals(100, p.getX(), EPSILON);
        assertEquals(200, p.getY(), EPSILON);
    }

    /**
     * Test of moveBy method, of class Point.
     */
    @Test
    public void testMoveBy() {
        Point p = new Point(10, 20);
        p.moveBy(100, 200);
        assertEquals(110, p.getX(), EPSILON);
        assertEquals(220, p.getY(), EPSILON);
    }

    /**
     * Test of getX method, of class Point.
     */
    @Test
    public void testGetX() {
        Point p = new Point(10, 20);
        assertEquals(10, p.getX(), EPSILON);
    }

    /**
     * Test of getY method, of class Point.
     */
    @Test
    public void testGetY() {
        Point p = new Point(10, 20);
        assertEquals(20, p.getY(), EPSILON);
    }
    
}
