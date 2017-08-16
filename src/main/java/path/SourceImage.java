package path;

import java.io.File;

public class SourceImage {
    private String _source;
    private String _path;
    private String _filename;

    public SourceImage (File source, File path) {
        _filename = source.getName();
        _source = source.getAbsolutePath();
        _path = path.getPath();
    }

    public String source() {
        return _source;
    }

    private String folder(String folder) {
        File f = new File(_path, folder);
        if (f.exists() == false) {
            createPath(f);
        }
        return f.getAbsolutePath();
    }

    public String file(String folder) {
        return new File(folder(folder), _filename).getAbsolutePath();
    }

    public String file(String folder, int i) {
        return new File(folder(folder), i + "_" + _filename).getAbsolutePath();
    }

    private File createPath(File foundPath) {
        if (foundPath.exists()) {
            removeFile(foundPath);
        }
        foundPath.mkdir();
        return foundPath;
    }

    private void removeFile(File path) {
        if (path.isDirectory()) {
            for (File c : path.listFiles())
                removeFile(c);
        }
        path.delete();
    }
}
