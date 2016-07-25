package org.freeware.monakhov.game3d.weapons;

import java.util.List;
import org.freeware.monakhov.game3d.ScreenBuffer;
import org.freeware.monakhov.game3d.SoundSystem;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.movable.MovableObject;
import org.freeware.monakhov.game3d.objects.nonmovable.CanGiveAmmo;
import org.freeware.monakhov.game3d.objects.nonmovable.MachineGunAmmo;
import org.freeware.monakhov.game3d.objects.nonmovable.MachineGunOnMap;
import org.freeware.monakhov.game3d.resources.BigImage;

/**
 * Пистолетик
 * @author Vasily Monakhov
 */
public class MachineGun extends InstantBulletWeapon {

    public MachineGun(World world, MovableObject owner, int ammo) {
        super(world, owner, ammo);
    }

    @Override
    public double getAimError() {
         return -0.02 + 0.04 * Math.random();
    }

    @Override
    public double getFireDistance() {
        return 65535;
    }

    @Override
    public double getDamage(double distance) {
        return 16 * getFireDistance() / distance;
    }

    @Override
    public long getTimeBetweenShots() {
        return 50000000l;
    }

    @Override
    public String getImageName() {
        return "machine_gun";
    }

    @Override
    void playShotSound() {
            SoundSystem.play("rifle_shot");
    }

    @Override
    public boolean isLowAmmo() {
        return ammo < 30;
    }

  @Override
    public List<BigImage.ImageToDraw> getWeaponView(ScreenBuffer screen) {
        if (timeFromLastShot >= getTimeBetweenShots()) {
            return images[images.length - 1].getImagesToDraw(screen);
        } else {
            int i = (int)((images.length - 1) * timeFromLastShot / getTimeBetweenShots());
            return images[i].getImagesToDraw(screen);
        }
    }

    private final static BigImage[] images = {BigImage.get("machine_gun02"), BigImage.get("machine_gun03"),
        BigImage.get("machine_gun04"), BigImage.get("machine_gun05"), BigImage.get("machine_gun01")};

    @Override
    public void pickUpAmmo(CanGiveAmmo wo) {
        if (ammo == 500) return;
        if (wo instanceof MachineGunAmmo || wo instanceof MachineGunOnMap) {
            ammo += wo.getAmmo();
            if (ammo > 500) ammo = 500;
            world.deleteObject(wo);
        }
    }

}
