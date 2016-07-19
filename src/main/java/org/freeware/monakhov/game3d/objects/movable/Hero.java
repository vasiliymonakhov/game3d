package org.freeware.monakhov.game3d.objects.movable;

import java.util.List;
import org.freeware.monakhov.game3d.ScreenBuffer;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.resources.Sprite;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.nonmovable.Ammo;
import org.freeware.monakhov.game3d.resources.MultiImage;
import org.freeware.monakhov.game3d.weapons.AssaultRifle;
import org.freeware.monakhov.game3d.weapons.FireBallGun;
import org.freeware.monakhov.game3d.weapons.MachineGun;
import org.freeware.monakhov.game3d.weapons.Pistol;
import org.freeware.monakhov.game3d.weapons.Weapon;

/**
 * Наш герой
 *
 * @author Vasily Monakhov
 */
public class Hero extends MovableObject {

    /**
     * Создаёт объект
     *
     * @param world мир
     * @param position мозиция
     */
    public Hero(World world, Point position) {
        super(world, position, null);
        weapons = new Weapon[5];
    }

    public void takeWeapons() {
        weapons[1] = new Pistol(world, this);
        weapons[2] = new AssaultRifle(world, this);
        weapons[3] = new MachineGun(world, this);
        weapons[4] = new FireBallGun(world, this);
    }

    @Override
    public Sprite getSprite() {
        return null;
    }

    @Override
    public double getRadius() {
        return 48;
    }

    @Override
    public double getInteractRadius() {
        return 96;
    }

    @Override
    public boolean isCrossable() {
        return false;
    }

    /**
     * Максимальная скорость движения назад
     */
    public static final double MAX_BACKWARD_MOVE_SPEED = -256 / 1.0E9;
    /**
     * Максимальная скорость движения вперёд
     */
    public static final double MAX_FORWARD_MOVE_SPEED = 1024 / 1.0E9;
    /**
     * Максимальная скорость движения в бок
     */
    public static final double MAX_STRAFE_SPEED = 1024 / 1.0E9;
    /**
     * Максимальная скорость поворота
     */
    public static final double MAX_TURN_SPEED = Math.PI / 1.5E9;

    /**
     * Ускорение при движении вперёд
     */
    public static final double MOVE_FORWARD_ACCELERATION = 2048 / 1.0E18;
    /**
     * Ускорение при движении назад
     */
    public static final double MOVE_BACKWARD_ACCELERATION = -512 / 1.0E18;
    /**
     * Ускорение при движении в бок
     */
    public static final double STRAFE_ACCELERATION = 512 / 1.0E18;
    /**
     * Ускорение при повороте
     */
    public static final double TURN_ACCELERATION = 2.5 * Math.PI / 1.0E18;

    /**
     * Замедление при движении прямо
     */
    public static final double MOVE_BREAKING = 4096 / 1.0E18;
    /**
     * Замедление при движении в бок
     */
    public static final double STRAFE_BREAKING = 4096 / 1.0E18;
    /**
     * Замедление при поворотах
     */
    public static final double TURN_BREAKING = 3 * Math.PI / 1.0E18;

    /**
     * Скорость движения в бок
     */
    private double strafeSpeed = 0;
    /**
     * Скорость движения прямо
     */
    private double moveSpeed = 0;
    /**
     * Скорость поворота
     */
    private double turnSpeed = 0;

    /**
     * Анализ поворота
     *
     * @param left поворачивать влево
     * @param right поворачивать вправо
     * @param frameNanoTime время
     */
    private void analyseTurn(boolean left, boolean right, long frameNanoTime) {
        if (left) {
            if (turnSpeed > 0) {
                turnSpeed = turnSpeed - TURN_BREAKING * frameNanoTime;
            } else {
                turnSpeed = turnSpeed - TURN_ACCELERATION * frameNanoTime;
            }
            if (turnSpeed < -MAX_TURN_SPEED) {
                turnSpeed = -MAX_TURN_SPEED;
            }
        } else if (right) {
            if (turnSpeed < 0) {
                turnSpeed = turnSpeed + TURN_BREAKING * frameNanoTime;
            } else {
                turnSpeed = turnSpeed + TURN_ACCELERATION * frameNanoTime;
            }
            if (turnSpeed > MAX_TURN_SPEED) {
                turnSpeed = MAX_TURN_SPEED;
            }
        } else {
            if (turnSpeed > 0) {
                turnSpeed = turnSpeed - TURN_BREAKING * frameNanoTime;
                if (turnSpeed < 0) {
                    turnSpeed = 0;
                }
            } else if (turnSpeed < 0) {
                turnSpeed = turnSpeed + TURN_BREAKING * frameNanoTime;
                if (turnSpeed > 0) {
                    turnSpeed = 0;
                }
            }
        }
    }

    /**
     * Анализ двидения пряма
     *
     * @param forward идти вперёд
     * @param backward идти назад
     * @param frameNanoTime время
     */
    private void analyseMove(boolean forward, boolean backward, long frameNanoTime) {
        if (forward) {
            if (moveSpeed < 0) {
                moveSpeed = moveSpeed + MOVE_BREAKING * frameNanoTime;
            } else {
                moveSpeed = moveSpeed + MOVE_FORWARD_ACCELERATION * frameNanoTime;
            }
            if (moveSpeed > MAX_FORWARD_MOVE_SPEED) {
                moveSpeed = MAX_FORWARD_MOVE_SPEED;
            }
        } else if (backward) {
            if (moveSpeed > 0) {
                moveSpeed = moveSpeed - MOVE_BREAKING * frameNanoTime;
            } else {
                moveSpeed = moveSpeed + MOVE_BACKWARD_ACCELERATION * frameNanoTime;
            }
            if (moveSpeed < MAX_BACKWARD_MOVE_SPEED) {
                moveSpeed = MAX_BACKWARD_MOVE_SPEED;
            }
        } else {
            if (moveSpeed > 0) {
                moveSpeed = moveSpeed - MOVE_BREAKING * frameNanoTime;
                if (moveSpeed < 0) {
                    moveSpeed = 0;
                }
            } else if (moveSpeed < 0) {
                moveSpeed = moveSpeed + MOVE_BREAKING * frameNanoTime;
                if (moveSpeed > 0) {
                    moveSpeed = 0;
                }
            }
        }
    }

    /**
     * Анализ вдидения в бок
     *
     * @param strafeLeft двигаться влеов
     * @param strafeRight двигаться вправо
     * @param frameNanoTime время
     */
    private void analyseStrafe(boolean strafeLeft, boolean strafeRight, long frameNanoTime) {
        if (strafeLeft) {
            if (strafeSpeed > 0) {
                strafeSpeed = strafeSpeed - STRAFE_BREAKING * frameNanoTime;
            } else {
                strafeSpeed = strafeSpeed - STRAFE_ACCELERATION * frameNanoTime;
            }
            if (strafeSpeed < -MAX_STRAFE_SPEED) {
                strafeSpeed = -MAX_STRAFE_SPEED;
            }
        } else if (strafeRight) {
            if (strafeSpeed < 0) {
                strafeSpeed = strafeSpeed + STRAFE_BREAKING * frameNanoTime;
            } else {
                strafeSpeed = strafeSpeed + STRAFE_ACCELERATION * frameNanoTime;
            }
            if (strafeSpeed > MAX_STRAFE_SPEED) {
                strafeSpeed = MAX_STRAFE_SPEED;
            }
        } else {
            if (strafeSpeed > 0) {
                strafeSpeed = strafeSpeed - STRAFE_BREAKING * frameNanoTime;
                if (strafeSpeed < 0) {
                    strafeSpeed = 0;
                }
            } else if (strafeSpeed < 0) {
                strafeSpeed = strafeSpeed + STRAFE_BREAKING * frameNanoTime;
                if (strafeSpeed > 0) {
                    strafeSpeed = 0;
                }
            }
        }
    }

    /**
     * Анализ движения
     *
     * @param left поворачиваться влево
     * @param right поворачиваться вправо
     * @param forward двигаться вперёд
     * @param backward двигаться назад
     * @param strafeLeft двигаться влево
     * @param strafeRight двигаться вправо
     * @param frameNanoTime время
     */
    public void analyseKeys(boolean left, boolean right, boolean forward, boolean backward, boolean strafeLeft, boolean strafeRight, long frameNanoTime) {
        analyseTurn(left, right, frameNanoTime);
        setAzimuth(getAzimuth() + turnSpeed * frameNanoTime);
        analyseMove(forward, backward, frameNanoTime);
        analyseStrafe(strafeLeft, strafeRight, frameNanoTime);
        double ms = moveSpeed * frameNanoTime;
        double ss = strafeSpeed * frameNanoTime;
        if (ms != 0 || ss != 0) {
            moveByWithCheck(ms, ss);
        }
    }

    public void changeWeapon(boolean w0, boolean w1, boolean w2, boolean w3, boolean w4) {
        if (w4) {
            changeWeapon(4);
            return;
        }
        if (w3) {
            changeWeapon(3);
            return;
        }
        if (w2) {
            changeWeapon(2);
            return;
        }
        if (w1) {
            changeWeapon(1);
            return;
        }
        if (w0) {
            changeWeapon(0);
        }
    }

    private final Weapon[] weapons;
    private volatile int currentWeapon = 1;

    private volatile long painTime, painTimeCounter;
    private volatile int painLevel;

    @Override
    public void doSomething(long frameNanoTime) {
        weapons[currentWeapon].doSomething(frameNanoTime);
        if (weaponChanging) {
            weaponChangingTime += frameNanoTime;
            if (weaponChangingTime > WEAPON_CHANGE_TIME) {
                weaponChangingTime = WEAPON_CHANGE_TIME;
                weaponChanging = false;
                currentWeapon = newWeapon;
            }
        }
        if (painLevel > 0) {
            painTimeCounter += frameNanoTime;
            painLevel = (int) Math.round(painLevel * (painTime - painTimeCounter) / painTime);
        }
    }

    public void fire() {
        if (weaponChanging) {
            return;
        }
        weapons[currentWeapon].fire();
        if (weapons[currentWeapon].isOutOfAmmo()) {
            for (int i = currentWeapon; i >= 0; i--) {
                if (weapons[i] != null && !weapons[i].isOutOfAmmo()) {
                    changeWeapon(i);
                    break;
                }
            }
        }
    }

    private volatile boolean weaponChanging;
    private  volatile long weaponChangingTime;
    private volatile int newWeapon;

    private final long WEAPON_HIDE_TIME = 500000000l;
    private final long WEAPON_TAKE_TIME = 500000000l;
    private final long WEAPON_CHANGE_TIME = WEAPON_HIDE_TIME + WEAPON_TAKE_TIME;

    void changeWeapon(int newWeapon) {
        if (weaponChanging  || newWeapon == currentWeapon) return;
        if (weapons[newWeapon] != null) {
            weaponChanging = true;
            weaponChangingTime = 0;
            this.newWeapon = newWeapon;
        }
    }

    private volatile double armor = 100;

    private volatile double health = 100;

    public void addHealth(double val) {
        health += val;
        if (health > 100) {
            health = 100;
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public void onGetDamage(double d) {
        int newPainLevel = (int) d * 10;
        if (newPainLevel > 255) {
            newPainLevel = 255;
        }
        if (newPainLevel > painLevel) {
            painLevel = newPainLevel;
            painTime = Math.round(d * 500000000l);
            painTimeCounter = 0;
        }
        if (armor > 0) {
            armor -= d;
            if (armor < 0) {
                health += armor;
                armor = 0;
            }
        } else {
            health -= d;
        }
        if (health < 0) {
            health = 0;
        }
    }

    @Override
    public void onInteractWith(WorldObject wo) {
        if (wo instanceof Ammo) {
            for (Weapon wp : weapons) {
                if (wp != null) {
                    wp.pickUpAmmo((Ammo) wo);
                }
            }
        }
    }

    @Override
    public void onCollapseWith(WorldObject wo) {
    }

    @Override
    public void onCycleEnd() {
    }

    /**
     * @return the health
     */
    public double getHealth() {
        return health >= 1 ? health : 0;
    }

    public String getHealthString() {
        return String.format("%d", (int) getHealth());
    }

    public String getArmorString() {
        return String.format("%d", (int) armor);
    }

    public String getWeaponString() {
        return weapons[currentWeapon].getImageName();
    }

    public String getAmmoString() {
        return weapons[currentWeapon].getAmmoString();
    }

    public boolean isLowAmmo() {
        return weapons[currentWeapon].isLowAmmo();
    }

    public boolean isLowArmor() {
        return armor < 25;
    }

    public boolean isLowHealth() {
        return health < 40;
    }

    /**
     * @return the painLevel
     */
    public int getPainLevel() {
        return painLevel;
    }

    @Override
    public double getAimError() {
        return 0;
    }

    public List<MultiImage.ImageToDraw> getWeaponView(ScreenBuffer screen) {
        if (weaponChanging) {
            if (weaponChangingTime < WEAPON_HIDE_TIME) {
                return weapons[currentWeapon].getWeaponView(screen);
            } else {
                return weapons[newWeapon].getWeaponView(screen);
            }
        }
        return weapons[currentWeapon].getWeaponView(screen);
    }

    public int getWeaponX(ScreenBuffer screen) {
        return 0;
    }

    public int getWeaponY(ScreenBuffer screen) {
        if (weaponChanging) {
            long wct = weaponChangingTime;
            if (wct < WEAPON_HIDE_TIME) {
                return (int)(screen.getHeight() * wct / WEAPON_HIDE_TIME);
            } else {
                return (int)(screen.getHeight() - screen.getHeight() * (wct - WEAPON_HIDE_TIME) / WEAPON_TAKE_TIME);
            }
        }
        return 0;
    }
}
