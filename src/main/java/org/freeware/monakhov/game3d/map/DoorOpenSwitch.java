package org.freeware.monakhov.game3d.map;

import static org.freeware.monakhov.game3d.map.AbstractDoor.OPEN;
import static org.freeware.monakhov.game3d.map.AbstractDoor.OPENING;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 *
 * @author Vasily Monakhov 
 */
public class DoorOpenSwitch extends SimpleSwitch {

    private final AbstractDoor door;
    
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
        if (door.state == OPEN || door.state == OPENING) {
            state = ON;
        } else {
            state = OFF;
        }
    }    

}
