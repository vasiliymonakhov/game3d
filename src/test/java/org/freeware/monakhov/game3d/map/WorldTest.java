/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.map;

import java.util.Collection;
import static org.freeware.monakhov.game3d.map.RoomTest.EPSILON;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.nonmovable.GreenBarrel;
import org.freeware.monakhov.game3d.objects.nonmovable.Tree;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test of World
 * @author Vasily Monakhov
 */
public class WorldTest {

    /**
     * Test of addPoint method, of class Room.
     */
    @Test
    public void testAddAndGetPoint1() {
        World w = new World();
        w.addPoint("p0", new Point(10, 20));
        w.addPoint("p1", new Point(30, 40));
        w.addPoint("p2", new Point(50, 60));

        Point p = w.getPoint("p0");
        assertEquals(10, p.getX(), EPSILON);
        assertEquals(20, p.getY(), EPSILON);
        p = w.getPoint("p1");
        assertEquals(30, p.getX(), EPSILON);
        assertEquals(40, p.getY(), EPSILON);
        p = w.getPoint("p2");
        assertEquals(50, p.getX(), EPSILON);
        assertEquals(60, p.getY(), EPSILON);
        p = w.getPoint("p3");
        assertNull(p);
    }
    /**
     * Test of addPoint method, of class Room.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddPoint() {
        World w = new World();
        w.addPoint("p0", new Point(10, 20));
        w.addPoint("p0", new Point(30, 40));
    }

    /**
     * Test of addPoint method, of class Room.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddPoint2() {
        World w = new World();
        w.addPoint("", new Point(10, 20));
    }

    /**
     * Test of addPoint method, of class Room.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddPoint3() {
        World w = new World();
        w.addPoint(null, new Point(10, 20));
    }

    /**
     * Test of addPoint method, of class Room.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddPoint4() {
        World w = new World();
        w.addPoint("p0", null);
    }

    /**
     * Test of addRoom method, of class World.
     */
    @Test
    public void testAddAndGetRoom1() {
        World w = new World();
        Room r1 = new Room();
        Room r2 = new Room();
        Room r3 = new Room();
        w.addRoom("r1", r1);
        w.addRoom("r2", r2);
        w.addRoom("r3", r3);
        assertTrue(r1 == w.getRoom("r1"));
        assertTrue(r2 == w.getRoom("r2"));
        assertTrue(r3 == w.getRoom("r3"));
    }

    /**
     * Test of addRoom method, of class World.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddAndGetRoom2() {
        World w = new World();
        Room r1 = new Room();
        Room r2 = new Room();
        w.addRoom("r1", r1);
        w.addRoom("r1", r2);
    }

    /**
     * Test of addRoom method, of class World.
     */
    @Test
    public void testAddRoom() {
        World w = new World();
        Room r1 = new Room();
        Room r2 = new Room();
        Room r3 = new Room();
        w.addRoom("r1", r1);
        w.addRoom("r2", r2);
        w.addRoom("r3", r3);
        assertTrue(r1 == w.getRoom("r1"));
        assertTrue(r2 == w.getRoom("r2"));
        assertTrue(r3 == w.getRoom("r3"));
    }

    /**
     * Test of getRoom method, of class World.
     */
    @Test
    public void testGetRoom() {
        World w = new World();
        Room r1 = new Room();
        Room r2 = new Room();
        Room r3 = new Room();
        w.addRoom("r1", r1);
        w.addRoom("r2", r2);
        w.addRoom("r3", r3);
        assertTrue(r1 == w.getRoom("r1"));
        assertTrue(r2 == w.getRoom("r2"));
        assertTrue(r3 == w.getRoom("r3"));
    }

    /**
     * Test of getAllRooms method, of class World.
     */
    @Test
    public void testGetAllRooms() {
        World w = new World();
        Room r1 = new Room();
        Room r2 = new Room();
        Room r3 = new Room();
        Room r4 = new Room();
        w.addRoom("r1", r1);
        w.addRoom("r2", r2);
        w.addRoom("r3", r3);
        assertTrue(r1 == w.getRoom("r1"));
        assertTrue(r2 == w.getRoom("r2"));
        assertTrue(r3 == w.getRoom("r3"));
        Collection<Room> expResult = w.getAllRooms();
        assertTrue(expResult.contains(r1));
        assertTrue(expResult.contains(r2));
        assertTrue(expResult.contains(r3));
        assertFalse(expResult.contains(r4));
    }

    /**
     * Test of getAllObjects method, of class World.
     */
    @Test
    public void testGetAllObjects() {
        World w = new World();
        GreenBarrel b = new GreenBarrel(w, new Point());
        Tree t = new Tree(w, new Point());
        w.addObject("1", b);
        w.addObject("2", t);
        Collection<WorldObject> res = w.getAllObjects();
        assertTrue(res.contains(t));
        assertTrue(res.contains(b));
    }

    /**
     * Test of addObject method, of class World.
     */
    @Test
    public void testAddObject1() {
        World w = new World();
        GreenBarrel b = new GreenBarrel(w, new Point());
        Tree t = new Tree(w, new Point());
        w.addObject("1", b);
        w.addObject("2", t);
    }

    /**
     * Test of addObject method, of class World.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddObject2() {
        World w = new World();
        GreenBarrel b = new GreenBarrel(w, new Point());
        w.addObject("", b);
    }

    /**
     * Test of addObject method, of class World.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddObject3() {
        World w = new World();
        GreenBarrel b = new GreenBarrel(w, new Point());
        w.addObject(null, b);
    }

    /**
     * Test of addObject method, of class World.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddObject4() {
        World w = new World();
        w.addObject("abarrel", null);
    }

    /**
     * Test of getPoint method, of class Room.
     */
    @Test
    public void testGetPoint() {
        World w = new World();
        w.addPoint("p0", new Point(10, 20));
        Point p = w.getPoint("p0");
        assertEquals(10, p.getX(), EPSILON);
        assertEquals(20, p.getY(), EPSILON);
    }

    /**
     * Test of getAllPoints method, of class Room.
     */
    @Test
    public void testGetAllPoints() {
        World w = new World();
        Point p0 = new Point(10, 20);
        Point p1 = new Point(30, 40);
        Point p2 = new Point(50, 60);
        w.addPoint("p0", p0);
        w.addPoint("p1", p1);
        w.addPoint("p2", p2);
        Collection<Point> points = w.getAllPoints();
        assertTrue(points.contains(p0));
        assertTrue(points.contains(p1));
        assertTrue(points.contains(p2));
    }


}