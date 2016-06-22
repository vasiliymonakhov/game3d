package org.freeware.monakhov.game3d.objects.misc;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.movable.Hero;

/**
 *
 * @author Vasily Monakhov 
 */
public abstract class PlainObject extends WorldObject {
    
    public PlainObject(World world, Point s, Point e) {
        super(world, new Point ((s.getX() + e.getX()) / 2,
                (s.getY() + e.getY()) / 2));
        getLeft().moveTo(s.getX(), s.getY());
        getRight().moveTo(e.getX(), e.getY());
    }

    @Override
    public void turnSpriteToHero(Hero hero) {
    }    

    @Override
    public double getRadius() {
        return 0;
    }    
    
}
