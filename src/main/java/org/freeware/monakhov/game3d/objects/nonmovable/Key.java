package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;

/**
 * Ключ
 * @author Vasily Monakhov 
 */
public class Key extends CrossableObject {

    public Key(World world, Point position) {
        super(world, position);
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
