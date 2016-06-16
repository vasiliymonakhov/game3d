
package org.freeware.monakhov.game3d;

import org.freeware.monakhov.game3d.maps.Point;
import org.freeware.monakhov.game3d.maps.Sprite;


/**
 * This is who are You! :)
 * @author Vasily Monakhov 
 */
public class Hero extends MovableObject {

    /**
     * @param position the position to set
     */
    public Hero(Point position) {
        super(position);
    }

    @Override
    public Sprite getSprite() {
        return null;
    }
    
}
