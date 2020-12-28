package util;

import java.io.File;

public class directories {

    public static void RemoveDirectoryContent(File directory) {
        //проверено: listFiles() может быть null
        if (directory == null || !directory.exists() || directory.listFiles() == null) {
            return;
        }
        for (File i : directory.listFiles()) {
            delete(i);
        }
    }

    private static void delete(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            RemoveDirectoryContent(file);
        }
        file.delete();
    }
}
