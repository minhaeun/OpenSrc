package search_engine;

import java.io.File;

public class ReadFile {
    public File[] OpenFile() {
        File path = new File("src/data");
        File[] fileList = path.listFiles();
        return fileList;
    }
}
