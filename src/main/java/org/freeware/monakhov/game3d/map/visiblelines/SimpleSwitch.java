package org.freeware.monakhov.game3d.map.visiblelines;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.resources.Texture;
import org.freeware.monakhov.game3d.map.World;

/**
 * Простой выключатель на два положения
 * @author Vasily Monakhov 
 */
public class SimpleSwitch extends AbstractSwitch {

    /**
     * Текстура включенного выключателя
     */
    private final Texture onTexture;
    /**
     * Текстура выключенного выключателя
     */
    private final Texture offTexture;    
    
    /**
     * Создайт выключатель
     * @param start точканачала
     * @param end точка конца
     * @param onTexture текстура выключенного состояния
     * @param offTexture текстура включенного состояния
     * @param world мир
     */
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
