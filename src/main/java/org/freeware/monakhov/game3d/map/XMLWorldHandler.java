/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.map;

import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.movable.ViewPoint;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handles XML data of our world
 * @author Vasily Monakhov 
 */
class XMLWorldHandler extends DefaultHandler {
    
    private final World world;
    private final ViewPoint hero;
    
    XMLWorldHandler(World world, ViewPoint hero) {
        this.world = world;
        this.hero = hero;
    }

    private boolean loadingPoints;
    private boolean loadingLines;
    private boolean loadingRooms;
    
    private String roomID;
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attr)  throws SAXException {
        switch(qName) {
            case "root":
                world.setFloor(attr.getValue("floor"));
                world.setCeiling(attr.getValue("ceiling"));
                world.setSky(attr.getValue("sky"));
                break;
            case "points" : 
                loadingPoints = true;
                break;
            case "point" : 
                if (loadingPoints) {
                     world.addPoint(attr.getValue("id"), new Point(attr));
                }
                break;                
            case "lines":
                loadingLines = true;
                break;
            case "line" :
                if (loadingLines) {
                    world.addLine(attr.getValue("id"), new Line(world.getPoint(attr.getValue("start")), world.getPoint(attr.getValue("end")), world));
                } else if (loadingRooms) {
                    String lineID = attr.getValue("id");
                    world.getRoom(roomID).addLine(lineID, world.getLine(lineID));
                }                
                break;
            case "wall" :
                if (loadingLines) {
                    world.addLine(attr.getValue("id"), new Wall(world.getPoint(attr.getValue("start")), world.getPoint(attr.getValue("end")), Texture.get(attr.getValue("texture")), world));
                }                
                break;
            case "door" :
                if (loadingLines) {
                    world.addLine(attr.getValue("id"), new Door(world.getPoint(attr.getValue("start")), world.getPoint(attr.getValue("end")), Texture.get(attr.getValue("texture")), world));
                }
            case "rooms" : 
                loadingRooms = true;
                break;
            case "room" :
                if (loadingRooms) {
                    roomID = attr.getValue("id");
                    world.addRoom(roomID, new Room());
                }
                break;
            case "portal" :
                Line l = world.getLine(attr.getValue("line"));
                l.setPortal(world.getRoom(attr.getValue("from")));
                l.setPortal(world.getRoom(attr.getValue("to")));
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
            case "points" : 
                loadingPoints = false;
                break;
            case "lines":
                loadingLines = false;
                break;
            case "rooms" : 
                loadingRooms = false;
                break;
        }
    }

}
