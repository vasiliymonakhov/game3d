/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d;

import org.freeware.monakhov.game3d.maps.Line;
import org.freeware.monakhov.game3d.maps.Point;
import org.freeware.monakhov.game3d.maps.Room;


/**
 * This is who are You! :)
 * @author Vasily Monakhov 
 */
public class Hero {

    private final Point position;
    private final Point oldPosition;
    
    private double asimuth;
    
    private Room room;

    /**
     * @param position the position to set
     */
    public Hero(Point position) {
        if (position == null) {
            throw new IllegalArgumentException("Position may not be null");
        }        
        this.position = position;
        oldPosition = new Point();
    }

    /**
     * @return the asimuth
     */
    public double getAsimuth() {
        return asimuth;
    }

    /**
     * @param asimuth the asimuth to set
     */
    public void setAsimuth(double asimuth) {
        this.asimuth = asimuth % (2 * Math.PI);
    }

    /**
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * @param room the room to set
     */
    public void setRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room may not be null");
        }
        this.room = room;
    }

    public void moveBy(double df, double ds) {
        oldPosition.moveTo(position.getX(), position.getY());
        double deltaX = df * Math.sin(asimuth) + ds * Math.cos(-asimuth);
        double deltaY =  df * Math.cos(asimuth) + ds * Math.sin(-asimuth);       
        position.moveBy(deltaX, deltaY);
        if (room.insideThisRoom(position)) {
            return;
        }
        for (Line l : room.getAllLines()) {
            Room nr = l.getRoomFromPortal();
            if (nr != null) {
                // возможно, перешли в другую комнату?
                if (l.checkCross(oldPosition, position)) {
                    if (nr.insideThisRoom(position)) {
                        room = nr;
                        break;                        
                    }
                }
            }
        }
    }

    /**
     * @return the position
     */
    public Point getPosition() {
        return position;
    }
    
}
