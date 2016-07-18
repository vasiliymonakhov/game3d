package org.freeware.monakhov.game3d.map.visiblelines;

import java.awt.image.BufferedImage;
import org.freeware.monakhov.game3d.SoundSystem;
import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.resources.Texture;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 * Секретная дверь. Эта такая стена, которая один раз открывается
 * @author Vasily Monakhov
 */
public class SecretDoor extends AbstractDoor {

    /**
     * Текстура
     */
    private final Texture texture;

    /**
     * Создаёт дверь
     * @param start точка начала
     * @param end точка конца
     * @param texture текстура
     * @param world мир
     */
    public SecretDoor(Point start, Point end, Texture texture, World world) {
        super(start, end, world);
        this.texture = texture;
    }

    /**
     * Возвращает текстуру
     * @return текстура
     */
    @Override
    public Texture getTexture() {
        return texture;
    }

    /**
     * Возвращает участок текстуры шириной 1 пиксель
     * @param p точка, в которую попал трассирующий луч
     * @param height высота волбца в буфере экрана
     * @return участок текстуры
     */
    @Override
    public BufferedImage getSubImage(Point p, double height) {
        int xOffset = (int) Math.round(SpecialMath.lineLength(start, p) - width * opened);
        return getTexture().getSubImage(xOffset, height);
    }

    /**
     * Скоросто открывания двери
     */
    private static final double OPEN_SPEED = 0.5e-9;

    @Override
    public void doSomething(long frameNanoTime) {
        if (state == OPENING) {
            // дверь открывается
            opened += OPEN_SPEED * frameNanoTime;
            if (opened >= 1) {
                // открылась
                opened = 1;
                state = OPEN;
            }
        }
    }

    @Override
    public void onInteractWith(WorldObject wo1) {
        if (state == CLOSED) {
            // если дверь была закрыта, открываем её
            state = OPENING;
            SoundSystem.play("door");
        }
    }

}
