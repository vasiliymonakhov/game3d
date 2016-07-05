package org.freeware.monakhov.game3d.map.visiblelines;

import java.awt.image.BufferedImage;
import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Texture;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 * Абстрактный переключатель. Это такая стена, которая может менять свой состояние и влиять на другие объекты мира
 * @author Vasily Monakhov 
 */
public abstract class AbstractSwitch extends VisibleLine {

    /**
     * Выключен. Не факт, что состояний будет несколько, поэтому не boolean
     */
    protected final int OFF = 0;
    /**
     * Включен
     */
    protected final int ON = 1;
    
    /**
     * Текущее состояние
     */
    protected int state = OFF;
    
    /**
     * Вовзращает текстуру для включенного выключателя
     * @return 
     */
    abstract Texture getOnTexture();
    /**
     * Возвращает текстуру для выключенного выключателя
     * @return 
     */
    abstract Texture getOffTexture();
    
    /**
     * Вовзращает текстуру
     * @return текстура
     */
    @Override
    public Texture getTexture() {
        switch (state) {
            case ON : return getOnTexture();
            case OFF: return getOffTexture();    
        }
        return null;
    }
    
    /**
     * Создаёт выключатель
     * @param start точка начала
     * @param end точка конца
     * @param world мир
     */
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
            // если был выключен - то включить
            state = OFF;
        } else {
            // если был включен, то выключить
            state = ON;
        }
    }    

}
