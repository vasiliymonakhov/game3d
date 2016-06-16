package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;

/**
 * Бочка
 * @author Vasily Monakhov 
 */
public class Barrel extends NonMovableObject {

    public Barrel(Point position) {
        super(position);
    }

    @Override
    public Sprite getSprite() {
        return Sprite.get("green_barrel");
    }

    @Override
    public double getRadius() {
        return 40;
    }

}
