package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 * Зелёная бочка
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

    @Override
    public void onInteractWith(WorldObject wo) {
    }

    @Override
    public void doSomething(long frameNanoTime) {
    }

    @Override
    public double getInteractRadius() {
        return 25;
    }

}
