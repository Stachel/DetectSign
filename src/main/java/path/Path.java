package path;

import java.io.File;
import java.io.FileNotFoundException;

public class Path {

    private File _path;

    private Path (File path) {
        _path = path;
    }

    public static Path create(String file) throws FileNotFoundException {
        File path = new File(file);

        if (path.exists() == false) {
            throw new FileNotFoundException(path.getAbsolutePath());
        }

        return new Path(path);
    }


    public PathFoundIterator files(String folder) {
        File path = new File(_path, folder);
        if (path.exists() == false) {
            path.mkdir();
        }

        System.out.println(path.getAbsolutePath());

        return new PathFoundIterator(path);
    }
}
