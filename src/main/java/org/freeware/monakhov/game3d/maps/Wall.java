/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.maps;

import org.freeware.monakhov.game3d.Hero;
import org.freeware.monakhov.game3d.SpecialMath;

/**
 * Wall in a map
 * @author Vasily Monakhov 
 */
public class Wall extends Line {

    public Wall(Point start, Point end) {
        super(start, end);
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
    
    
}
