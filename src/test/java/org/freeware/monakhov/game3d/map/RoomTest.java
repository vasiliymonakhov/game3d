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
     * Test of addLine method, of class Room.
     */
    @Test
    public void testAddAndGetWall1() {
        World w = new World();
        Room r = new Room();
        w.addPoint("p0", new Point(10, 20));
        w.addPoint("p1", new Point(30, 40));
        w.addPoint("p2", new Point(50, 60));
        r.addLine("w0", new Wall(w.getPoint("p0"), w.getPoint("p1"), null, w));
        r.addLine("w1", new Wall(w.getPoint("p1"), w.getPoint("p2"), null, w));
        r.addLine("w2", new Wall(w.getPoint("p2"), w.getPoint("p0"), null, w));
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
        r.addLine("w0", new Wall(null, null, null, null));
        r.addLine("w0", new Wall(null, null, null, null));
    }    

    /**
     * Test of getAllLines method, of class Room.
     */
    @Test
    public void testGetAllLines() {
        Room r = new Room();
        Line l1 = new Line(new Point (), new Point(), null);
        Line l2 = new Line(new Point (), new Point(), null);
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
        Line l = new Line(new Point (), new Point(), null);
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
        Line l = new Line(new Point (), new Point(), null);
        r.addLine("l0", l);
        r.addLine("l0", l);
    }    

    /**
     * Test of addLine method, of class Room.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddLine2() {
        Room r = new Room();
        Line l = new Line(new Point (), new Point(), null);
        r.addLine("", l);
    }
    
    /**
     * Test of addLine method, of class Room.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddLine3() {
        Room r = new Room();
        Line l = new Line(new Point (), new Point(), null);
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
        Line l1 = new Line(new Point (), new Point(), null);
        Line l2 = new Line(new Point (), new Point(), null);
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
        Line l1 = new Line(new Point (), new Point(), null);
        Line l2 = new Line(new Point (), new Point(), null);
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
        World w = new World();
        Room r = new Room();
        Point p0 = new Point(-10, 10);
        Point p1 = new Point(10, 10);
        Point p2 = new Point(10, -10);
        Point p3 = new Point(-10, -10);        
        w.addPoint("p0", p0);
        w.addPoint("p1", p1);
        w.addPoint("p2", p2);
        w.addPoint("p3", p3);        
        r.addLine("l0", new Line(p0, p1, w));
        r.addLine("l1", new Line(p1, p2, w));
        r.addLine("l2", new Line(p2, p3, w));
        r.addLine("l3", new Line(p3, p0, w));        
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