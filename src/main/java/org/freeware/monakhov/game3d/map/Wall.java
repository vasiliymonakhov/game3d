/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.map;

import java.awt.image.BufferedImage;
import org.freeware.monakhov.game3d.SpecialMath;

/**
 * Wall in a map
 * @author Vasily Monakhov 
 */
public class Wall extends Line {

    protected final Texture texture;
    
    public Wall(Point start, Point end, Texture texture, World world) {
        super(start, end, world);
        this.texture = texture;
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
     * @return можно ли пересекать через линию
     */
    @Override
    public boolean isCrossable() {
        return false;
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
                        if (pointIsVisible(p)) {
                            mapLines[i] = this;
                            everSeen = true;
                            intersectPoints[i].moveTo(p.getX(), p.getY());
                        }
                    }
                }
            }
        }
        return flag;
    }
    
    @Override
    public BufferedImage getSubImage(Point p, int height) {
        int xOffset = (int)Math.round(SpecialMath.lineLength(start, p));
        return texture.getSubImage(xOffset, height);
    }
    
    
}
