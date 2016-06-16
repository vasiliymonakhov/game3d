/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.objects.movable;

import org.freeware.monakhov.game3d.objects.movable.Hero;
import org.freeware.monakhov.game3d.map.Line;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Room;
import org.freeware.monakhov.game3d.map.Wall;
import org.freeware.monakhov.game3d.map.World;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author Vasily Monakhov 
 */
public class HeroTest {

    final static double EPSILON = 0.0001d;   
    
    /**
     * Test of getAsimuth method, of class Hero.
     */
    @Test
    public void testSetAndGetAsimuth() {
        Hero h = new Hero(new Point(0, 0), null);
        assertEquals(0, h.getAsimuth(), EPSILON);
        h.setAsimuth(1);
        assertEquals(1, h.getAsimuth(), EPSILON);
        h.setAsimuth(Math.PI);
        assertEquals(Math.PI, h.getAsimuth(), EPSILON);
        h.setAsimuth(2 * Math.PI + EPSILON / 2);
        assertEquals(0, h.getAsimuth(), EPSILON);
        h.setAsimuth(3 * Math.PI);
        assertEquals(Math.PI, h.getAsimuth(), EPSILON);
    }

    /**
     * Test of getRoom method, of class Hero.
     */
    @Test
    public void testSetAndGetRoom1() {
        Hero h = new Hero(new Point(0, 0), null);
        assertNull(h.getRoom());
        Room r1 = new Room();
        h.setRoom(r1);
        assertNotNull(h.getRoom());
        assertTrue(r1 == h.getRoom());
        Room r2 = new Room();
        h.setRoom(r2);
        assertNotNull(h.getRoom());
        assertTrue(r2 == h.getRoom());        
    }
    
    /**
     * Test of getRoom method, of class Hero.
     */
    @Test (expected = Exception.class)
    public void testSetAndGetRoom2() {
        Hero h = new Hero(new Point(0, 0), null);
        h.setRoom(null);
    }    

    /**
     * Test of moveBy method, of class Hero.
     */
    @Test
    public void testMoveBy() {
        World w = new World();
        Point p0 = new Point (0, 0);
        Point p1 = new Point (0, 10);
        Point p2 = new Point (10, 10);
        Point p3 = new Point (10, 0);
        Point p4 = new Point (20, 10);
        Point p5 = new Point (20, 0);
        Wall w0 = new Wall(p0, p1, null);
        Wall w1 = new Wall(p1, p2, null);
        Wall w2 = new Wall(p2, p4, null);
        Wall w3 = new Wall(p4, p5, null);
        Wall w4 = new Wall(p5, p3, null);
        Wall w5 = new Wall(p3, p0, null);
        Line l0 = new Line (p2, p3);
        Line l1 = new Line (p3, p2);        
        Room r0 = new Room();
        Room r1 = new Room();        
        r0.addPoint("p0", p0);
        r0.addPoint("p1", p1);
        r0.addPoint("p2", p2);
        r0.addPoint("p3", p3);
        r0.addLine("w0", w0);
        r0.addLine("w1", w1);
        r0.addLine("w5", w5);
        r0.addLine("l0", l0);
        l0.setPortal(r1);
        
        r1.addPoint("p2", p2);
        r1.addPoint("p3", p3);
        r1.addPoint("p4", p4);
        r1.addPoint("p5", p5);
        r1.addLine("w2", w2);
        r1.addLine("w3", w3);
        r1.addLine("w4", w4);
        r1.addLine("l1", l1);
        l1.setPortal(r0);

        w.addRoom("r0", r0);
        w.addRoom("r1", r1);
        
        Point p = new Point(5, 5);
        Hero h = new Hero(p, w);
        h.setRoom(r0);
        
        h.moveBy(0, 10); // strife right
        assertEquals(15,  h.getPosition().getX(), EPSILON);
        assertEquals(5,  h.getPosition().getY(), EPSILON);
        assertTrue(h.getRoom() == r1);
        
        h.setAsimuth(- Math.PI / 2); // turn left
        h.moveBy(10, 0);
        assertEquals(5,  h.getPosition().getX(), EPSILON);
        assertEquals(5,  h.getPosition().getY(), EPSILON);
        assertTrue(h.getRoom() == r0);

        h.moveBy(0, -1);
        assertEquals(5,  h.getPosition().getX(), EPSILON);
        assertEquals(4,  h.getPosition().getY(), EPSILON);
        assertTrue(h.getRoom() == r0);
        
        h.moveBy(-10, 1);
        assertEquals(15,  h.getPosition().getX(), EPSILON);
        assertEquals(5,  h.getPosition().getY(), EPSILON);
        assertTrue(h.getRoom() == r1);
        
    }

    /**
     * Test of getPosition method, of class Hero.
     */
    @Test
    public void testGetPosition() {
        Point p = new Point(0, 0);
        Hero h = new Hero(p, null);
        assertNotNull(h.getPosition());
        assertTrue(p == h.getPosition());
        assertEquals(0, h.getPosition().getX(), EPSILON);
        assertEquals(0, h.getPosition().getY(), EPSILON);
    }

    @Test
    public void testHero1() {
        assertNotNull(new Hero(new Point(0, 0), null));
    }
    
    @Test (expected = Exception.class)
    public void testHero2() {
        assertNotNull(new Hero(null, null));
    }    
    
}