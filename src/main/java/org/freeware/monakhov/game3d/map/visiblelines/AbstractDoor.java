package org.freeware.monakhov.game3d.map.visiblelines;

import java.awt.image.BufferedImage;
import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;

/**
 * Абстрактная дверь. Это такая стена, которая умеет отъезжать в сторону 
 * @author Vasily Monakhov 
 */
public abstract class AbstractDoor  extends VisibleLine {

    /**
     * Закрыта
     */
    final static int CLOSED = 0;
    /**
     * Открывается
     */
    final static int OPENING = 1;
    /**
     * Открыта
     */
    final static int OPEN = 2;
    /**
     * Закрывается
     */
    final static int CLOSING = 3;

    /**
     * Состояние двери
     */
    protected int state = CLOSED;
    /**
     * Степень открытия
     */
    protected double opened;
    /**
     * Ширина двери
     */
    protected final double width;    
    
    /**
     * Создайт дверь
     * @param start точка начала
     * @param end точка конца
     * @param world мир
     */
    public AbstractDoor(Point start, Point end, World world) {
        super(start, end, world);
        width = SpecialMath.lineLength(start, end);        
    }

    @Override
    public boolean isCrossable() {
        // пройти можно только через открытую дверь
        return state == OPEN;
    }

    @Override
    public boolean isVisible() {
        // видеть можно только не открытую дверь
        return state != OPEN;
    }

    @Override
    public boolean pointIsVisible(Point p) {
        if (state == CLOSED) {
            // точка однозначно видна если дверь закрыта
            return true;
        }
        if (state == OPEN) {
            // точка однозначно не видна если дверь открыта
            return false;
        }
        // видна точка в открытой части двери
        return SpecialMath.lineLength(start, p) >= opened * width;
    }
    
    @Override
    public BufferedImage getSubImage(Point p, double height) {
        // опрежелим смещение текстуры двери в зависимости от степени открытости двери
        int xOffset = (int) Math.round(SpecialMath.lineLength(start, p) - width * opened);
        return getTexture().getSubImage(xOffset, height);
    }    
    
}
