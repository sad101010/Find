package containers.crit;

import swing.button.transparent_button;
import swing.dialog;
import swing.useful_container;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static swing.util.*;

public class crit extends useful_container {

    private final transparent_button dir;
    private final CritContextButton context;
    private final transparent_button fs_attributes;
    private final MimeButton mime_choose;
    private final CritResultsButton results;
    private final AddButton addButton;
    private final CritTextArea area = new CritTextArea();
    private final JScrollPane pane = new JScrollPane(area);
    private final CritFindButton find;
    protected final JLabel dir_label = new JLabel(" ");
    private final transparent_button or = new transparent_button();
    private final transparent_button and = new transparent_button();

    public crit(dialog owner) {
        super(owner);

        dir = new CritDirButton(dir_label, owner);
        results = new CritResultsButton(owner);
        find = new CritFindButton(owner, area);
        fs_attributes = new CritFAttrButton(owner, area);
        context = new CritContextButton(owner);
        mime_choose = new MimeButton(owner);
        addButton = new AddButton(owner, area);
        or.setText("ИЛИ");
        fit(or);
        or.setSize(or.getWidth() + 5, 30);
        or.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                area.insert("| ",area.getCaretPosition());
            }
        });

        and.setText("И");
        fit(and);
        and.setSize(and.getWidth() + 5, 30);
        and.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                area.insert("& ",area.getCaretPosition());
            }
        });

        setxy(dir, 3, 3);
        add(dir);

        setsize(pane, 400, 200);
        append_bottom(pane, dir);
        add(pane);

        setsize(dir_label, pane.getWidth(), dir_label.getPreferredSize().height);
        append_bottom(dir_label, pane);
        add(dir_label);

        append_right(fs_attributes, dir);
        add(fs_attributes);

        append_right(context, fs_attributes);
        add(context);

        append_right(mime_choose, context);
        add(mime_choose);

        append_right(addButton, mime_choose);
        add(addButton);

        append_right(find, addButton);
        add(find);

        append_right(results, find);
        add(results);

        append_right(or, results);
        add(or);

        append_right(and, or);
        add(and);
        
        //убрать фокусы, плохо смотрится
        for (Component c : getComponents()) {
            c.setFocusable(false);
        }
        area.setFocusable(true);
    }
}
