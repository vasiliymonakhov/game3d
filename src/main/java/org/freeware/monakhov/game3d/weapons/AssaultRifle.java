package org.freeware.monakhov.game3d.weapons;

import java.util.List;
import org.freeware.monakhov.game3d.ScreenBuffer;
import org.freeware.monakhov.game3d.SoundSystem;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.movable.MovableObject;
import org.freeware.monakhov.game3d.objects.nonmovable.AssaultRifleAmmo;
import org.freeware.monakhov.game3d.objects.nonmovable.AssaultRifleOnMap;
import org.freeware.monakhov.game3d.objects.nonmovable.CanGiveAmmo;
import org.freeware.monakhov.game3d.resources.BigImage;

/**
 * Пистолетик
 * @author Vasily Monakhov
 */
public class AssaultRifle extends InstantBulletWeapon {

    public AssaultRifle(World world, MovableObject owner, int ammo) {
        super(world, owner, ammo);
    }

    @Override
    public double getAimError() {
         return -0.01 + 0.02 * Math.random();
    }

    @Override
    public double getFireDistance() {
        return 65535;
    }

    @Override
    public double getDamage(double distance) {
        return 8 * getFireDistance() / distance;
    }

     @Override
    public void pickUpAmmo(CanGiveAmmo wo) {
        if (ammo == 100) return;
        if (wo instanceof AssaultRifleAmmo || wo instanceof AssaultRifleOnMap) {
            ammo += wo.getAmmo();
            if (ammo > 100) ammo = 100;
            world.deleteObject(wo);
        }
    }

    @Override
    public long getTimeBetweenShots() {
        return 100000000l;
    }

    @Override
    public String getImageName() {
        return "assault_rifle";
    }

    @Override
    void playShotSound() {
            SoundSystem.play("rifle_shot");
    }

      @Override
    public boolean isLowAmmo() {
        return ammo < 5;
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

    private final static BigImage[] images = {BigImage.get("assault_rifle02"), BigImage.get("assault_rifle03"),
        BigImage.get("assault_rifle04"), BigImage.get("assault_rifle01")};

}
