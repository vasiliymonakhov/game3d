package org.freeware.monakhov.game3d.objects.movable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;

/**
 * This is who are You! :)
 *
 * @author Vasily Monakhov
 */
public class Hero extends MovableObject {

    public Hero(World world, Point position) {
        super(world, position);
    }

    @Override
    public Sprite getSprite() {
        return null;
    }

    @Override
    public double getRadius() {
        return 64;
    }

}
