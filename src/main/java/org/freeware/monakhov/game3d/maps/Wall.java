/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.maps;

import java.awt.image.BufferedImage;
import org.freeware.monakhov.game3d.SpecialMath;

/**
 * Wall in a map
 * @author Vasily Monakhov 
 */
public class Wall extends Line {

    private final Texture texture;
    
    public Wall(Point start, Point end, Texture texture) {
        super(start, end);
        this.texture = texture;
    }
    
    @Override
    public boolean isVisible() {
        return true;
    }

    /**
     * Проверяет видимость линии на экране
     */
    @Override
    boolean checkVisibility(Line[] mapLines, Point viewPoint, Point[] rayPoints, Point[] intersectPoints) {
        Point p = new Point();
        boolean flag = false;
        for (int i = 0; i < mapLines.length; i++) {
            if (SpecialMath.lineIntersection(start, end, rayPoints[i], viewPoint, p)) {
                if (p.between(start, end) && p.between(rayPoints[i], viewPoint)) {
                    if (mapLines[i] == null) {
                        flag = true;
                        mapLines[i] = this;
                        intersectPoints[i].moveTo(p.getX(), p.getY());
                    }
                }
            }
        }
        return flag;
    }
    
    @Override
    public BufferedImage getSubImage(Point p) {
        long xOffset = Math.round(SpecialMath.lineLength(start, p) * 20);
        int textureOffset = (int)(xOffset % texture.getWidth());
        return texture.getSubImage(textureOffset, 0, 1, texture.getHeight());
    }
    
}
