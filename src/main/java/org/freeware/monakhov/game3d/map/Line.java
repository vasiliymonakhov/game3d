package org.freeware.monakhov.game3d.map;

import java.awt.image.BufferedImage;
import org.freeware.monakhov.game3d.SpecialMath;

/**
 * Линия комнаты на карте
 *
 * @author Vasily Monakhov
 */
public class Line {

    /**
     * Начальная точка линии, должна находиться слева от линии взгляда
     * наблюдателя
     */
    protected final Point start;

    /**
     * Конечная точка линии, должна находиться слева от линии взгляда
     * наблюдателя
     */
    protected final Point end;
    
    /**
     * Эта линия может быть порталом в другую комнату
     */
    private Room portalTo;

    /**
     * Создаёт линию
     *
     * @param start начальная точка
     * @param end конечная точка
     */
    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Возвращает начало линии
     *
     * @return начало линии
     */
    public Point getStart() {
        return start;
    }

    /**
     * Возвращает конец линии
     *
     * @return конец линии
     */
    public Point getEnd() {
        return end;
    }

    /**
     * Устанавливает комнату, превращая линию в портал
     *
     * @param portalTo в какую комнату ведёт портал
     */
    public void setPortal(Room portalTo) {
        this.portalTo = portalTo;
    }

    /**
     * Возвращает комнату, в которую ведёт этот портал
     *
     * @return комната, в которую ведёт этот портал
     */
    public Room getRoomFromPortal() {
        return portalTo;
    }

    /**
     * Сообщает, видима ли эта линия
     *
     * @return true - линия видима
     */
    public boolean isVisible() {
        return false;
    }
    
    protected boolean everSeen = false;    
    
    public boolean isEverSeen() {
        return everSeen;
    }
    
    /**
     * Сообщает, что линию можно пересекать
     * @return можно ли пересекать через линию
     */
    public boolean isCrossable() {
        return true;
    }

    /**
     * Проверяет видимость линии на экране
     */
    boolean checkVisibility(Line[] mapLines, Point viewPoint, Point[] rayPoints, Point[] intersectPoints) {
        Point p = new Point();
        for (int i = 0; i < mapLines.length; i++) {
            if (SpecialMath.lineIntersection(start, end, rayPoints[i], viewPoint, p)) {
                if (p.between(start, end)) return true;
            }
        }
        return false;
    }

    /**
     * Проверяет, пересекает ли отрезок из друх точек эту линию
     *
     * @param a начало отрезка
     * @param b конец отрезка
     * @return true если пересекает
     */
    public boolean checkCross(Point a, Point b) {
        return SpecialMath.checkCross(a, b, start, end);
    }

    public BufferedImage getSubImage(Point p) {
        return null;
    }
    
}
