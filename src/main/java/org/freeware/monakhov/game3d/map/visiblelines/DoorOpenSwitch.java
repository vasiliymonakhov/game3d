package org.freeware.monakhov.game3d.map.visiblelines;

import org.freeware.monakhov.game3d.map.Line;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.resources.Texture;
import org.freeware.monakhov.game3d.map.World;
import static org.freeware.monakhov.game3d.map.visiblelines.AbstractDoor.OPEN;
import static org.freeware.monakhov.game3d.map.visiblelines.AbstractDoor.OPENING;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 * Выключатель, открывающий двери
 * @author Vasily Monakhov 
 */
public class DoorOpenSwitch extends SimpleSwitch {

    /**
     * Дверь
     */
    private final AbstractDoor door;
    
    /**
     * Создайт выключатель
     * @param start точка начала
     * @param end точка конца
     * @param onTexture текстура выключенного состояния
     * @param offTexture текстура включенного состояния
     * @param door дверь, которой надо управлять
     * @param world мир
     */
    public DoorOpenSwitch(Point start, Point end, Texture onTexture, Texture offTexture, Line door, World world) {
        super(start, end, onTexture, offTexture, world);
        this.door = (AbstractDoor)door;
    }
    
    @Override
    public void onInteractWith(WorldObject wo) {
        door.onInteractWith(wo);        
    }     
    
    @Override
    public void doSomething(long frameNanoTime) {
        // определяем сосотояние выключателя в зависимости от стосяния двери
        if (door.state == OPEN || door.state == OPENING) {
            state = ON;
        } else {
            state = OFF;
        }
    }    

}
