package org.freeware.monakhov.game3d;

import org.freeware.monakhov.game3d.map.Point;

/**
 * Класс для специальной математики
 * @author Vasily Monakhov 
 */
public class SpecialMath {

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
    
    public static int lineAndCircleIntersection(Point p1, Point p2, Point o, double r, Point pr1, Point pr2) {
        double a = p2.getY() - p1.getY();
        double b = p1.getX() - p2.getX();
        double c = -p1.getX() * (p2.getY() - p1.getY()) + p1.getY() * (p2.getX() - p1.getX());        
        c = a * o.getX() + b * o.getY() + c;
        double a2b2 = a * a + b * b;
        double tx = -a * c / a2b2;
        double ty = -b * c / a2b2;
        double c2 = c * c;
        double r2a2b2 = r * r * (a * a + b * b);
        
        if (c2 > r2a2b2 + EPSILON) {
            return 0;
        } else if (Math.abs(c2 - r2a2b2) < EPSILON) {
            pr1.moveTo(tx + o.getX(), ty + o.getY());
            return 1;
        } else {
            double d = r * r - c * c / a2b2;
            double mult = Math.sqrt(d / a2b2);
            pr1.moveTo(tx + b * mult + o.getX(), ty - a * mult + o.getY());
            pr2.moveTo(tx - b * mult + o.getX(), ty + a * mult + o.getY());
            return 2;
        }
        
    }
    
    public static double angleBetweenLines(Point p1, Point p2, Point p3, Point p4) {
        double a1 = p2.getY() - p1.getY();
        double b1 = p1.getX() - p2.getX();
        double c1 = -p1.getX() * (p2.getY() - p1.getY()) + p1.getY() * (p2.getX() - p1.getX());
        
        double a2 = p4.getY() - p3.getY();
        double b2 = p3.getX() - p4.getX();
        double c2 = -p3.getX() * (p4.getY() - p3.getY()) + p3.getY() * (p4.getX() - p3.getX());
        
        double aabb = a1 * a2 + b1 * b2;
        
        if (aabb < EPSILON) {
            return Math.PI / 4;
        }
        return Math.atan((a1 * b2 - a2 * b1) / aabb);
    }
    
   
}
