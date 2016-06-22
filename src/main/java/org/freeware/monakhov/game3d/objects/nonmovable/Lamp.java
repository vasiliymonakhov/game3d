package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;

/**
 * Лампа
 * @author Vasily Monakhov 
 */
public class Lamp extends CrossableObject {

    public Lamp(World world, Point position) {
        super(world, position);
    }

    @Override
    public Sprite getSprite() {
        return Sprite.get("lamp");
    }

    @Override
    public double getRadius() {
        return 10;
    }

}
