package org.freeware.monakhov.game3d;

import org.freeware.monakhov.game3d.maps.Point;

/**
 * Класс для специальной математики
 * @author Vasily Monakhov 
 */
public class SpecialMath {

    private static double area(Point a, Point b, Point c) {
        return (b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a.getY()) * (c.getX() - a.getX());
    }

    public static boolean checkCross(Point a, Point b, Point c, Point d) {
        return intersect(a.getX(), b.getX(), c.getX(), d.getX()) && intersect(a.getY(), b.getY(), c.getY(), d.getY()) && area(a, b, c) * area(a, b, d) <= 0 && area(c, d, a) * area(c, d, b) <= 0;
    }
    
    private static boolean intersect(double a, double b, double c, double d) {
        if (a > b) {
            double t = a;
            a = b;
            b = t;
        }
        if (c > d) {
            double t = c;
            c = d;
            d = t;
        }
        return Math.max(a, c) <= Math.min(b, d);
    }

    public static double lineLength(Point a, Point b) {
        double x = a.getX() - b.getX();
        double y = a.getY() - b.getY();
        return Math.sqrt(x * x + y * y);
    }

    public static double triangleSquare(Point a, Point b, Point c) {
        double ab = lineLength(a, b);
        double bc = lineLength(b, c);
        double ac = lineLength(a, c);
        double p = (ab + bc + ac) / 2;
        return Math.sqrt(p * (p - ab) * (p - bc) * (p - ac));
    }

    public final static double EPSILON = 0.0001d;      
    
    public static boolean lineIntersection (Point p1, Point p2, Point p3, Point p4, Point i) {
        double a1 = p2.getY() - p1.getY();
        double b1 = p1.getX() - p2.getX();
        double c1 = -p1.getX() * (p2.getY() - p1.getY()) + p1.getY() * (p2.getX() - p1.getX());
        
        double a2 = p4.getY() - p3.getY();
        double b2 = p3.getX() - p4.getX();
        double c2 = -p3.getX() * (p4.getY() - p3.getY()) + p3.getY() * (p4.getX() - p3.getX());
        
        double d = a1 * b2 - b1 * a2;
        if (Math.abs(d) < EPSILON) {
            return false;
        }
        double dx = -c1 * b2 + b1 * c2;
        double dy = -a1 * c2 + c1 * a2;
        i.moveTo(dx / d, dy / d);
        return true;        
    }    
    
   
}
