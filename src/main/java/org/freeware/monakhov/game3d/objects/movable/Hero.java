
package org.freeware.monakhov.game3d.objects.movable;

import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;


/**
 * This is who are You! :)
 * @author Vasily Monakhov 
 */
public class Hero extends MovableObject {

    private final World world;
    
    public Hero(Point position, World world) {
        super(position);
        this.world = world;
    }

    @Override
    public Sprite getSprite() {
        return null;
    }

    @Override
    public double getRadius() {
        return 64;
    }
    
    @Override
    public void moveBy(double df, double ds) {
        super.moveBy(df, ds);
        if (oldPosition.getX() != position.getX() || oldPosition.getY() != position.getY()) {
            for (WorldObject o : world.getAllObjects()) {
                if (!o.isCrossable()) {
                    // Через объект нельзя пройти
                    double distance = SpecialMath.lineLength(o.getPosition(), position);
                    double radiuses = o.getRadius() + getRadius();
                    if (distance < radiuses) {
                        // расстояние между объектами слишком мало, пройти нельзя
                        position.moveTo(oldPosition.getX(), oldPosition.getY());
                    }
                }
            }
        }
    }    
    
}
