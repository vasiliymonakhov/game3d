package org.freeware.monakhov.game3d.map.visiblelines;

import org.freeware.monakhov.game3d.map.Line;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Texture;
import org.freeware.monakhov.game3d.map.World;
import static org.freeware.monakhov.game3d.map.visiblelines.AbstractDoor.OPEN;
import static org.freeware.monakhov.game3d.map.visiblelines.AbstractDoor.OPENING;
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
