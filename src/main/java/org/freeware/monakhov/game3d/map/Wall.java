/**
 * This software is free. You can use it without any limitations, but I don't
 * give any kind of warranties!
 */
package org.freeware.monakhov.game3d.map;

import java.awt.image.BufferedImage;
import org.freeware.monakhov.game3d.SpecialMath;

/**
 * Wall in a map
 *
 * @author Vasily Monakhov
 */
public class Wall extends VisibleLine {

    private final Texture texture;

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
