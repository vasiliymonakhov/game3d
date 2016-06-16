package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;

/**
 * Йолка
 * @author Vasily Monakhov 
 */
public class Milton extends NonMovableObject {
   
    public Milton(Point position) {
        super(position);
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
