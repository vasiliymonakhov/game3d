package org.freeware.monakhov.game3d.objects.movable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.nonmovable.Ammo;
import org.freeware.monakhov.game3d.weapons.FireBallGun;
import org.freeware.monakhov.game3d.weapons.Weapon;

/**
 * Наш герой
 *
 * @author Vasily Monakhov
 */
public class Hero extends MovableObject {

    /**
     * Создаёт объект
     * @param world мир
     * @param position мозиция
     */
    public Hero(World world, Point position) {
        super(world, position, null);
        weapon = new FireBallGun(world);
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

//    /**
//     * изображение оружия в руках
//     */
//    MultiImage weapon = MultiImage.get("pistol");
//
//
//    public List<MultiImage.ImageToDraw> getImagesToDraw(ScreenBuffer screen) {
//        return weapon.getImagesToDraw(screen);
//    }

    private final Weapon weapon;

    private long painTime, painTimeCounter;
    private int painLevel;

    @Override
    public void doSomething(long frameNanoTime) {
        weapon.doSomething(frameNanoTime);
        if (painLevel > 0) {
            painTimeCounter += frameNanoTime;
            painLevel = (int)Math.round(painLevel * (painTime - painTimeCounter) / painTime);
        }
    }

    public void fire(boolean on) {
        if (on) {
            weapon.fire();
        }
    }

    private double health = 100;

    @Override
    public void onGetDamage(double d) {
        int newPainLevel = (int) d * 10;
        if (newPainLevel > 255) newPainLevel = 255;
        if (newPainLevel > painLevel) {
            painLevel = newPainLevel;
            painTime = Math.round(d * 100000000l);
            painTimeCounter = 0;
        }
        health -= d;
    }

    @Override
    public void onInteractWith(WorldObject wo) {
        if (wo instanceof Ammo) {
            weapon.pickUpAmmo((Ammo) wo);
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
        return health > 0 ? health : 0;
    }

    public String getHealthString() {
        return String.format("HEALTH : %d%%", (int)getHealth());
    }

    public String getWeaponString() {
        return weapon.toString();
    }

    /**
     * @return the painLevel
     */
    public int getPainLevel() {
        return painLevel;
    }



}
