package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;

/**
 * Ключ
 * @author Vasily Monakhov 
 */
public class Key extends CrossableObject {

    public Key(Point position) {
        super(position);
    }

    @Override
    public Sprite getSprite() {
        return Sprite.get("key");
    }
    
    @Override
    public boolean isCrossable() {
        return true;
    }    

    @Override
    public double getRadius() {
        return 15;
    }

}
