/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.map;

import org.freeware.monakhov.game3d.map.Wall;
import org.freeware.monakhov.game3d.map.Room;
import org.freeware.monakhov.game3d.map.Point;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Testing class Room
 * @author Vasily Monakhov 
 */
public class RoomTest {

    final static double EPSILON = 0.0001d;
    
    /**
     * Test of addPoint method, of class Room.
     */
    @Test
    public void testAddAndGetPoint1() {
        Room r = new Room();
        r.addPoint("p0", new Point(10, 20));
        r.addPoint("p1", new Point(30, 40));
        r.addPoint("p2", new Point(50, 60));
        
        Point p = r.getPoint("p0");
        assertEquals(10, p.getX(), EPSILON);
        assertEquals(20, p.getY(), EPSILON);
        p = r.getPoint("p1");
        assertEquals(30, p.getX(), EPSILON);
        assertEquals(40, p.getY(), EPSILON);
        p = r.getPoint("p2");
        assertEquals(50, p.getX(), EPSILON);
        assertEquals(60, p.getY(), EPSILON);
        p = r.getPoint("p3");
        assertNull(p);
    }

    /**
     * Test of addPoint method, of class Room.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddAndGetPoint2() {
        Room r = new Room();
        r.addPoint("p0", new Point(10, 20));
        r.addPoint("p0", new Point(30, 40));
    }    
    
    /**
     * Test of addLine method, of class Room.
     */
    @Test
    public void testAddAndGetWall1() {
        Room r = new Room();
        r.addPoint("p0", new Point(10, 20));
        r.addPoint("p1", new Point(30, 40));
        r.addPoint("p2", new Point(50, 60));
        r.addLine("w0", new Wall(r.getPoint("p0"), r.getPoint("p1"), null));
        r.addLine("w1", new Wall(r.getPoint("p1"), r.getPoint("p2"), null));
        r.addLine("w2", new Wall(r.getPoint("p2"), r.getPoint("p0"), null));
        assertEquals(10, r.getLine("w0").getStart().getX(), EPSILON);
        assertEquals(20, r.getLine("w0").getStart().getY(), EPSILON);
        assertEquals(30, r.getLine("w0").getEnd().getX(), EPSILON);
        assertEquals(40, r.getLine("w0").getEnd().getY(), EPSILON);
        assertEquals(30, r.getLine("w1").getStart().getX(), EPSILON);
        assertEquals(40, r.getLine("w1").getStart().getY(), EPSILON);
        assertEquals(50, r.getLine("w1").getEnd().getX(), EPSILON);
        assertEquals(60, r.getLine("w1").getEnd().getY(), EPSILON);        
        assertEquals(50, r.getLine("w2").getStart().getX(), EPSILON);
        assertEquals(60, r.getLine("w2").getStart().getY(), EPSILON);
        assertEquals(10, r.getLine("w2").getEnd().getX(), EPSILON);
        assertEquals(20, r.getLine("w2").getEnd().getY(), EPSILON);                
    }

/**
     * Test of addLine method, of class Room.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddAndGetWall2() {
        Room r = new Room();
        r.addLine("w0", new Wall(null, null, null));
        r.addLine("w0", new Wall(null, null, null));
    }    

}