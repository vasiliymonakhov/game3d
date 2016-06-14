/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.maps;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handles XML data of our world
 * @author Vasily Monakhov 
 */
class XMLWorldHandler extends DefaultHandler {
    
    private final World world;
    private final TextureManager textureManager;
    
    XMLWorldHandler(World world, TextureManager textureManager) {
        this.world = world;
        this.textureManager = textureManager;
    }

    private boolean loadingRooms;
    
    private String roomID;
    private Room room;
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attr)  throws SAXException {
        switch(qName) {
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
                        textureManager.get(attr.getValue("texture"))));
                    }
                }                
                break;
            case "portal" :
                world.getRoom(attr.getValue("from")).getLine(attr.getValue("line")).setPortal(world.getRoom(attr.getValue("to")));
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
