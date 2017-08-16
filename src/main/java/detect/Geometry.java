package detect;

import org.opencv.core.Point;

class Geometry {

    public static double cos(Point pt1, Point pt2, Point pt0 ) {
        double dx1 = pt1.x - pt0.x;
        double dy1 = pt1.y - pt0.y;
        double dx2 = pt2.x - pt0.x;
        double dy2 = pt2.y - pt0.y;
        double angle = (dx1*dx2 + dy1*dy2)/Math.sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2) + 1e-10);
        return Math.abs(angle);
    }

    public static double distance(Point P, Point Q) {
        return Math.sqrt(Math.pow((P.x - Q.x), 2) + Math.pow((P.y - Q.y), 2)) ;
    }
}
