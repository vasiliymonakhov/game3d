package org.freeware.monakhov.game3d.objects.movable;

import org.freeware.monakhov.game3d.objects.nonmovable.FireballGun;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.xml.sax.Attributes;

/**
 * Нечто в мире, что может что-то делать само
 * @author Vasily Monakhov
 */
public abstract class Entity extends MovableObject {

    public Entity(World world, Point position, WorldObject creator, double azimuth) {
        super(world, position, creator);
        setAzimuth(azimuth);
    }

    /**
     * Создаёт объекты из XML
     * @param world мир
     * @param attr запись файла
     * @return созданный объект
     */
    public static WorldObject createFromXML(World world, Attributes attr) {
        String clasz = attr.getValue("class");
        switch (clasz) {
            case "fireball_gun" :
                return new FireballGun(world, new Point(attr), Math.PI * 2 * Integer.parseInt(attr.getValue("azimuth")) / 360);
        }
        return null;
    }

}
