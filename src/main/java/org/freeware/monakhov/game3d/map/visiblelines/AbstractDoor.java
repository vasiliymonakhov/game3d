package org.freeware.monakhov.game3d.map.visiblelines;

import java.awt.image.BufferedImage;
import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;

/**
 *
 * @author Vasily Monakhov 
 */
public abstract class AbstractDoor  extends VisibleLine {

    final static int CLOSED = 0;
    final static int OPENING = 1;
    final static int OPEN = 2;
    final static int CLOSING = 3;

    protected int state = CLOSED;
    protected double opened;
    protected final double width;    
    
    public AbstractDoor(Point start, Point end, World world) {
        super(start, end, world);
        width = SpecialMath.lineLength(start, end);        
    }

    @Override
    public boolean isCrossable() {
        return state == OPEN;
    }

    @Override
    public boolean isVisible() {
        return state != OPEN;
    }

    @Override
    public boolean pointIsVisible(Point p) {
        if (state == CLOSED) {
            return true;
        }
        if (state == OPEN) {
            return false;
        }
        return SpecialMath.lineLength(start, p) >= opened * width;
    }
    
    @Override
    public BufferedImage getSubImage(Point p, double height) {
        int xOffset = (int) Math.round(SpecialMath.lineLength(start, p) - width * opened);
        return getTexture().getSubImage(xOffset, height);
    }    
    
}
