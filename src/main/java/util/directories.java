package util;

import java.io.File;

public class directories {

    public static void dirClear(String pathToDir) {
        if (pathToDir == null) {
            return;
        }
        File dir = new File(pathToDir);
        if (!dir.exists()) {
            return;
        }
        //проверено: listFiles() может быть null
        if (dir.listFiles() == null) {
            return;
        }
        for (File i : dir.listFiles()) {
            delete(i);
        }
    }

    private static void delete(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            directory_delete(file);
        } else {
            file.delete();
        }
    }

    private static void directory_delete(File cat) {
        if (cat == null || !cat.exists()) {
            return;
        }
        //проверено: listFiles() может быть null
        if (cat.listFiles() == null) {
            return;
        }
        for (File i : cat.listFiles()) {
            delete(i);
        }
        cat.delete();
    }
}
