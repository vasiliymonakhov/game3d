package org.freeware.monakhov.game3d.weapons;

import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.movable.MovableObject;

/**
 *
 * @author Vasily Monakhov
 */
public abstract class InstantBulletWeapon extends Weapon {

    public InstantBulletWeapon(World world, MovableObject owner) {
        super(world, owner);
    }


    /**
     * Расчёт повреждений в зависимости от расстояния до цели
     * @param distance расстояние до цели
     * @return величина повреждения в % здоровья
     */
    public abstract double getDamage(double distance);

    @Override
    public void makeFire() {
        WorldObject candidateToDie = checkFireLine(aim());
        if (candidateToDie != null) {
            // в кого-то попали, нанесём ему урон
            candidateToDie.onGetDamage(getDamage(SpecialMath.lineLength(owner.getPosition(), candidateToDie.getPosition())));
        }
    }

}
