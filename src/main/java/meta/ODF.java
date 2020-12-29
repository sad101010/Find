package meta;

import java.io.File;
import java.io.InputStream;
import java.time.Duration;
import java.util.Map;
import java.util.zip.ZipFile;
import javax.xml.parsers.DocumentBuilderFactory;
import static meta.db.mimedb;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import util.TimeBean;

public class ODF {

    private static final Map<String, String> OdtNames = mimedb.get("application/vnd.oasis.opendocument.text");

    public static boolean AddOdtTags(File zipFile, Map<String, String> map) {
        ZipFile zip;
        try {
            zip = new ZipFile(zipFile);
        } catch (Exception | Error e) {
            return false;
        }
        return load_xml(zip, "meta.xml", map);
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
        //можно аккуратнее
        Node node;
        try {
            node = document.getDocumentElement().getFirstChild().getFirstChild();
        } catch (Exception | Error e) {
            return false;
        }
        if (!node.getParentNode().getParentNode().getNodeName().equals("office:document-meta") || !node.getParentNode().getNodeName().equals("office:meta")) {
            return false;
        }
        while (node != null) {
            if (node.getNodeName().equals("meta:document-statistic")) {
                NamedNodeMap attr = node.getAttributes();
                for (int i = 0; i < attr.getLength(); i++) {
                    Node item = attr.item(i);
                    if (!put(item.getNodeName(), item.getTextContent(), map)) {
                        return false;
                    }
                }
            } else {
                if (node.getNodeName().equals("meta:editing-duration")) {
                    map.put("Время редактирования", TimeBean.valueOf(Duration.parse(node.getTextContent()).toMillis() / 1000).toString());
                } else {
                    if (!put(node.getNodeName(), node.getTextContent(), map)) {
                        return false;
                    }
                }
            }
            node = node.getNextSibling();
        }
        return true;
    }

    private static boolean put(String name, String value, Map<String, String> map) {
        String rusName = OdtNames.get(name);
        if (rusName == null) {
            return false;
        }
        Object obj = type.parseFieldValue(rusName, value);
        if (obj == null) {
            return false;
        }
        map.put(rusName, obj.toString());
        return true;
    }
}
