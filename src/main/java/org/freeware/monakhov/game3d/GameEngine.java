package org.freeware.monakhov.game3d;

import org.freeware.monakhov.game3d.map.Line;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.movable.Hero;

/**
 * Логика игрового цикла
 * @author Vasily Monakhov
 */
public class GameEngine {

    /**
     * ссылка на текущий мир
     */
    private final World world;

   /**
     * Создаёт логику
     * @param world мир
     */
    public GameEngine(World world) {
        this.world = world;
    }

    /**
     * Отработка взаимодействия главного героя с предметами мира
     */
    public void interactWithHero() {
        // проверить взаимодействие с объектами мира
        for (WorldObject wo : world.getAllObjects()) {
            if (SpecialMath.lineLength(wo.getPosition(), world.getHero().getPosition()) < world.getHero().getInteractRadius() + wo.getInteractRadius()) {
                world.getHero().onInteractWith(wo);
                wo.onInteractWith(world.getHero());
            }
        }
        // проверить взаимодействие с линиями на карте мира
        for (Line l : world.getAllLines()) {
            if (SpecialMath.lineAndCircleIntersects(l.getStart(), l.getEnd(), world.getHero().getPosition(), world.getHero().getInteractRadius())) {
                l.onInteractWith(world.getHero());
            }
        }
    }

    /**
     * Выполнение одного такта 
     * @param nanoTime текущее системное время
     */
    public void doCycle(long nanoTime) {
        for (WorldObject wo : world.getAllObjects()) {
            wo.doSomething(nanoTime);
        }
        for (Line l : world.getAllLines()) {
            l.doSomething(nanoTime);
        }
    }

}
