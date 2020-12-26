package containers;

import swing.button.transparent_button;
import swing.useful_container;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static swing.util.*;
import expr.check;
import java.util.Map;
import static meta.map.mime_map;
import org.joda.time.chrono.StrictChronology;
import swing.dialog;

public class mime_select extends useful_container {

    private final JComboBox box = new JComboBox();
    private final transparent_button ok = new transparent_button();
    private final String __mime[] = new String[mime_map.size()];
    private final String __ext[] = new String[mime_map.size()];

    public mime_select(dialog owner) {
        super(owner);

        int i = 0;
        for (Map.Entry<String, String> e : mime_map.entrySet()) {
            __mime[i] = e.getKey();
            __ext[i] = e.getValue();
            i++;
        }

        box.setModel(new DefaultComboBoxModel(__ext));
        if (check.mime == null) {
            for (i = 0; i < __mime.length; i++) {
                if (__mime[i].equals("Не задан")) {
                    box.setSelectedIndex(i);
                }
            }
        }else{
            for (i = 0; i < __mime.length; i++) {
                if (check.mime.equals(__mime[i])) {
                    box.setSelectedIndex(i);
                }
            }
        }
        setxy(box, 3, 3);
        fit(box);
        add(box);

        ok.setIcon(new ImageIcon("data/www.apache.org/hand.right.png"));
        ok.setToolTipText("Выбрать");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ok_clicked();
            }
        });
        append_middle_bottom(ok, box);
        add(ok);

        //убрать фокусы, плохо смотрится
        for (Component c : getComponents()) {
            c.setFocusable(false);
        }
    }

    private void ok_clicked() {
        check.mime = box.getSelectedItem().equals("Не задан") ? null : __mime[box.getSelectedIndex()];
        owner.dispose();
    }
}
