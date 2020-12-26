package containers;

import expr.check;
import java.awt.Font;
import swing.button.transparent_button;
import swing.useful_container;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import swing.dialog;
import static swing.util.*;

public class context_container extends useful_container {

    public context_container(dialog owner) {
        super(owner);
        JLabel label = new JLabel("Введите текст, который должен содержать искомый документ");
        add(label);
        fit(label);
        setxy(label, 3, 3);

        final JTextArea area = new JTextArea(check.text);
        area.setFont(new Font("Arial Unicode", Font.PLAIN, 14));

        JScrollPane pane = new JScrollPane(area);
        add(pane);
        setsize(pane, label.getWidth(), 200);
        append_bottom(pane, label);

        transparent_button ok = new transparent_button();
        ok.setToolTipText("Выбрать");
        ok.setIcon(new ImageIcon("data/www.apache.org/hand.right.png"));
        add(ok);
        append_middle_bottom(ok, pane);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String userText = area.getText();
                check.text = userText.isEmpty() ? null : userText;
                owner.dispose();
            }
        });
    }
}
