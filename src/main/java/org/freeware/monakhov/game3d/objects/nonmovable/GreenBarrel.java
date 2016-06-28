package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;

/**
 * Бочка
 * @author Vasily Monakhov 
 */
public class GreenBarrel extends NonMovableObject {

    public GreenBarrel(World world, Point position) {
        super(world, position);
    }


    @Override
    public Sprite getSprite() {
        return Sprite.get("green_barrel");
    }

    @Override
    public double getRadius() {
        return 25;
    }

}
