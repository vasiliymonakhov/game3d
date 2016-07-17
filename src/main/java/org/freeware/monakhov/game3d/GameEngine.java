package org.freeware.monakhov.game3d;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import org.freeware.monakhov.game3d.map.Line;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.movable.Hero;

/**
 * Логика игрового цикла
 *
 * @author Vasily Monakhov
 */
public class GameEngine {

    /**
     * ссылка на текущий мир
     */
    private final World world;

    private final Semaphore semaphore;

    /**
     * Создаёт логику
     *
     * @param world мир
     */
    public GameEngine(World world, Semaphore semaphore) {
        this.world = world;
        this.semaphore = semaphore;
    }

    /**
     * Отработка взаимодействия главного героя с предметами мира
     */
    public void heroInteractWithWorld() {
        // проверить взаимодействие с объектами мира
        for (WorldObject wo : world.getAllObjects()) {
            if (SpecialMath.lineLength(wo.getPosition(), world.getHero().getPosition()) < world.getHero().getInteractRadius() + wo.getInteractRadius()) {
                world.getHero().onInteractWith(wo);
            }
        }
        // проверить взаимодействие с линиями на карте мира
        for (Line l : world.getAllLines()) {
            if (SpecialMath.lineAndCircleIntersects(l.getStart(), l.getEnd(), world.getHero().getPosition(), world.getHero().getInteractRadius())) {
                l.onInteractWith(world.getHero());
            }
        }
    }

    private void worldInteractWithHero() {
        // проверить взаимодействие с объектами мира
        for (WorldObject wo : world.getAllObjects()) {
            if (SpecialMath.lineLength(wo.getPosition(), world.getHero().getPosition()) < world.getHero().getInteractRadius() + wo.getInteractRadius()) {
                wo.onInteractWith(world.getHero());
                world.getHero().onInteractWith(wo);
            }
        }
    }

    private void worldCollapsesWithHero() {
        // проверить взаимодействие с объектами мира
        for (WorldObject wo : world.getAllObjects()) {
            if (wo.isCrossable()) continue;
            if (SpecialMath.lineLength(wo.getPosition(), world.getHero().getPosition()) < world.getHero().getRadius() + wo.getRadius()) {
                wo.onCollapseWith(world.getHero());
                world.getHero().onCollapseWith(wo);
            }
        }
    }

    ArrayList<WorldObject> wobjects = new ArrayList<>();

    private void objectsInteractWithObjects() {
        wobjects.clear();
        wobjects.addAll(world.getAllObjects());
        if (wobjects.size() < 2) {
            return;
        }
        for (int i = 0; i < wobjects.size() - 1; i++) {
            WorldObject wo1 = wobjects.get(i);
            for (int j = i + 1; j < wobjects.size(); j++) {
                WorldObject wo2 = wobjects.get(j);
                if (SpecialMath.lineLength(wo1.getPosition(), wo2.getPosition()) < wo1.getInteractRadius() + wo2.getInteractRadius()) {
                    wo1.onInteractWith(wo2);
                    wo2.onInteractWith(wo1);
                }
            }
        }
    }

    private void objectsCollapsWithObjects() {
        wobjects.clear();
        wobjects.addAll(world.getAllObjects());
        if (wobjects.size() < 2) {
            return;
        }
        for (int i = 0; i < wobjects.size() - 1; i++) {
            WorldObject wo1 = wobjects.get(i);
            if (wo1.isCrossable()) continue;
            for (int j = i + 1; j < wobjects.size(); j++) {
                WorldObject wo2 = wobjects.get(j);
                if (wo2.isCrossable()) continue;
                if (SpecialMath.lineLength(wo1.getPosition(), wo2.getPosition()) < wo1.getRadius() + wo2.getRadius()) {
                    wo1.onCollapseWith(wo2);
                    wo2.onCollapseWith(wo1);
                }
            }
        }
    }

    private void worldCycleEnd() {
        for (WorldObject wo : world.getAllObjects()) {
            wo.onCycleEnd();
        }
    }

    /**
     * Выполнение одного такта несколькими частями
     *
     * @param nanoTime текущее системное время
     * @param ked диспетчер событий от клавиатуры
     * @throws java.lang.InterruptedException
     */
    public void doCycle(long nanoTime, KeyDispatcher ked) throws InterruptedException {
        semaphore.acquire();
        Hero h = world.getHero();
        if (h.getHealth() > 0) {
            h.analyseKeys(ked.isLeft(), ked.isRight(), ked.isForward(), ked.isBackward(), ked.isStrafeLeft(), ked.isStrafeRight(), nanoTime);
            if (ked.isInteract()) {
                heroInteractWithWorld();
                ked.setInteract(false);
            }
            h.changeWeapon(ked.isWeapon0(), ked.isWeapon1(), ked.isWeapon2(), ked.isWeapon3(), ked.isWeapon4());
            h.fire(ked.isFirePressed());
        } else {
            h.analyseKeys(ked.isLeft(), ked.isRight(), false, false, false, false, nanoTime);
        }
        h.doSomething(nanoTime);
        for (WorldObject wo : world.getAllObjects()) {
            wo.doSomething(nanoTime);
        }
        for (Line l : world.getAllLines()) {
            l.doSomething(nanoTime);
        }
        worldInteractWithHero();
        objectsInteractWithObjects();
        worldCollapsesWithHero();
        objectsCollapsWithObjects();
        worldCycleEnd();
        world.updateObjects();
        semaphore.release();
    }

}
