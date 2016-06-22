package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;

/**
 * Статический объект, через который можно перейти
 * @author Vasily Monakhov 
 */
abstract public class CrossableObject extends NonMovableObject {

    public CrossableObject(World world, Point position) {
        super(world, position);
    }


    @Override
    public boolean isCrossable() {
        return true;
    }    

}
