package org.freeware.monakhov.game3d.map;

import org.xml.sax.Attributes;

/**
 * Точка на карте
 * @author Vasily Monakhov 
 */
public class Point {
    
    /**
     * Координаты точки
     */
    private double x, y;
    
    /**
     * Создаёт точку
     * @param attr аттрибуты точки в XML-файле
     */
    public Point(Attributes attr) {
        this(Double.parseDouble(attr.getValue("x")), Double.parseDouble(attr.getValue("y")));
    }
    
    /**
     * Создаёт точку
     * @param x X-координата точки
     * @param y Y-координата точки
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Создаёт точку на карте
     */
    public Point() {
    }    

    /**
     * Перемещает точку в новые координаты
     * @param x новая X-координата точки
     * @param y новая Y-координата точки
     */
    public void moveTo(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Перемещает точку на заданное смещение
     * @param sx смещение по оси X
     * @param sy смещение по оси Y
     */
    public void moveBy(double sx, double sy) {
        this.x += sx;
        this.y += sy;        
    }    
    
    /**
     * Возвращает X-координату точки
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * Возвращает Y-координату точки
     * @return the y
     */
    public double getY() {
        return y;
    }
    
     final static double EPSILON = 0.0001d;    
    
    /**
     * Определяет, лежит ли точка между двумя другими
     * @param pa первая точка
     * @param pb вторая точка
     * @return true если да
     */
    public boolean between(Point pa, Point pb) {
        double xmax = pa.x >  pb.x ? pa.x + EPSILON : pb.x + EPSILON;
        double xmin = pa.x < pb.x ?  pa.x - EPSILON : pb.x - EPSILON;
        double ymax = pa.y > pb.y ? pa.y + EPSILON : pb.y + EPSILON;
        double ymin = pa.y < pb.y ? pa.y - EPSILON : pb.y - EPSILON;
        return x <= xmax && x >= xmin && y <= ymax && y >= ymin;
    }

}
