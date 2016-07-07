package org.freeware.monakhov.game3d.map.visiblelines;

import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Texture;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 * Дверь
 * @author Vasily Monakhov
 */
public class Door extends AbstractDoor {

    /**
     * Текстура открытой двери
     */
    private final Texture openTexture;
    /**
     * Текстура закрытой двери
     */
    private final Texture closedTexture;

    /**
     * Создаёт дверь
     * @param start точка начала
     * @param end точка конца
     * @param openTexture текстура для открытой двери
     * @param closedTexture текстура для закрытой двери
     * @param world мир
     */
    public Door(Point start, Point end, Texture openTexture, Texture closedTexture, World world) {
        super(start, end, world);
        this.openTexture = openTexture;
        this.closedTexture = closedTexture;
    }

    /**
     * Вовзращает тоекстуру
     * @return текстура
     */
    @Override
    public Texture getTexture() {
        if (state == OPEN || state == OPENING) return openTexture;
        return closedTexture;
    }

    /**
     * Время, которое дверь была открыта
     */
    private long openedTime;

    /**
     * Максимальное время для нахождения двери в открытом состоянии
     */
    private static final long MAX_OPENED_TIME = 5000000000l;
    /**
     * Скорость открытия двери
     */
    private static final double OPEN_SPEED = 0.5e-9;

    @Override
    public void doSomething(long frameNanoTime) {
        if (state == OPEN) {
            // если дверь открыта, добавитьв время
            openedTime += frameNanoTime;
            if (openedTime > MAX_OPENED_TIME) {
                // если дверь была открыта слишком долго, то сбросить счётчик
                openedTime = 0;
                // если кто-то стоит в двери, то ничего не делать
                if (somebodyInside()) return;
                // если в двери никого нет, то закрываем
                state = CLOSING;
            }
        } else if (state == CLOSING) {
            // дверь закрывается
            opened -= OPEN_SPEED * frameNanoTime;
            if (opened <= 0) {
                // закрылась
                opened = 0;
                state = CLOSED;
            }
        } else if (state == OPENING) {
            // дверь октрывается
            opened += OPEN_SPEED * frameNanoTime;
            if (opened >= 1) {
                // открылась
                opened = 1;
                state = OPEN;
            }
        }
    }

    /**
     * Проверить, нет ли кого в двери
     * @return true если в двери кто-то стоит
     */
    private boolean somebodyInside() {
        // проверить для главного героя
        if (SpecialMath.lineAndCircleIntersects(start, end, world.getHero().getPosition(), world.getHero().getRadius())) {
            return true;
        }
        // проверить для остальных объектов мира
        for (WorldObject wo : world.getAllObjects()) {
            if (SpecialMath.lineAndCircleIntersects(start, end, wo.getPosition(), wo.getRadius())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onInteractWith(WorldObject wo1) {
        if (state == CLOSED) {
            // если дверь была закрыта, то открываем
            state = OPENING;
        } else if (state == OPEN) {
            // если дверь была закрыта и в ней никого не было, то закрываем
            if (somebodyInside()) return;
            state = CLOSING;
        } else if (state == OPENING) {
            // если дверь открывалась, а кто-то её дёрнул, то пусть закрывается
            state = CLOSING;
        } else if (state == CLOSING) {
            // если дверь закрывалась, а кто-то её дёрнул, то пусть открывается
            state = OPENING;
        }
    }

}
