package org.freeware.monakhov.game3d.weapons;

import java.util.List;
import org.freeware.monakhov.game3d.ScreenBuffer;
import org.freeware.monakhov.game3d.SoundSystem;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.movable.MovableObject;
import org.freeware.monakhov.game3d.objects.nonmovable.Ammo;
import org.freeware.monakhov.game3d.resources.MultiImage;

/**
 * Пистолетик
 * @author Vasily Monakhov
 */
public class AssaultRifle extends InstantBulletWeapon {

    public AssaultRifle(World world, MovableObject owner) {
        super(world, owner);
        ammo = 30;
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
        return 16 * getFireDistance() / distance;
    }

    @Override
    public void pickUpAmmo(Ammo wo) {
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
    public List<MultiImage.ImageToDraw> getWeaponView(ScreenBuffer screen) {
        if (timeFromLastShot >= getTimeBetweenShots()) {
            return images[images.length - 1].getImagesToDraw(screen);
        } else {
            int i = (int)((images.length - 1) * timeFromLastShot / getTimeBetweenShots());
            return images[i].getImagesToDraw(screen);
        }
    }

    private final static MultiImage[] images = {MultiImage.get("assault_rifle02"), MultiImage.get("assault_rifle03"),
        MultiImage.get("assault_rifle04"), MultiImage.get("assault_rifle01")};

}
