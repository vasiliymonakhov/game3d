package org.freeware.monakhov.game3d;

import org.freeware.monakhov.game3d.maps.Point;
import org.freeware.monakhov.game3d.maps.Sprite;

/**
 *
 * @author Vasily Monakhov 
 */
public class StaticObject extends WorldObject {

    private final Sprite sprite;

    public StaticObject(Point position, String spriteName) {
        super(position);
        sprite = Sprite.getSprite(spriteName);
    }
    
    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void moveBy(double df, double ds) {
    }

}
