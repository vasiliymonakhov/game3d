package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;

/**
 * Лампа
 * @author Vasily Monakhov 
 */
public class Lamp extends CrossableObject {

    public Lamp(Point position) {
        super(position);
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
