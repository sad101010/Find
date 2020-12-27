package util;

import java.io.File;
import java.io.InputStream;
import java.time.Duration;
import java.util.Map;
import java.util.zip.ZipFile;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import static meta.db.mimedb;
import meta.type;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ODF {

    private static final Map<String, String> OdtNames = mimedb.get("application/vnd.oasis.opendocument.text");

    public static boolean AddOdtTags(File zipFile, Map<String, String> map) {
        //dc:date - неизвестный таг
        ZipFile zip;
        try {
            zip = new ZipFile(zipFile);
        } catch (Exception | Error e) {
            return false;
        }
        InputStream inputStream;
        try {
            inputStream = zip.getInputStream(zip.getEntry("meta.xml"));
        } catch (Exception | Error e) {
            return false;
        }
        if (!load_xml(inputStream, map)) {
            return false;
        }
        try {
            inputStream.close();
        } catch (Exception | Error e) {
            return false;
        }
        return true;
    }

    private static boolean load_xml(InputStream inputStream, Map<String, String> map) {
        Document document;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
        } catch (Exception | Error e) {
            return false;
        }
        Node node = document.getDocumentElement();
        while (node != null && !node.getNodeName().equals("office:document-meta")) {
            node = node.getNextSibling();
        }
        if (node == null) {
            return false;
        }
        node = node.getFirstChild();
        while (node != null && !node.getNodeName().equals("office:meta")) {
            node = node.getNextSibling();
        }
        if (node == null) {
            return false;
        }
        node = node.getFirstChild();
        while (node != null) {
            if (node.getNodeName().equals("meta:document-statistic")) {
                NamedNodeMap attr = node.getAttributes();
                for (int i = 0; i < attr.getLength(); i++) {
                    Node item = attr.item(i);
                    put(item.getNodeName(), item.getTextContent(), map);
                }
            } else {
                if (node.getNodeName().equals("meta:editing-duration")) {
                    Duration dur = Duration.parse(node.getTextContent());
                    TimeBean timeBean = TimeBean.valueOf(dur.toMillis() / 1000);
                    map.put("Время редактирования", timeBean.toString());
                } else {
                    put(node.getNodeName(), node.getTextContent(), map);
                }
            }
            node = node.getNextSibling();
        }
        return true;
    }

    private static void put(String name, String value, Map<String, String> map) {
        if (OdtNames.get(name) == null) {
            System.err.println("ODF: неизвестное имя " + name);
            return;
        }
        String rusName = OdtNames.get(name);
        Object obj = type.parseFieldValue(rusName, value);
        map.put(rusName, obj.toString());
    }
}
