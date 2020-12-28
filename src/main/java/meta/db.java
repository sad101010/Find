package meta;

import util.breader;
import java.io.File;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import static meta.map.mime_map;
import static meta.type.getFieldType;
import util.DateBean;
import static util.directories.dirClear;
import static util.util.exit_with_err_msg;

public class db {

    public static final Map<String, Map<String, String>> mimedb = new TreeMap<String, Map<String, String>>();
    private static int cat_length;

    static {
        File cat = new File("data/mimes");
        cat.mkdirs();
        if (!cat.exists()) {
            exit_with_err_msg("Ошибка инициализации БД", null);
        }
        try {
            cat_length = cat.getCanonicalPath().length();
        } catch (Exception | Error e) {
            exit_with_err_msg("err in meta.mime.db mimedb load", null);
        }
        load_mimes_brute(cat);
    }

    private static void load_mimes_brute(File file) {
        if (file.isDirectory()) {
            dir_brute(file);
        } else {
            load_names_from_file(file);
        }
    }

    private static void dir_brute(File f) {
        //проверено: listFiles() может быть null
        if (f.listFiles() == null) {
            return;
        }
        for (File i : f.listFiles()) {
            load_mimes_brute(i);
        }
    }

    private static void load_names_from_file(File f) {
        breader reader = null;
        try {
            reader = new breader(f);
        } catch (Exception | Error e) {
            exit_with_err_msg("Ошибка чтения имен", null);
        }
        String mime = null;
        try {
            mime = f.getCanonicalPath().replace('\\', '/');
        } catch (Exception | Error e) {
            exit_with_err_msg("Ошибка чтения имен: ошибка получеия имени файла", null);
        }
        mime = mime.substring(cat_length + 1);
        if (!mime_map.containsKey(mime)) {
            return;
        }
        Map<String, String> names = new TreeMap<>();
        mimedb.put(mime, names);
        while (true) {
            String line1 = null, line2 = null;
            try {
                line1 = reader.readLine();
                line2 = reader.readLine();
            } catch (Exception | Error e) {
                exit_with_err_msg("Ошибка чтения базы", null);
            }

            if (line1 == null || line2 == null) {
                break;
            }
            names.put(line1, line2);
        }
        try {
            reader.close();
        } catch (Exception | Error e) {
            exit_with_err_msg("Ошибка закрытия breader", null);
        }
    }

    public static void save_mimedb() {
        dirClear("data/mimes");
        for (String mime : mimedb.keySet()) {
            File file = new File("data/mimes/" + mime);
            file.getParentFile().mkdirs();
            PrintWriter writer;
            try {
                writer = new PrintWriter(file, "UTF-8");
            } catch (Exception | Error e) {
                continue;
            }
            for (Map.Entry<String, String> e : mimedb.get(mime).entrySet()) {
                if (e.getKey().equals(e.getValue())) {
                    continue;
                }
                writer.println(e.getKey());
                writer.println(e.getValue());
            }
            writer.close();
        }
    }

    public static String[] rusNames(String mime) {
        Set<String> set = new TreeSet<>();
        Map<String, String> names = mimedb.get(mime);
        for (String k : names.keySet()) {
            String v = names.get(k);
            if (!k.equals(v)) {
                set.add(v);
            }
        }
        String[] result = new String[set.size()];
        Object objects[] = set.toArray();
        for (int i = 0; i < set.size(); i++) {
            result[i] = (String) objects[i];
        }
        return result;
    }
}
