package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;

/**
 * столб
 * @author Vasily Monakhov 
 */
public class Pole extends CrossableObject {

    public Pole(World world, Point position) {
        super(world, position);
    }

    @Override
    public Sprite getSprite() {
        return Sprite.get("pole01");
    }

    @Override
    public double getRadius() {
        return 10;
    }

}
