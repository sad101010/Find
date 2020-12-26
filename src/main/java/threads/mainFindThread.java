package threads;

import static com.mycompany.Find.MainCatalog;
import static com.mycompany.Find.ThumbsDB;
import java.awt.Window;
import java.io.File;
import java.io.PrintWriter;
import swing.button.png_roll;
import static util.util.BadOrSymLink;
import static util.util.current_time;
import static util.util.err_msg;
import static util.util.getPath;
import static util.util.msg;

public class mainFindThread extends basicThread {

    private final png_roll button;
    private final Window owner;
    private PrintWriter search_results;

    public mainFindThread(png_roll button, Window owner) {
        this.button = button;
        this.owner = owner;
    }

    @Override
    public void run() {
        System.err.println(">" + current_time() + "  find thread start");
        try {
            search_results = new PrintWriter("data/results.txt", "UTF-8");
        } catch (Exception | Error e) {
            err_msg("Не удалось создать файл для результатов поиска", owner);
            return;
        }
        brute(MainCatalog);
        button.__switch(1);
        msg("Поиск завершен", owner);
        System.err.println(">" + current_time() + "  find thread finish");
    }

    private void brute(File f) {
        if (!getRunnin() || BadOrSymLink(f)) {
            return;
        }
        if (f.isDirectory()) {
            dir_brute(f);
        } else {
            check(f);
        }
    }

    private void dir_brute(File f) {
        //проверено: listFiles() может быть null
        if (f.listFiles() == null) {
            return;
        }
        for (File i : f.listFiles()) {
            brute(i);
        }
    }

    private void check(File f) {
        if (!expr.check.check(f)) {
            return;
        }
        String path = getPath(f);
        if (path == null) {
            return;
        }
        search_results.println(path);
        search_results.flush();
        ThumbsDB.add(path);
    }
}
