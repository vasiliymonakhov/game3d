package org.freeware.monakhov.game3d.map;

import java.awt.image.BufferedImage;
import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 *
 * @author Vasily Monakhov
 */
public class Door extends AbstractDoor {

   
    private final Texture openTexture;
    private final Texture closedTexture;
    
    public Door(Point start, Point end, Texture openTexture, Texture closedTexture, World world) {
        super(start, end, world);
        this.openTexture = openTexture;
        this.closedTexture = closedTexture;
    }

    @Override
    public Texture getTexture() {
        if (state == OPEN || state == OPENING) return openTexture;
        return closedTexture;
    }    

    private long openedTime;

    private static final long MAX_OPENED_TIME = 5000000000l;
    private static final double OPEN_SPEED = 0.5e-9;

    @Override
    public void doSomething(long frameNanoTime) {
        if (state == OPEN) {
            openedTime += frameNanoTime;
            if (openedTime > MAX_OPENED_TIME) {
                openedTime = 0;
                if (somebodyInside()) return;
                state = CLOSING;
            }
        } else if (state == CLOSING) {
            opened -= OPEN_SPEED * frameNanoTime;
            if (opened <= 0) {
                opened = 0;
                state = CLOSED;
            }
        } else if (state == OPENING) {
            opened += OPEN_SPEED * frameNanoTime;
            if (opened >= 1) {
                opened = 1;
                state = OPEN;
            }
        }
    }

    private boolean somebodyInside() {
        if (SpecialMath.lineAndCircleIntersects(start, end, world.getHero().getPosition(), world.getHero().getRadius())) {
            return true;
        }
        for (WorldObject wo : world.getAllObjects()) {
            if (SpecialMath.lineAndCircleIntersects(start, end, wo.getPosition(), wo.getRadius())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onInteractWith(WorldObject wo1) {
        if (state == CLOSED) {
            state = OPENING;
        } else if (state == OPEN) {
            if (somebodyInside()) return;
            state = CLOSING;
        } else if (state == OPENING) {
            state = CLOSING;
        } else if (state == CLOSING) {
            state = OPENING;
        }
    }

}
