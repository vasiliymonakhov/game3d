package org.freeware.monakhov.game3d;

import org.freeware.monakhov.game3d.maps.Line;
import org.freeware.monakhov.game3d.maps.Point;
import org.freeware.monakhov.game3d.maps.Room;

/**
 * Объект, который может перемещаться
 * @author Vasily Monakhov 
 */
public abstract class MovableObject extends WorldObject {

    public MovableObject(Point position) {
        super(position);
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

}
