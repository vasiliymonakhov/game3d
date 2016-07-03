package org.freeware.monakhov.game3d.map.visiblelines;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Texture;
import org.freeware.monakhov.game3d.map.World;

/**
 *
 * @author Vasily Monakhov 
 */
public class SimpleSwitch extends AbstractSwitch {

    private final Texture onTexture;
    private final Texture offTexture;    
    
    public SimpleSwitch(Point start, Point end, Texture onTexture, Texture offTexture, World world) {
        super(start, end, world);
        this.onTexture = onTexture;
        this.offTexture = offTexture;
    }
    
    @Override
    Texture getOnTexture() {
        return onTexture;
    }

    @Override
    Texture getOffTexture() {
        return offTexture;
    }

}
