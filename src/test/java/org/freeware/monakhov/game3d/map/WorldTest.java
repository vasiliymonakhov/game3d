/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.map;

import java.util.Collection;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.nonmovable.GreenBarrel;
import org.freeware.monakhov.game3d.objects.nonmovable.Pole;
import org.freeware.monakhov.game3d.objects.nonmovable.Tree;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test of World
 * @author Vasily Monakhov 
 */
public class WorldTest {

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
     * Test of prepareForVisibilityCheck method, of class World.
     */
    @Test
    public void testPrepareForVisibilityCheck() {
        World w = new World();
        Room r1 = new Room();
        Room r2 = new Room();
        Room r3 = new Room();
        w.addRoom("r1", r1);
        w.addRoom("r2", r2);
        w.addRoom("r3", r3);                
        r1.roomVisibilityAlreadyChecked = true;
        r2.roomVisibilityAlreadyChecked = true;        
        r3.roomVisibilityAlreadyChecked = true;                
        w.prepareForVisibilityCheck();
        assertFalse(r1.isRoomVisibilityAlreadyChecked());
        assertFalse(r2.isRoomVisibilityAlreadyChecked());
        assertFalse(r3.isRoomVisibilityAlreadyChecked());
        assertFalse(r1.roomVisibilityAlreadyChecked);
        assertFalse(r2.roomVisibilityAlreadyChecked);
        assertFalse(r3.roomVisibilityAlreadyChecked);
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
        Pole p = new Pole(w, new Point());
        Collection<WorldObject> res = w.getAllObjects();
        assertTrue(res.contains(t));
        assertTrue(res.contains(b));
        assertFalse(res.contains(p));
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
    
    
}