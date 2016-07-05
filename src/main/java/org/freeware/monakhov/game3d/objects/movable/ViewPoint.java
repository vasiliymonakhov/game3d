package org.freeware.monakhov.game3d.objects.movable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 * Точка, с которой производится наблюдение
 *
 * @author Vasily Monakhov
 */
public class ViewPoint extends MovableObject {
    
    /**
     * Создаёт объект
     * @param world мир
     * @param position мозиция
     */
    public ViewPoint(World world, Point position) {
        super(world, position);
    }

    @Override
    public Sprite getSprite() {
        return null;
    }

    @Override
    public double getRadius() {
        return 48;
    }

    @Override
    public void onInteractWith(WorldObject wo) {
    }

    @Override
    public void doSomething(long frameNanoTime) {
    }

    @Override
    public double getInteractRadius() {
        return 96;
    }

}
