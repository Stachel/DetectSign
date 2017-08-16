package detect;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

class Square {

    private MatOfPoint _obj;

    private Point _center;
    private double _sideLength = -1;

    private Square(MatOfPoint obj) {
        _obj = obj;
    }

    public static Square createIfSquare(MatOfPoint contour, Size imageSize) {
        // approximate contour
        MatOfPoint2f contour2F = new MatOfPoint2f(contour.toArray());
        MatOfPoint2f approx = new MatOfPoint2f();
        Imgproc.approxPolyDP(contour2F, approx, Imgproc.arcLength(contour2F, true) * 0.05, true);

        // Calc min size of contour
        double squareSize = Math.min(imageSize.width, imageSize.height) / 4;

        Point[] points = approx.toArray();
        MatOfPoint approxPoint = new MatOfPoint(points);

        // count of edges = 4
        // size of contour more than minSize
        // contour is convex
        if (points.length == 4 && Math.abs(Imgproc.contourArea(approx)) > squareSize && Imgproc.isContourConvex(approxPoint) ) {
            // all corners is around 90 degrees
            // Width is equals to height
            if(isCornersRight(points) && isWidthEqualsHeight(approx)) {
                return new Square(approxPoint);
            }
        }
        return null;
    }

    private static boolean isCornersRight(Point[] points) {
        // find the maximum cosine of the angle between joint edges
        double maxCosine = 0;
        for( int j = 2; j < 5; j++ ) {
            double cosine = Math.abs(Geometry.cos(points[j%4], points[j-2], points[j-1]));
            maxCosine = Math.max(maxCosine, cosine);
        }
        return maxCosine < 0.3;
    }

    private static boolean isWidthEqualsHeight(MatOfPoint2f approx) {
        RotatedRect rr = Imgproc.minAreaRect(approx);
        Size size = rr.size;
        double epsilon = Math.min(size.width, size.height);
        return Math.abs(size.width - size.height) < epsilon;
    }

    @Deprecated
    public MatOfPoint getPoint() {
        return _obj;
    }

    public Point getCenter() {
        if (_center == null) {
            Moments moment = Imgproc.moments(_obj);
            _center = new Point();
            _center.x = moment.get_m10() / moment.get_m00();
            _center.y = moment.get_m01() / moment.get_m00();
        }
        return _center;
    }

    public double getSideLength() {
        if (_sideLength < 0) {
            MatOfPoint2f contour2F = new MatOfPoint2f(_obj.toArray());
            RotatedRect rr = Imgproc.minAreaRect(contour2F);
            Size size = rr.size;
            _sideLength = (size.width + size.height) / 2;
        }
        return _sideLength;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Square) {
            Square other = (Square) obj;
            Point center = other.getCenter();
            double sideLength = other.getSideLength();

            double min = Math.min(getSideLength(), sideLength);

            return Geometry.distance(getCenter(), center) < min / 4 && Math.abs(_sideLength - sideLength) < min / 2;
        }
        return false;
    }
}
