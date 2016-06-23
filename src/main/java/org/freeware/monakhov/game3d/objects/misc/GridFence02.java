package org.freeware.monakhov.game3d.objects.misc;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;

/**
 *
 * @author Vasily Monakhov 
 */
public class GridFence02 extends PlainObject {

    private final static Sprite SPRITE = Sprite.get("grid_fence02");
    
    public GridFence02(World world, Point start, Point end) {
        super(world, start, end);
    }

    @Override
    public Sprite getSprite() {
        return SPRITE;
    }

}
