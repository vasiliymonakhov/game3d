package org.freeware.monakhov.game3d.map;

import java.awt.image.BufferedImage;
import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 *
 * @author Vasily Monakhov
 */
public class SecretDoor extends AbstractDoor {

    private final Texture texture;
    
    public SecretDoor(Point start, Point end, Texture texture, World world) {
        super(start, end, world);
        this.texture = texture;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }    
    
    @Override
    public BufferedImage getSubImage(Point p, double height) {
        int xOffset = (int) Math.round(SpecialMath.lineLength(start, p) - width * opened);
        return getTexture().getSubImage(xOffset, height);
    }

    private static final double OPEN_SPEED = 0.5e-9;

    @Override
    public void doSomething(long frameNanoTime) {
        if (state == OPENING) {
            opened += OPEN_SPEED * frameNanoTime;
            if (opened >= 1) {
                opened = 1;
                state = OPEN;
            }
        }
    }

    @Override
    public void onInteractWith(WorldObject wo1) {
        if (state == CLOSED) {
            state = OPENING;
        } 
    }

}
