package meta;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import static util.DateBean.DateToString;

public class FAttr {

    public static final String FAttrFields[] = {
        "Имя файла",
        "Размер файла в байтах",
        "Расширение файла",
        "Дата создания файла",
        "Дата последнего доступа к файлу",
        "Дата последнего изменения файла"
    };

    static Map add_file_attr(File file, Map result) {
        result.put("Размер файла в байтах", String.valueOf(file.length()));
        String name = file.getName();
        result.put("Имя файла", name);
        int j = name.lastIndexOf('.');
        if (j != -1) {
            result.put("Расширение файла", name.substring(j + 1));
        } else {
            result.put("Расширение файла", "отсутствует");
        }
        BasicFileAttributes attr;
        try {
            attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        } catch (Exception | Error e) {
            return result;
        }
        if (attr.creationTime() != null) {
            String s = FileTimeToString(attr.creationTime());
            if (s != null) {
                result.put("Дата создания файла", s);
            }
        }
        if (attr.lastAccessTime() != null) {
            String s = FileTimeToString(attr.lastAccessTime());
            if (s != null) {
                result.put("Дата последнего доступа к файлу", s);
            }
        }
        if (attr.lastModifiedTime() != null) {
            String s = FileTimeToString(attr.lastModifiedTime());
            if (s != null) {
                result.put("Дата последнего изменения файла", s);
            }
        }
        return result;
    }

    private static String FileTimeToString(FileTime time) {
        if (time == null) {
            return null;
        }
        Date date = new Date(time.toMillis());
        return DateToString(date);
    }

    public static Map get_file_attr(File file) {
        Map result = new TreeMap();
        add_file_attr(file, result);
        return result;
    }

}
