
package org.freeware.monakhov.game3d;

import org.freeware.monakhov.game3d.map.Point;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * 
 * @author Vasily Monakhov 
 */
public class SpecialMathTest {

    @Test    
    public void testCheckCross1() {
        Point p1 = new Point(1, 0);
        Point p2 = new Point(-1, 0);
        Point p3 = new Point(0, 1);
        Point p4 = new Point(0, -1);
        assertTrue(SpecialMath.checkCross(p1, p2, p3, p4));
        Point p5 = new Point(1, 2);
        Point p6 = new Point(-1, 2);
        assertFalse(SpecialMath.checkCross(p1, p2, p5, p6));
        Point p7 = new Point(1, -2);
        Point p8 = new Point(-1, -2);
        assertFalse(SpecialMath.checkCross(p1, p2, p7, p8));   
        Point p9 = new Point(2, 2);
        Point p10 = new Point(2, -2);
        assertFalse(SpecialMath.checkCross(p1, p2, p9, p10));   
        Point p11 = new Point(2, 2);
        Point p12 = new Point(2, -2);        
        assertFalse(SpecialMath.checkCross(p1, p2, p11, p12));   
    }
    
    @Test    
    public void testCheckCross2() {
        Point p1 = new Point(0.5, 1);
        Point p2 = new Point(1, 2);
        Point p3 = new Point(1, 1);
        Point p4 = new Point(2, 2);
        Point p5 = new Point(1, 0.5);
        Point p6 = new Point(2, 1);
        assertFalse(SpecialMath.checkCross(p1, p2, p3, p4));
        assertFalse(SpecialMath.checkCross(p1, p2, p5, p6));   
        assertTrue(SpecialMath.checkCross(p3, p4, p2, p6));   
        assertFalse(SpecialMath.checkCross(p3, p4, p1, p5));   
    }   

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

}