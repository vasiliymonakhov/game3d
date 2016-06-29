/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.map;

import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of XMLWorldLoader
 * @author Vasily Monakhov 
 */
public class XMLWorldLoaderTest {

    final static double EPSILON = 0.0001d;

    @Before
    public void setUp() throws Exception {
        XMLResourceLoader xmlrl = new XMLResourceLoader();
        xmlrl.parse(XMLWorldLoaderTest.class.getResourceAsStream("/org/freeware/monakhov/game3d/resources.xml"));
    }
    
    /**
     * Test of parse method, of class XMLWorldLoader.
     * @throws java.lang.Exception
     */
    @Test
    public void testParse() throws Exception {
        World w = new World();
        XMLWorldLoader wl = new XMLWorldLoader();
        wl.parse(w, null, XMLWorldLoaderTest.class.getResourceAsStream("/org/freeware/monakhov/game3d/maps/testXMLWorld.xml"));
        Room r0 = w.getRoom("r0");
        Point p0 = w.getPoint("p00");
        Point p1 = w.getPoint("p10");
        Point p2 = w.getPoint("p20");
        Point p3 = w.getPoint("p30");
        Line w0 = r0.getLine("w00");
        Line w1 = r0.getLine("w10");
        Line w2 = r0.getLine("l20");
        Line w3 = r0.getLine("w30");
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
        p0 = w.getPoint("p01");
        p1 = w.getPoint("p11");
        p2 = w.getPoint("p21");
        p3 = w.getPoint("p31");
        w0 = r1.getLine("l01");
        w1 = r1.getLine("w11");
        w2 = r1.getLine("w21");
        w3 = r1.getLine("w31");
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
        
        Set<Room> pfr0 = w.getRoom("r0").getLine("l20").getRoomsFromPortal();
        Set<Room> pfr1 = w.getRoom("r1").getLine("l01").getRoomsFromPortal();
        assertTrue(pfr0.contains(r1));
        assertTrue(pfr1.contains(r0));
        
    }

}