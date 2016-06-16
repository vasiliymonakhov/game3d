/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.maps;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.freeware.monakhov.game3d.WorldObject;

/**
 * This is a map of our world
 * @author Vasily Monakhov 
 */
public class World {
    
    /**
     * Rooms in our map
     */
    private final Map<String, Room> rooms = new HashMap<>();
    
    public void addRoom(String id, Room r) {
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
        if (objects.containsKey(id)) {
            throw new IllegalArgumentException("Object " + id + " already exists"); 
        }
        objects.put(id, o);
    }    
    
}
