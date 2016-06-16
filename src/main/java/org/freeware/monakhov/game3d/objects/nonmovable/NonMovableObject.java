package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 *
 * @author Vasily Monakhov 
 */
abstract class NonMovableObject extends WorldObject {

    public NonMovableObject(Point position) {
        super(position);
    }
    
    @Override
    abstract public Sprite getSprite();

    @Override
    public void moveBy(double df, double ds) {
    }

}
