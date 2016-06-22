
/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.map;

import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.movable.Hero;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handles XML data of our world
 * @author Vasily Monakhov 
 */
class XMLWorldHandler extends DefaultHandler {
    
    private final World world;
    private final Hero hero;
    
    XMLWorldHandler(World world, Hero hero) {
        this.world = world;
        this.hero = hero;
    }

    private boolean loadingRooms;
    
    private String roomID;
    private Room room;
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attr)  throws SAXException {
        switch(qName) {
            case "root":
                world.setFloor(attr.getValue("floor"));
                world.setCeiling(attr.getValue("ceiling"));
                world.setSky(attr.getValue("sky"));
                break;
            case "rooms" : 
                loadingRooms = true;
                break;
            case "room" :
                if (loadingRooms) {
                    roomID = attr.getValue("id");
                    room = new Room();
                    world.addRoom(roomID, room);
                }
                break;
            case "point" : 
                if (loadingRooms) {
                    if (roomID != null) {
                        room.addPoint(attr.getValue("id"), new Point(attr));
                    }
                }
                break;
            case "line" :
                if (loadingRooms) {
                    if (roomID != null) {
                        room.addLine(attr.getValue("id"), new Line(room.getPoint(attr.getValue("start")), room.getPoint(attr.getValue("end"))));
                    }
                }                
                break;
            case "wall" :
                if (loadingRooms) {
                    if (roomID != null) {
                        room.addLine(attr.getValue("id"), new Wall(room.getPoint(attr.getValue("start")), room.getPoint(attr.getValue("end")), 
                        Texture.get(attr.getValue("texture"))));
                    }
                }                
                break;
            case "portal" :
                world.getRoom(attr.getValue("from")).getLine(attr.getValue("line")).setPortal(world.getRoom(attr.getValue("to")));
                break;
            case "hero" :
                hero.getPosition().moveTo(Integer.parseInt(attr.getValue("x")), Integer.parseInt(attr.getValue("y")));
                hero.setRoom(world.getRoom(attr.getValue("room")));
                hero.setAzimuth(Math.PI * 2 * Integer.parseInt(attr.getValue("azimuth")) / 360);
                break;
            case "object":
                world.addObject(attr.getValue("id"), WorldObject.createFromXML(world, attr));
                break;
        }
    }        

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch(qName) {
            case "rooms" : 
                loadingRooms = false;
                break;
            case "room" :
                roomID = null;
                room = null;
                break;
        }
    }

}
