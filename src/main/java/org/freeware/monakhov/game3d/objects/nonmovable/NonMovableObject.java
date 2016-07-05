package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 * Неподвижный объект
 * @author Vasily Monakhov 
 */
abstract class NonMovableObject extends WorldObject {

    /**
     * Создаёт объект
     * @param world мир
     * @param position позиция
     */
    public NonMovableObject(World world, Point position) {
        super(world, position);
    }

}
