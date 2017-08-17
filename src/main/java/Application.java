import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import path.Path;
import path.PathFoundIterator;
import path.SourceImage;
import utils.NativeUtils;
import detect.SquareDetection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Application {

    static {
        try {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                System.out.println("Include library: opencv_java320.dll");
                NativeUtils.loadLibraryFromJar("/opencv_java320.dll");
            } else {
                System.out.println("Include library: libopencv_java320.so");
                NativeUtils.loadLibraryFromJar("/libopencv_java320.so");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args == null || args.length < 1) {
            System.out.println("You should pass path as parameter.");
            return;
        }

        // Check path
        Path path = null;
        try {
            path = Path.create(args[0]);
        } catch (FileNotFoundException e) {
            System.out.println(String.format("File not found: %s", e.getMessage()));
            return;
        }

        PathFoundIterator pfi = path.files("asus_new");
        removeFolder(new File(pfi.path(), "found"));
        removeFolder(new File(pfi.path(), "notFound"));
        int all = pfi.size();
        int found = 0;
        int current = 0;

        while (pfi.hasNext()) {
            current++;
            SourceImage image = pfi.next();
            System.out.println(current + "/" + all + ": " + image.source());
            try {
                /*Mat mat = SquareDetection.detect(image.source());

                if (mat != null) {
                    Imgcodecs.imwrite(image.file("found"), mat);
                    found++;
                } else {
                    Imgcodecs.imwrite(image.file("notFound"), Imgcodecs.imread(image.source()));
                }*/
                if (SquareDetection.detect(image.source())) {
                    found++;
                }
            } catch (FileNotFoundException e) {}

        }

        System.out.println("All images: " + all);
        System.out.println("Found: " + found);
        System.out.println("Percent: " + (int)(((double)found / (double)all) * 100.0));
    }

    public static void removeFolder(File folder) {
        if (folder.exists()) {
            if (folder.isDirectory()) {
                for (File c : folder.listFiles()) {
                    removeFolder(c);
                }
            }
            folder.delete();
        }
    }
}