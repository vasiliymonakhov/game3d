package org.freeware.monakhov.game3d.objects.movable;

import org.freeware.monakhov.game3d.map.Line;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Room;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 * Объект, который может перемещаться
 * @author Vasily Monakhov 
 */
public abstract class MovableObject extends WorldObject {

    public MovableObject(Point position) {
        super(position);
    }
    
    @Override
    public void moveBy(double df, double ds) {
        oldPosition.moveTo(position.getX(), position.getY());
        double deltaX = df * Math.sin(asimuth) + ds * Math.cos(-asimuth);
        double deltaY =  df * Math.cos(asimuth) + ds * Math.sin(-asimuth);       
        position.moveBy(deltaX, deltaY);
        if (room.insideThisRoom(position)) {
            return;
        }
        for (Line l : room.getAllLines()) {
            if (l.checkCross(oldPosition, position)) {
                // пересекли линию
                if (l.isCrossable()) {
                    // и её можно пересекать
                    Room nr = l.getRoomFromPortal();
                    if (nr != null) {
                        // возможно, перешли в другую комнату?
                        if (nr.insideThisRoom(position)) {
                            room = nr;
                            break;                        
                        }
                    }                    
                } else {
                    // линию пересекать нельзя!
                    // вернём юзера назад
                    position.moveTo(oldPosition.getX(), oldPosition.getY());
                }
            }
        }
    }    

}
