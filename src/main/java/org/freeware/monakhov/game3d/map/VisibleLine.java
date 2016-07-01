package org.freeware.monakhov.game3d.map;

import java.awt.image.BufferedImage;
import org.freeware.monakhov.game3d.SpecialMath;

/**
 *
 * @author Vasily Monakhov 
 */
public abstract class VisibleLine extends Line {

    public VisibleLine(Point start, Point end, World world) {
        super(start, end, world);
    }

    public abstract Texture getTexture();
    
    public abstract boolean pointIsVisible(Point p);
    
    /**
     * Проверяет видимость линии на экране
     */
    @Override
    boolean checkVisibility(VisibleLine[] mapLines, Point viewPoint, Point[] rayPoints, Point[] intersectPoints) {
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
    

    public abstract BufferedImage getSubImage(Point p, int height);
    
}
