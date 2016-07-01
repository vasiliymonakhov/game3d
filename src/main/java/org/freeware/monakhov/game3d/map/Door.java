package org.freeware.monakhov.game3d.map;

import java.awt.image.BufferedImage;
import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 *
 * @author Vasily Monakhov
 */
public class Door extends VisibleLine {

    final static int CLOSED = 0;
    final static int OPENING = 1;
    final static int OPEN = 2;
    final static int CLOSING = 3;

    private int state = CLOSED;
    private double opened;
    private final double width;
    
    private final static Texture OPEN_TEXTURE = Texture.get("door_open01");
    private final static Texture CLOSED_TEXTURE = Texture.get("door_closed01");
    
    public Door(Point start, Point end, World world) {
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
    public Texture getTexture() {
        if (state == OPEN || state == OPENING) return OPEN_TEXTURE;
        return CLOSED_TEXTURE;
    }    
    
    @Override
    public BufferedImage getSubImage(Point p, int height) {
        int xOffset = (int) Math.round(SpecialMath.lineLength(start, p) - width * opened);
        return getTexture().getSubImage(xOffset, height);
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
