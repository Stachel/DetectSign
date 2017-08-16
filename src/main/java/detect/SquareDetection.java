package detect;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import path.SourceImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class SquareDetection {

    public static boolean detect(String filename) throws FileNotFoundException {
        // Check file exists
        if ((new File(filename)).exists() == false) {
            throw new FileNotFoundException(filename);
        }

        // Load image source
        Mat image = Imgcodecs.imread(filename);

        // Create new Mat for gray scale image
        Mat gray = new Mat(image.size(), CvType.makeType(image.depth(), 1)); // To hold Grayscale Image

        // Convert original image to gray scale
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

        // Down scale and upscale the image to filter out the noise
        Imgproc.pyrDown(gray, gray, new Size(image.cols()/2, image.rows()/2));
        Imgproc.pyrUp(gray, gray, image.size());

        // Apply adaptiveThreshold filter (it can be long)
        // in some cases blockSize=115 is more helpful
        Imgproc.adaptiveThreshold(gray, gray, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 15, 1);

        // Detect all edges on image (Canny algorithm)
        Imgproc.Canny(gray, gray, 50 , 700, 3, false);
        Imgproc.dilate(gray, gray, new Mat());

        // Find all contours
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(gray, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE); // Find contours with hierarchy

        ArrayList<Square> squares = new ArrayList<>();
        for(MatOfPoint point : contours) {
            Square square = Square.createIfSquare(point, image.size());
            if (square != null) {
                squares.add(square);
            }
        }

        // Divide all squares to sets by three squares and check every triple

        // Check every set
        ArrayList<SquareSet> sets = new ArrayList<>();
        for (SquareSet set : SquareSet.divideInTripleSets(squares)) {
            if (set.check() && sets.contains(set) == false) {
                sets.add(set);
            }
        }

        if (sets.size() > 0) {
            /*DEBUG  Cut square from image
            ArrayList<Point> points = new ArrayList<>();
            for (SquareSet set : sets) {
                for (int k = 0; k < 3; k++) {
                    points.addAll(set.getPoints().get(k).toList());
                }
            }
            Rect rect = Imgproc.boundingRect(new MatOfPoint(points.toArray(new Point[0])));
            Mat temp = image.submat(rect);
            */


            /*DEBUG Draw squares on image
            Mat temp = new Mat(image.size(), CvType.makeType(image.depth(), 1));
            image.copyTo(temp);
            for (SquareSet set : sets) {
                for (int k = 0; k < set.getPoints().size(); k++) {
                    Imgproc.drawContours(temp, set.getPoints(), k, new Scalar(0, 0, 255), 3);
                }
            }*/
            return true;
        }

        return false;
    }

    public static String toString(double[] arr) {
        StringBuffer sb = new StringBuffer();
        for( int i = 0; i < arr.length; i++ ) {
            sb.append(arr[i]);
            sb.append(" ");
        }
        return sb.toString();
    }

    public static String toString(Point p) {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        sb.append(p.x);
        sb.append(", ");
        sb.append(p.y);
        sb.append(")");

        return sb.toString();
    }

    public static String toString(MatOfPoint p) {
        StringBuffer sb = new StringBuffer();
        for (Point point : p.toArray()) {
            sb.append(toString(point));
            sb.append("; ");
        }

        return sb.toString();
    }
}
