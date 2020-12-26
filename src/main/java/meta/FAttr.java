package meta;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import util.DateBean;

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
        DateBean dateBean = DateBean.valueOf(attr.creationTime());
        if (dateBean != null) {
            result.put("Дата создания файла", dateBean.toString());
        }
        dateBean = DateBean.valueOf(attr.lastAccessTime());
        if (dateBean != null) {
            result.put("Дата последнего доступа к файлу", dateBean.toString());
        }
        dateBean = DateBean.valueOf(attr.lastModifiedTime());
        if (dateBean != null) {
            result.put("Дата последнего изменения файла", dateBean.toString());
        }
        return result;
    }

    public static Map get_file_attr(File file) {
        Map result = new TreeMap();
        add_file_attr(file, result);
        return result;
    }

}
