package path;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class PathFoundIterator implements Iterator<SourceImage> {
    private File _path;

    private ArrayList<File> _files;
    private int _current;

    public PathFoundIterator(File path) {
        _path = path;

        _files = new ArrayList<>();
        for (File file : path.listFiles()) {
            if (file.isFile()) {
                _files.add(file);
            }
        }
    }

    @Override
    public boolean hasNext() {
        return _files.size() > _current;
    }

    @Override
    public SourceImage next() {
        File file = _files.get(_current);
        SourceImage image = new SourceImage(file, _path);

        _current++;
        return image;
    }

    public int size() {
        return _files.size();
    }

    public File path() {
        return _path;
    }
}
