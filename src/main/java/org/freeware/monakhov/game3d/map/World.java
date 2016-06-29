/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.movable.Hero;

/**
 * This is a map of our world
 * @author Vasily Monakhov 
 */
public class World {
    
    private String floor;
    private String ceiling;
    private String sky;
    private Hero hero;
    
    public void setHero (Hero hero) {
        this.hero = hero;
    }
    
    public Hero getHero() {
        return hero;
    }
    
    /**
     * Points in a map
     */
    private final Map<String, Point> points = new LinkedHashMap<>();    
    
    public void addPoint(String id, Point p) {
         if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Point id must be not null or empty");             
        }        
        if (p == null) {
            throw new IllegalArgumentException("Point must be not null");             
        }         
        if (points.containsKey(id)) {
            throw new IllegalArgumentException("Point " + id + " already exists"); 
        }
        points.put(id, p);
    }
    
    public Point getPoint(String id) {
        return points.get(id);
    }
    
    public Collection<Point>  getAllPoints() {
        return points.values();
    }    
    
    /**
     * Walls of this room
     */
    private final Map<String, Line> lines = new LinkedHashMap<>();
    
    public void addLine(String id, Line w) {
         if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Line id must be not null or empty");             
        }        
        if (w == null) {
            throw new IllegalArgumentException("Line must be not null");             
        } 
        if (lines.containsKey(id)) {
            throw new IllegalArgumentException("Line " + id + " already exists"); 
        }
        lines.put(id, w);
    }    
    
    public Line getLine(String id) {
        return lines.get(id);
    }    

    public Collection<Line> getAllLines() {
        return lines.values();
    }    
    
    /**
     * Rooms in our map
     */
    private final Map<String, Room> rooms = new HashMap<>();
    
    public void addRoom(String id, Room r) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Room id must be not null or empty");             
        }        
        if (r == null) {
            throw new IllegalArgumentException("Room must be not null");             
        }               
        if (rooms.containsKey(id)) {
            throw new IllegalArgumentException("Room " + id + " already exists"); 
        }        
        rooms.put(id, r);
    }
    
    public Room getRoom(String id) {
        return rooms.get(id);
    }
    
    public Collection<Room> getAllRooms() {
        return rooms.values();
    }
    
    public void prepareForVisibilityCheck() {
        for (Room r : rooms.values()) {
            r.clearRoomVisibilityAlreadyChecked();
        }
    }
    
    private final Map<String, WorldObject> objects = new HashMap<>();
    
    public Collection<WorldObject> getAllObjects() {
        return objects.values();
    }
    
    public void addObject(String id, WorldObject o) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Object id must be not null or empty");             
        }
        if (o == null) {
            throw new IllegalArgumentException("Object must be not null");             
        }        
        if (objects.containsKey(id)) {
            throw new IllegalArgumentException("Object " + id + " already exists"); 
        }
        objects.put(id, o);
    }    
    
    public WorldObject getObject(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Object id must be not null or empty");             
        }        
        if (!objects.containsKey(id)) {
            throw new IllegalArgumentException("Object " + id + " not exists"); 
        }        
        return objects.get(id);
    }

    /**
     * @return the floor
     */
    public String getFloor() {
        return floor;
    }

    /**
     * @param floor the floorCeiling to set
     */
    public void setFloor(String floor) {
        this.floor = floor;
    }

    /**
     * @return the ceiling
     */
    public String getCeiling() {
        return ceiling;
    }

    /**
     * @param ceiling the ceiling to set
     */
    public void setCeiling(String ceiling) {
        this.ceiling = ceiling;
    }

    /**
     * @return the sky
     */
    public String getSky() {
        return sky;
    }

    /**
     * @param sky the sky to set
     */
    public void setSky(String sky) {
        this.sky = sky;
    }
    
}
