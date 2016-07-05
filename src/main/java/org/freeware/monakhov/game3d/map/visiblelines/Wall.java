package org.freeware.monakhov.game3d.map.visiblelines;

import java.awt.image.BufferedImage;
import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Texture;
import org.freeware.monakhov.game3d.map.World;

/**
 * Стена
 *
 * @author Vasily Monakhov
 */
public class Wall extends VisibleLine {

    /**
     * Текстура
     */
    private final Texture texture;

    /**
     * Создайт стену
     * @param start точка начала
     * @param end точка конца
     * @param texture текстура
     * @param world мир
     */
    public Wall(Point start, Point end, Texture texture, World world) {
        super(start, end, world);
        this.texture = texture;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public boolean isVisible() {
        // стены прозрачными не бывают
        return true;
    }

    @Override
    public boolean pointIsVisible(Point p) {
        return true;
    }

    /**
     * Сообщает, что линию можно пересекать
     *
     * @return можно ли пересекать через линию
     */
    @Override
    public boolean isCrossable() {
        return false;
    }

    @Override
    public BufferedImage getSubImage(Point p, double height) {
        int xOffset = (int) Math.round(SpecialMath.lineLength(start, p));
        return getTexture().getSubImage(xOffset, height);
    }

}
