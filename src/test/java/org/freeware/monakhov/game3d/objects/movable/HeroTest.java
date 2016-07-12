/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.objects.movable;

import org.freeware.monakhov.game3d.map.Line;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Room;
import org.freeware.monakhov.game3d.map.visiblelines.Wall;
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
        Hero h = new Hero(new World(), new Point(0, 0));
        assertEquals(0, h.getAzimuth(), EPSILON);
        h.setAzimuth(1);
        assertEquals(1, h.getAzimuth(), EPSILON);
        h.setAzimuth(Math.PI);
        assertEquals(Math.PI, h.getAzimuth(), EPSILON);
        h.setAzimuth(2 * Math.PI + EPSILON / 2);
        assertEquals(0, h.getAzimuth(), EPSILON);
        h.setAzimuth(3 * Math.PI);
        assertEquals(Math.PI, h.getAzimuth(), EPSILON);
    }

    /**
     * Test of getRoom method, of class Hero.
     */
    @Test
    public void testSetAndGetRoom1() {
        Hero h = new Hero(new World(), new Point(0, 0));
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
        Hero h = new Hero(new World(), new Point(0, 0));
        h.setRoom(null);
    }    

    /**
     * Test of moveByWithCheck method, of class Hero.
     */
    @Test
    public void testMoveBy() {
        World w = new World();
        Point p0 = new Point (0, 0);
        Point p1 = new Point (0, 1000);
        Point p2 = new Point (1000, 1000);
        Point p3 = new Point (1000, 0);
        Point p4 = new Point (2000, 1000);
        Point p5 = new Point (2000, 0);
        Wall w0 = new Wall(p0, p1, null, w);
        Wall w1 = new Wall(p1, p2, null, w);
        Wall w2 = new Wall(p2, p4, null, w);
        Wall w3 = new Wall(p4, p5, null, w);
        Wall w4 = new Wall(p5, p3, null, w);
        Wall w5 = new Wall(p3, p0, null, w);
        Line l0 = new Line (p2, p3, w);
        Line l1 = new Line (p2, p3, w);        
        Room r0 = new Room();
        Room r1 = new Room();        
        w.addPoint("p01", p0);
        w.addPoint("p11", p1);
        w.addPoint("p21", p2);
        w.addPoint("p31", p3);
        r0.addLine("w01", w0);
        r0.addLine("w11", w1);
        r0.addLine("w51", w5);
        r0.addLine("l01", l0);
        l0.setPortal(r1);
        
        w.addPoint("p22", p2);
        w.addPoint("p32", p3);
        w.addPoint("p43", p4);
        w.addPoint("p54", p5);
        r1.addLine("w21", w2);
        r1.addLine("w31", w3);
        r1.addLine("w41", w4);
        r1.addLine("l1", l1);
        l1.setPortal(r0);

        w.addRoom("r0", r0);
        w.addRoom("r1", r1);
        
        Point p = new Point(500, 500);
        Hero h = new Hero(w, p);
        h.setRoom(r0);
        
        h.moveByWithCheck(0, 1000); // strife right
        assertEquals(1500,  h.getPosition().getX(), EPSILON);
        assertEquals(500,  h.getPosition().getY(), EPSILON);
        assertTrue(h.getRoom() == r1);
        
        h.setAzimuth(- Math.PI / 2); // turn left
        h.moveByWithCheck(1000, 0);
        assertEquals(500,  h.getPosition().getX(), EPSILON);
        assertEquals(500,  h.getPosition().getY(), EPSILON);
        assertTrue(h.getRoom() == r0);

        h.moveByWithCheck(0, -100);
        assertEquals(500,  h.getPosition().getX(), EPSILON);
        assertEquals(400,  h.getPosition().getY(), EPSILON);
        assertTrue(h.getRoom() == r0);
        
        h.moveByWithCheck(-1000, 100);
        assertEquals(1500,  h.getPosition().getX(), EPSILON);
        assertEquals(500,  h.getPosition().getY(), EPSILON);
        assertTrue(h.getRoom() == r1);
        
    }

    /**
     * Test of getPosition method, of class Hero.
     */
    @Test
    public void testGetPosition() {
        Point p = new Point(0, 0);
        Hero h = new Hero(new World(), p);
        assertNotNull(h.getPosition());
        assertTrue(p == h.getPosition());
        assertEquals(0, h.getPosition().getX(), EPSILON);
        assertEquals(0, h.getPosition().getY(), EPSILON);
    }

    @Test
    public void testHero1() {
        assertNotNull(new Hero(new World(), new Point(0, 0)));
    }
    
    @Test (expected = Exception.class)
    public void testHero2() {
        assertNotNull(new Hero(new World(), null));
    }    
    
}