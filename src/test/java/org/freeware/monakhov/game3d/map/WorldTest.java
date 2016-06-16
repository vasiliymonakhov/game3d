/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.map;

import org.freeware.monakhov.game3d.map.Room;
import org.freeware.monakhov.game3d.map.World;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test of World
 * @author Vasily Monakhov 
 */
public class WorldTest {

    public WorldTest() {
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
    
}