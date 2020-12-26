package containers.crit;

import static util.util.stringIsBlank;
import com.mycompany.Find;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JTextArea;

public class CritTextArea extends JTextArea {

    {
        setFont(new Font("Arial Unicode", Font.PLAIN, 14));
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                JTextArea textArea = (JTextArea) e.getSource();
                Point pt = new Point(e.getX(), e.getY());
                set_area_tooltip(textArea.viewToModel(pt));
            }
        });
    }

    private void set_area_tooltip(int pos) {
        String txt = getText();
        if (stringIsBlank(txt)) {
            setToolTipText(null);
            return;
        }
        --pos;
        if (pos == txt.length() - 1 || pos == -1) {
            setToolTipText(null);
            return;
        }
        int i, j;
        for (i = pos; i >= Math.max(0, pos - 5); i--) {
            if (txt.charAt(i) == '[') {
                break;
            }
        }
        for (j = pos; j < Math.min(txt.length(), pos + 5); j++) {
            if (txt.charAt(j) == ']') {
                break;
            }
        }
        boolean flag = i >= 0 && j < txt.length() && txt.charAt(i) == '[' && txt.charAt(j) == ']';
        if (!flag) {
            setToolTipText(null);
            return;
        }
        int img_id;
        try {
            img_id = Integer.parseInt(txt.substring(i + 1, j));
        } catch (Exception | Error e) {
            return;
        }
        String cutPath = Find.ThumbsDB.getPathById(img_id);
        if (cutPath.length() > 14) {
            cutPath = "..." + cutPath.substring(cutPath.length() - 14);
        }
        setToolTipText("<html><body>" + cutPath + "<br><img src=\"file:data/thumbnails/" + txt.substring(i + 1, j) + ".png\"></body></html>");
    }
}
