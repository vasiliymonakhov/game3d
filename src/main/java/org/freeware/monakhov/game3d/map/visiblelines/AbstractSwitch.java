package org.freeware.monakhov.game3d.map.visiblelines;

import java.awt.image.BufferedImage;
import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Texture;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 *
 * @author Vasily Monakhov 
 */
public abstract class AbstractSwitch extends VisibleLine {

    protected final int OFF = 0;
    protected final int ON = 1;
    
    protected int state = OFF;
    
    abstract Texture getOnTexture();
    abstract Texture getOffTexture();
    
    @Override
    public Texture getTexture() {
        switch (state) {
            case ON : return getOnTexture();
            case OFF: return getOffTexture();    
        }
        return null;
    }
    
    public AbstractSwitch(Point start, Point end, World world) {
        super(start, end, world);
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public boolean pointIsVisible(Point p) {
        return true;
    }

    @Override
    public boolean isCrossable() {
        return false;
    }

    @Override
    public BufferedImage getSubImage(Point p, double height) {
        int xOffset = (int)Math.round(SpecialMath.lineLength(start, p));
        return getTexture().getSubImage(xOffset, height);
    }
    
    @Override
    public void onInteractWith(WorldObject wo) {
        if (state == ON) {
            state = OFF;
        } else {
            state = ON;
        }
    }    

}
