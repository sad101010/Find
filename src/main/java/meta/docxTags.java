package meta;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.ZipFile;
import javax.xml.parsers.DocumentBuilderFactory;
import static meta.db.mimedb;
import static meta.type.parseFieldValue;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import util.TimeBean;

public class docxTags {

    private static final Map<String, String> DocxNames = mimedb.get("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

    public static boolean AddDocxTags(File file, Map<String, String> map) {
        ZipFile zip;
        try {
            zip = new ZipFile(file);
        } catch (Exception | Error e) {
            return false;
        }
        return load_xml(zip, "docProps/core.xml", map) && load_xml(zip, "docProps/app.xml", map);
    }

    private static boolean load_xml(ZipFile zip, String filename, Map<String, String> map) {
        Document document;
        try {
            InputStream inputStream = zip.getInputStream(zip.getEntry(filename));
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
            inputStream.close();
        } catch (Exception | Error e) {
            return false;
        }
        //нет проверки на имя тага
        Node node = document.getDocumentElement().getFirstChild();
        while (node != null) {
            String name = DocxNames.get(node.getNodeName());
            if (name == null) {
                return false;
            }
            Object value;
            if (name.equals("Время редактирования")) {
                value = TimeBean.valueOf(Long.valueOf(node.getTextContent()));
            } else {
                value = parseFieldValue(name, node.getTextContent());
            }
            if (value == null) {
                return false;
            }
            map.put(name, value.toString());
            node = node.getNextSibling();
        }
        return true;
    }
}
