package org.freeware.monakhov.game3d;

import org.freeware.monakhov.game3d.map.Line;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.movable.Hero;

/**
 *
 * @author Vasily Monakhov
 */
public class GameEngine {

    private final World world;

    private final Hero hero;

    public GameEngine(World world, Hero hero) {
        this.world = world;
        this.hero = hero;
    }

    public void interactWithHero() {
        for (WorldObject wo : world.getAllObjects()) {
            if (SpecialMath.lineLength(wo.getPosition(), hero.getPosition()) < hero.getInteractRadius() + wo.getInteractRadius()) {
                hero.onInteractWith(wo);
                wo.onInteractWith(hero);
            }
        }
        for (Line l : world.getAllLines()) {
            if (SpecialMath.lineAndCircleIntersects(l.getStart(), l.getEnd(), hero.getPosition(), hero.getInteractRadius())) {
                l.onInteractWith(hero);
            }
        }
    }

    public void doCycle(long nanoTime) {
        for (WorldObject wo : world.getAllObjects()) {
            wo.doSomething(nanoTime);
        }
        for (Line l : world.getAllLines()) {
            l.doSomething(nanoTime);
        }
    }

}
