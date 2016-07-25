package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.resources.Sprite;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 *
 * @author Vasily Monakhov
 */
public class PistolAmmo extends Ammo {

    public PistolAmmo(World world, Point position) {
        super(world, position);
    }

    @Override
    public Sprite getSprite() {
        return Sprite.get("pistol_ammo");
    }

    @Override
    public double getRadius() {
        return 32;
    }

    @Override
    public double getInteractRadius() {
        return 64;
    }

    @Override
    public void onInteractWith(WorldObject wo) {
    }

    @Override
    public void onCollapseWith(WorldObject wo) {
    }

    @Override
    public void doSomething(long frameNanoTime) {
    }

    @Override
    public void onCycleEnd() {
    }

    @Override
    public int getAmmo() {
        return 10;
    }

}
