package detect;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

class SquareSet {

    private ArrayList<Square> _points;

    public static ArrayList<SquareSet> divideInTripleSets(ArrayList<Square> squares) {
        ArrayList<SquareSet> triples = new ArrayList<>();

        int size = squares.size();
        if (squares.size() < 3) {
            return triples;
        }

        for (int i = 0; i < size - 2; i++) {
            for (int j = i + 1; j < size - 1; j++) {
                for (int k = j + 1; k < size; k++) {
                    SquareSet triple = new SquareSet();
                    triple.add(squares.get(i));
                    triple.add(squares.get(j));
                    triple.add(squares.get(k));
                    triples.add(triple);
                }
            }

        }
        return triples;
    }

    public SquareSet() {
        _points = new ArrayList<>();
    }

    public void add(Square square) {
        _points.add(square);
    }

    public boolean check() {
        Square sqA = _points.get(0);
        Square sqB = _points.get(1);
        Square sqC = _points.get(2);

        // all squares has equals size
        double min = Math.min(sqA.getSideLength(), Math.min(sqB.getSideLength(), sqC.getSideLength()));
        double max = Math.max(sqA.getSideLength(), Math.max(sqB.getSideLength(), sqC.getSideLength()));
        double avg = min + max / 2;

        if (min * 1.5 < max) {
            return false;
        }

        // get centers
        Point cA = sqA.getCenter();
        Point cB = sqB.getCenter();
        Point cC = sqC.getCenter();

        // located on one line
        if (Geometry.cos(cA, cB, cC) < 0.99 || Geometry.cos(cA, cC, cB) < 0.99 || Geometry.cos(cB, cA, cC) < 0.99) {
            return false;
        }

        // Get distances between centers
        ArrayList<Double> sizes = new ArrayList<>();
        sizes.add(Geometry.distance(cA, cB));
        sizes.add(Geometry.distance(cA, cC));
        sizes.add(Geometry.distance(cC, cB));
        Collections.sort(sizes);

        // has middle
        if (Math.abs(sizes.get(0) - sizes.get(1)) > avg / 4) {
            return false;
        }

        // distance
        if (sizes.get(0) > avg * 2 || sizes.get(1) > avg * 2 || sizes.get(0) < max*1.5 || sizes.get(1) < max*1.5) {
            return false;
        }

        return true;
    }

    public ArrayList<MatOfPoint> getPoints() {
        ArrayList<MatOfPoint> points = new ArrayList<>();
        for (Square square : _points) {
            points.add(square.getPoint());
        }
        return points;
    }

    public ArrayList<Square> getSquares() {
        return _points;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof SquareSet) {
            SquareSet set = (SquareSet) obj;
            int found = 0;
            for (Square point : _points) {
                if (set.getSquares().contains(point)) {
                    found++;
                }
            }
            return found == 3;
        }
        return false;
    }
}
