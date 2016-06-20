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

    public Hero(Point position, World world) {
        super(position, world);
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
