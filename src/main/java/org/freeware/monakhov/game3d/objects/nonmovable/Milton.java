package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;

/**
 * Йолка
 * @author Vasily Monakhov 
 */
public class Milton extends NonMovableObject {

    public Milton(World world, Point position) {
        super(world, position);
    }
   

    @Override
    public Sprite getSprite() {
        return Sprite.get("milton");
    }

    @Override
    public double getRadius() {
        return 72;
    }

}
