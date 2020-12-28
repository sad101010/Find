package meta;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import util.DateBean;

public class PDFBox {

    public static ArrayList<BufferedImage> img_from_pdf(File file) {
        PDDocument document;
        try {
            document = PDDocument.load(file);
        } catch (Exception | Error e) {
            System.err.println("err img_from_pdf");
            return null;
        }
        ArrayList res = new ArrayList();
        for (PDPage page : document.getPages()) {
            PDResources resources = page.getResources();
            for (COSName name : resources.getXObjectNames()) {
                PDXObject object;
                try {
                    object = resources.getXObject(name);
                } catch (Exception | Error e) {
                    continue;
                }
                if (object instanceof PDImageXObject) {
                    PDImageXObject image = (PDImageXObject) object;
                    try {
                        res.add(image.getImage());
                    } catch (Exception | Error e) {
                    }
                }
            }
        }
        try {
            document.close();
        } catch (Exception | Error e) {
            System.err.println("Ошибка закрытия pdf-документа");
        }
        return res;
    }

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
            map.put("Дата создания", DateBean.valueOf(info.getCreationDate().getTime()).toString());
        }
        if (info.getModificationDate() != null) {
            map.put("Дата последнего изменения", DateBean.valueOf(info.getModificationDate().getTime()).toString());
        }
        try {
            doc.close();
        } catch (Exception | Error e) {
        }
    }

    public static String getPDFText(File file) {
        PDFTextStripper pdfStripper;
        try {
            pdfStripper = new PDFTextStripper();
        } catch (IOException ex) {
            return null;
        }
        pdfStripper.setStartPage(1);
        PDDocument doc;
        try {
            doc = PDDocument.load(file);
        } catch (IOException ex) {
            return null;
        }
        pdfStripper.setEndPage(doc.getNumberOfPages());
        try {
            return pdfStripper.getText(doc);
        } catch (IOException ex) {
            return null;
        }
    }

}
