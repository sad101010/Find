package meta;

import java.io.File;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import static util.DateBean.DateToString;

public class PDFBox {

    static void addPdfTags(File file, Map map) {
        PDDocument doc;
        try {
            doc = PDDocument.load(file);
        } catch (Exception | Error e) {
            return;
        }
        PDDocumentInformation info = doc.getDocumentInformation();
        map.put("Количество страниц", String.valueOf(doc.getNumberOfPages()));
        if (info.getTitle() != null) {
            map.put("Название", info.getTitle());
        }
        if (info.getAuthor() != null) {
            map.put("Автор", info.getAuthor());
        }
        if (info.getSubject() != null) {
            map.put("Тема", info.getSubject());
        }
        if (info.getKeywords() != null) {
            map.put("Ключевые слова", info.getKeywords());
        }
        if (info.getProducer() != null) {
            map.put("Имя программы", info.getProducer());
        }
        if (info.getCreationDate() != null) {
            map.put("Дата создания", DateToString(info.getCreationDate().getTime()));
        }
        if (info.getModificationDate() != null) {
            map.put("Дата последнего изменения", DateToString(info.getModificationDate().getTime()));
        }
        try {
            doc.close();
        } catch (Exception | Error e) {
        }
    }

}
