/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.map;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test of XMLWorldLoader
 * @author Vasily Monakhov 
 */
public class XMLWorldLoaderTest {

    final static double EPSILON = 0.0001d;
    
    /**
     * Test of parse method, of class XMLWorldLoader.
     * @throws java.lang.Exception
     */
    @Test
    public void testParse() throws Exception {
        World w = new World();
        XMLWorldLoader wl = new XMLWorldLoader();
        wl.parse(w, XMLWorldLoaderTest.class.getResourceAsStream("/org/freeware/monakhov/game3d/maps/testXMLWorld.xml"));
        Room r0 = w.getRoom("r0");
        Point p0 = r0.getPoint("p0");
        Point p1 = r0.getPoint("p1");
        Point p2 = r0.getPoint("p2");
        Point p3 = r0.getPoint("p3");
        Line w0 = r0.getLine("w0");
        Line w1 = r0.getLine("w1");
        Line w2 = r0.getLine("l2");
        Line w3 = r0.getLine("w3");
        assertEquals(-1, p0.getX(), EPSILON);
        assertEquals(-1, p0.getY(), EPSILON);
        assertEquals(-1, p1.getX(), EPSILON);
        assertEquals(1, p1.getY(), EPSILON);
        assertEquals(1, p2.getX(), EPSILON);
        assertEquals(1, p2.getY(), EPSILON);
        assertEquals(1, p3.getX(), EPSILON);
        assertEquals(-1, p3.getY(), EPSILON);        
        assertTrue(w0.getStart() == p0);
        assertTrue(w0.getEnd() == p1);
        assertTrue(w1.getStart() == p1);
        assertTrue(w1.getEnd() == p2);
        assertTrue(w2.getStart() == p2);
        assertTrue(w2.getEnd() == p3);
        assertTrue(w3.getStart() == p3);
        assertTrue(w3.getEnd() == p0);
        
        Room r1 = w.getRoom("r1");
        p0 = r1.getPoint("p0");
        p1 = r1.getPoint("p1");
        p2 = r1.getPoint("p2");
        p3 = r1.getPoint("p3");
        w0 = r1.getLine("l0");
        w1 = r1.getLine("w1");
        w2 = r1.getLine("w2");
        w3 = r1.getLine("w3");
        assertEquals(1, p0.getX(), EPSILON);
        assertEquals(-1, p0.getY(), EPSILON);
        assertEquals(1, p1.getX(), EPSILON);
        assertEquals(1, p1.getY(), EPSILON);
        assertEquals(3, p2.getX(), EPSILON);
        assertEquals(1, p2.getY(), EPSILON);
        assertEquals(3, p3.getX(), EPSILON);
        assertEquals(-1, p3.getY(), EPSILON);        
        assertTrue(w0.getStart() == p0);
        assertTrue(w0.getEnd() == p1);
        assertTrue(w1.getStart() == p1);
        assertTrue(w1.getEnd() == p2);
        assertTrue(w2.getStart() == p2);
        assertTrue(w2.getEnd() == p3);
        assertTrue(w3.getStart() == p3);
        assertTrue(w3.getEnd() == p0);
        
        Room pfr0 = w.getRoom("r0").getLine("l2").getRoomFromPortal();
        Room pfr1 = w.getRoom("r1").getLine("l0").getRoomFromPortal();
        assertTrue(pfr0 == r1);
        assertTrue(pfr1 == r0);
        
    }

}