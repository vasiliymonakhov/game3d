package org.freeware.monakhov.game3d.map;

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
