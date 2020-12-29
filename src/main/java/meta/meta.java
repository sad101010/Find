package meta;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import static meta.ApachePOI.AddDocTags;
import static meta.FAttr.addFTags;
import static meta.PDFBox.addPdfTags;
import static meta.docxTags.AddDocxTags;
import static meta.DrewNoakes.addImgTags;
import static meta.DrewNoakes.add_images_from_document;
import static util.util.get_mime;

public class meta {

    private static void addTags(Map map, File file) {
        switch (get_mime(file)) {
            case "image/jpeg":
            case "image/png":
                addImgTags(map, file);
                break;
            case "application/pdf":
                addPdfTags(file, map);
                break;
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                AddDocxTags(file, map);
                break;
            case "application/msword":
                AddDocTags(file, map);
                break;
        }
    }

    public static Map getSmartMeta(File file) {
        Map result = new TreeMap();
        if (expr.analyze.need_fs_attributes()) {
            addFTags(file, result);
        }
        if (expr.check.text != null) {
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //add_text_from_file(result, file);
        }
        if (expr.analyze.need_img()) {
            add_images_from_document(result, file);
        }
        if (expr.analyze.need_metadata()) {
            addTags(result, file);
        }
        return result;
    }

    public static Map get_meta_for_user(File file) {
        Map result = new TreeMap();
        addTags(result, file);
        return result;
    }
}
