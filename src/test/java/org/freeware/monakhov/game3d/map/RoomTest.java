/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.map;

import java.util.Collection;
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
    public void testAddPoint() {
        Room r = new Room();
        r.addPoint("p0", new Point(10, 20));
        r.addPoint("p0", new Point(30, 40));
    }    
    
    /**
     * Test of addPoint method, of class Room.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddPoint2() {
        Room r = new Room();
        r.addPoint("", new Point(10, 20));
    }    

    /**
     * Test of addPoint method, of class Room.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddPoint3() {
        Room r = new Room();
        r.addPoint(null, new Point(10, 20));
    }        
    
    /**
     * Test of addPoint method, of class Room.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddPoint4() {
        Room r = new Room();
        r.addPoint("p0", null);
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

    /**
     * Test of getPoint method, of class Room.
     */
    @Test
    public void testGetPoint() {
        Room r = new Room();
        r.addPoint("p0", new Point(10, 20));
        Point p = r.getPoint("p0");
        assertEquals(10, p.getX(), EPSILON);
        assertEquals(20, p.getY(), EPSILON);
    }

    /**
     * Test of getAllPoints method, of class Room.
     */
    @Test
    public void testGetAllPoints() {
        Room r = new Room();
        Point p0 = new Point(10, 20);
        Point p1 = new Point(30, 40);
        Point p2 = new Point(50, 60);
        r.addPoint("p0", p0);
        r.addPoint("p1", p1);
        r.addPoint("p2", p2);
        Collection<Point> points = r.getAllPoints();
        assertTrue(points.contains(p0));
        assertTrue(points.contains(p1));
        assertTrue(points.contains(p2));
    }

    /**
     * Test of getAllLines method, of class Room.
     */
    @Test
    public void testGetAllLines() {
        Room r = new Room();
        Line l1 = new Line(new Point (), new Point());
        Line l2 = new Line(new Point (), new Point());
        r.addLine("l1", l1);
        r.addLine("l2", l2);  
        Collection<Line> res = r.getAllLines();
        assertEquals(2, res.size());
        assertTrue(res.contains(l1));
        assertTrue(res.contains(l2));
    }

    /**
     * Test of addLine method, of class Room.
     */
    @Test
    public void testAddLine() {
        Room r = new Room();
        Line l = new Line(new Point (), new Point());
        r.addLine("l0", l);
        Line lr = r.getLine("l0");
        assertNotNull(lr);
        assertTrue(l == lr);
    }

    /**
     * Test of addLine method, of class Room.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddLine1() {
        Room r = new Room();
        Line l = new Line(new Point (), new Point());
        r.addLine("l0", l);
        r.addLine("l0", l);
    }    

    /**
     * Test of addLine method, of class Room.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddLine2() {
        Room r = new Room();
        Line l = new Line(new Point (), new Point());
        r.addLine("", l);
    }
    
    /**
     * Test of addLine method, of class Room.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddLine3() {
        Room r = new Room();
        Line l = new Line(new Point (), new Point());
        r.addLine(null, l);
    }
    
    /**
     * Test of addLine method, of class Room.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddLine4() {
        Room r = new Room();
        r.addLine("l0", null);
    }    
    
    
    /**
     * Test of getLine method, of class Room.
     */
    @Test
    public void testGetLine() {
        Room r = new Room();
        Line l1 = new Line(new Point (), new Point());
        Line l2 = new Line(new Point (), new Point());
        r.addLine("l1", l1);
        r.addLine("l2", l2);        
        Line lr1 = r.getLine("l1");
        assertNotNull(lr1);
        assertTrue(l1 == lr1);
        Line lr2 = r.getLine("l2");
        assertNotNull(lr2);
        assertTrue(l2 == lr2);        
        assertTrue(lr1 != lr2);                
    }

    /**
     * Test of clearRoomVisibilityAlreadyChecked method, of class Room.
     */
    @Test
    public void testClearRoomVisibilityAlreadyChecked() {
        Room r = new Room();
        Line l1 = new Line(new Point (), new Point());
        Line l2 = new Line(new Point (), new Point());
        r.addLine("l1", l1);
        r.addLine("l2", l2);   
        r.roomVisibilityAlreadyChecked = true;
        r.clearRoomVisibilityAlreadyChecked();
    }

    /**
     * Test of insideThisRoom method, of class Room.
     */
    @Test
    public void testInsideThisRoom() {
        Room r = new Room();
        Point p0 = new Point(-10, 10);
        Point p1 = new Point(10, 10);
        Point p2 = new Point(10, -10);
        Point p3 = new Point(-10, -10);        
        r.addPoint("p0", p0);
        r.addPoint("p1", p1);
        r.addPoint("p2", p2);
        r.addPoint("p3", p3);        
        r.addLine("l0", new Line(p0, p1));
        r.addLine("l1", new Line(p1, p2));
        r.addLine("l2", new Line(p2, p3));
        r.addLine("l3", new Line(p3, p0));        
        Point pt0 = new Point(0, 0);
        assertTrue(r.insideThisRoom(pt0));
        Point pt1 = new Point(20, 0);
        Point pt2 = new Point(0, 20);
        Point pt3 = new Point(-20, 0);
        Point pt4 = new Point(0, -20);
        Point pt5 = new Point(20, 20);
        Point pt6 = new Point(-20, 20);
        Point pt7 = new Point(-20, -20);
        Point pt8 = new Point(20, -20);        
        assertFalse(r.insideThisRoom(pt1));
        assertFalse(r.insideThisRoom(pt2));
        assertFalse(r.insideThisRoom(pt3));
        assertFalse(r.insideThisRoom(pt4));
        assertFalse(r.insideThisRoom(pt5));
        assertFalse(r.insideThisRoom(pt6));
        assertFalse(r.insideThisRoom(pt7));
        assertFalse(r.insideThisRoom(pt8));
    }

}