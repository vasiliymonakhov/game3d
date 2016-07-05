package org.freeware.monakhov.game3d.map;

import org.freeware.monakhov.game3d.map.visiblelines.DoorOpenSwitch;
import org.freeware.monakhov.game3d.map.visiblelines.SecretDoor;
import org.freeware.monakhov.game3d.map.visiblelines.SimpleSwitch;
import org.freeware.monakhov.game3d.map.visiblelines.Door;
import org.freeware.monakhov.game3d.map.visiblelines.Wall;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Загружает мир из XML
 * @author Vasily Monakhov 
 */
class XMLWorldHandler extends DefaultHandler {
    
    /**
     * Мир
     */
    private final World world;
    
    /**
     * Содаёт загрузчик
     * @param world мир
     */
    XMLWorldHandler(World world) {
        this.world = world;
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
                    world.addLine(attr.getValue("id"), new Door(world.getPoint(attr.getValue("start")), world.getPoint(attr.getValue("end")), Texture.get(attr.getValue("open_texture")), Texture.get(attr.getValue("closed_texture")), world));
                    break;
                }
            case "secret_door" :
                if (loadingLines) {
                    world.addLine(attr.getValue("id"), new SecretDoor(world.getPoint(attr.getValue("start")), world.getPoint(attr.getValue("end")), Texture.get(attr.getValue("texture")), world));
                    break;
                }            
            case "door_open_switch" :
                if (loadingLines) {
                    world.addLine(attr.getValue("id"), new DoorOpenSwitch(world.getPoint(attr.getValue("start")), world.getPoint(attr.getValue("end")), Texture.get(attr.getValue("on_texture")), Texture.get(attr.getValue("off_texture")), world.getLine(attr.getValue("door")), world));
                    break;
                }            
            case "simple_switch" :
                if (loadingLines) {
                    world.addLine(attr.getValue("id"), new SimpleSwitch(world.getPoint(attr.getValue("start")), world.getPoint(attr.getValue("end")), Texture.get(attr.getValue("on_texture")), Texture.get(attr.getValue("off_texture")), world));
                    break;
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
                world.getHero().getPosition().moveTo(Integer.parseInt(attr.getValue("x")), Integer.parseInt(attr.getValue("y")));
                world.getHero().setAzimuth(Math.PI * 2 * Integer.parseInt(attr.getValue("azimuth")) / 360);
                world.getHero().updateRoom();
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
