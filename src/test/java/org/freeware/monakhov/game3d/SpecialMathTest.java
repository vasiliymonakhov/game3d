
package org.freeware.monakhov.game3d;

import org.freeware.monakhov.game3d.map.Point;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * 
 * @author Vasily Monakhov 
 */
public class SpecialMathTest {

    final static double EPSILON = 0.0001d;          
    
    /**
     * Test of lineLength method, of class SpecialMath.
     */
    @Test
    public void testLineLength() {
        Point a = new Point();
        Point b = new Point(0, 10);
        Point c = new Point(10, 0);
        Point d = new Point(0, -10);
        Point e = new Point(-10, 0);
        assertEquals(10, SpecialMath.lineLength(a, b), EPSILON);
        assertEquals(10, SpecialMath.lineLength(a, c), EPSILON);
        assertEquals(10, SpecialMath.lineLength(a, d), EPSILON);
        assertEquals(10, SpecialMath.lineLength(a, e), EPSILON);
        assertEquals(20, SpecialMath.lineLength(b, d), EPSILON);
        assertEquals(20, SpecialMath.lineLength(c, e), EPSILON);
        assertEquals(Math.sqrt(200), SpecialMath.lineLength(b, e), EPSILON);
        assertEquals(Math.sqrt(200), SpecialMath.lineLength(d, c), EPSILON);        
    }

    /**
     * Test of triangleSquare method, of class SpecialMath.
     */
    @Test
    public void testTriangleSquare() {
        Point a = new Point();
        Point b = new Point(0, 10);
        Point c = new Point(10, 0);
        Point d = new Point(0, -10);
        Point e = new Point(-10, 0);
        assertEquals(50, SpecialMath.triangleSquare(a, b, c), EPSILON);
        assertEquals(50, SpecialMath.triangleSquare(a, c, d), EPSILON);        
        assertEquals(50, SpecialMath.triangleSquare(a, e, d), EPSILON);                
        assertEquals(50, SpecialMath.triangleSquare(a, e, b), EPSILON);                
        assertEquals(100, SpecialMath.triangleSquare(c, b, e), EPSILON);
        assertEquals(100, SpecialMath.triangleSquare(b, d, c), EPSILON);                
    }

    /**
     * Test of lineIntersection method, of class SpecialMath.
     */
    @Test
    public void testLineIntersection1() {
        Point b = new Point(0, 10);
        Point c = new Point(10, 0);
        Point d = new Point(0, -10);
        Point e = new Point(-10, 0);
        Point i = new Point();
        assertTrue(SpecialMath.lineIntersection(b, d, c, e, i));
        assertEquals(0, i.getX(), EPSILON);
        assertEquals(0, i.getY(), EPSILON);
        assertFalse(SpecialMath.lineIntersection(b, e, c, d, i));
        assertFalse(SpecialMath.lineIntersection(c, b, d, e, i));        
    }
    
    /**
     * Test of lineIntersection method, of class SpecialMath.
     */
    @Test
    public void testLineIntersection2() {
        Point a = new Point(-20, 30);        
        Point b = new Point(-10, 10);
        Point c = new Point(10, 10);
        Point d = new Point(20, 30);
        Point i = new Point();
        assertTrue(SpecialMath.lineIntersection(a, b, c, d, i));
        assertEquals(0, i.getX(), EPSILON);
        assertEquals(-10, i.getY(), EPSILON);
    }    

    @Test
    public void testLineAndCircleIntersection() {
        Point a = new Point(-1, 2);
        Point b = new Point (2, 2);
        Point o = new Point (1, 1);
        Point p1 = new Point();
        Point p2 = new Point();
        assertEquals(0, SpecialMath.lineAndCircleIntersection(a, b, o, 0.5, p1, p2));
        assertEquals(1, SpecialMath.lineAndCircleIntersection(a, b, o, 1, p1, p2));
        assertEquals(1, p1.getX(), EPSILON);
        assertEquals(2, p1.getY(), EPSILON);
        assertEquals(2, SpecialMath.lineAndCircleIntersection(a, b, o, 2, p1, p2));
        assertEquals(-0.73205, p1.getX(), EPSILON);
        assertEquals(2, p1.getY(), EPSILON);
        assertEquals(2.73205, p2.getX(), EPSILON);
        assertEquals(2, p2.getY(), EPSILON);        
        
        a = new Point(2, 2);
        b = new Point (2, -2);        
        assertEquals(0, SpecialMath.lineAndCircleIntersection(a, b, o, 0.5, p1, p2));
        assertEquals(1, SpecialMath.lineAndCircleIntersection(a, b, o, 1, p1, p2));
        assertEquals(2, p1.getX(), EPSILON);
        assertEquals(1, p1.getY(), EPSILON);
        assertEquals(2, SpecialMath.lineAndCircleIntersection(a, b, o, 2, p1, p2));
        assertEquals(2, p1.getX(), EPSILON);
        assertEquals(2.73205, p1.getY(), EPSILON);
        assertEquals(2, p2.getX(), EPSILON);
        assertEquals(-0.73205, p2.getY(), EPSILON);                
        
        a = new Point(2, 2);
        b = new Point (-2, -2);              
        o = new Point();
        assertEquals(2, SpecialMath.lineAndCircleIntersection(a, b, o, Math.sqrt(2), p1, p2));
        assertEquals(1, p1.getX(), EPSILON);
        assertEquals(1, p1.getY(), EPSILON);
        assertEquals(-1, p2.getX(), EPSILON);
        assertEquals(-1, p2.getY(), EPSILON);                        
    }
    
}