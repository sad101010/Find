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
import swing.dialog;

public class mime_select extends useful_container {

    private final JComboBox box = new JComboBox();
    private final transparent_button ok = new transparent_button();
    private final String __mime[] = new String[mime_map.size() + 1];
    private final String __ext[] = new String[mime_map.size() + 1];

    public mime_select(dialog owner) {
        super(owner);

        __mime[0] = null;
        __ext[0] = "Не задан";
        int i = 1;
        for (Map.Entry<String, String> e : mime_map.entrySet()) {
            __mime[i] = e.getKey();
            __ext[i] = e.getValue();
            i++;
        }

        box.setModel(new DefaultComboBoxModel(__ext));
        if (check.mime == null) {
            box.setSelectedIndex(0);
        } else {
            for (i = 1; i < __mime.length; i++) {
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
                check.mime = __mime[box.getSelectedIndex()];
                owner.dispose();
            }
        });
        append_middle_bottom(ok, box);
        add(ok);

        //убрать фокусы, плохо смотрится
        for (Component c : getComponents()) {
            c.setFocusable(false);
        }
    }
}
