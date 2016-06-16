package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;

/**
 * Статический объект, через который можно перейти
 * @author Vasily Monakhov 
 */
abstract public class CrossableObject extends NonMovableObject {

    public CrossableObject(Point position) {
        super(position);
    }

    @Override
    public boolean isCrossable() {
        return true;
    }    

}
