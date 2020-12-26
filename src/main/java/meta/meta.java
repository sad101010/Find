package meta;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import static meta.FAttr.add_file_attr;
import static meta.PDFBox.addPdfTags;
import static meta.Tika.get_mime;
import static meta.Tika.add_text_from_file;
import static meta.db.add_names;
import static meta.img.addImgNames;
import static meta.img.add_images_from_document;

public class meta {
    
    private static void addMetaFields(Map map, File file){
        switch (get_mime(file)) {
            case "image/jpeg":
            case "image/png":
                addImgNames(map, file);
                break;
            case "application/pdf":
                addPdfTags(file, map);
                break;
            default:
                add_names(map, file);
                break;
        }        
    }

    public static Map getSmartMeta(File file) {
        Map result = new TreeMap();
        if (expr.analyze.need_fs_attributes()) {
            add_file_attr(file, result);
        }
        if (expr.check.text != null) {
            add_text_from_file(result,file);
        }
        if (expr.analyze.need_img()) {
            add_images_from_document(result,file);
        }
        if (expr.analyze.need_metadata()) {
            addMetaFields(result, file);
        }
        return result;
    }

    public static Map get_meta_for_user(File file) {
        Map result = new TreeMap();
        addMetaFields(result, file);
        return result;
    }
}
