package org.freeware.monakhov.game3d.objects.movable;

import org.freeware.monakhov.game3d.objects.movable.slugs.FireBall;
import java.util.List;
import org.freeware.monakhov.game3d.ScreenBuffer;
import org.freeware.monakhov.game3d.map.MultiImage;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;

/**
 * Главный герой
 * @author Vasily Monakhov
 */
public class Hero extends ViewPoint {

    /**
     * Создаёт героя
     * @param world мир
     * @param position позиция
     */
    public Hero(World world, Point position) {
        super(world, position);
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

    /**
     * изображение оружия в руках
     */
    MultiImage weapon = MultiImage.get("pistol");


    public List<MultiImage.ImageToDraw> getImagesToDraw(ScreenBuffer screen) {
        return weapon.getImagesToDraw(screen);
    }

     volatile long weaponTime = 100000000;

    @Override
    public void doSomething(long frameNanoTime) {
        weaponTime -= frameNanoTime;
    }

    public void fire(boolean on) {
        if (on) {
            if (weaponTime <= 0) {
                double azdelta = -0.1 * 0.2 * Math.random();
                world.addNewObject(new FireBall(world, new Point(position.getX(), position.getY()), this, azimuth + azdelta));
                weaponTime = 100000000;
            }
        }

    }

}
