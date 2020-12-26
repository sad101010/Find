package containers.expr;

import static com.mycompany.Find.ThumbsDB;
import swing.button.text_roll;
import swing.button.transparent_button;
import swing.useful_container;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static meta.type.getFieldType;
import static meta.type.parseFieldValue;
import swing.dialog;
import static swing.util.*;
import static util.util.err_msg;

public class expr extends useful_container {

    private final JComboBox box = new JComboBox();
    private final text_roll sign = new text_roll();
    private final transparent_button ok = new transparent_button();
    private final JTextArea area;
    private final ComboField comboField;

    {
        box.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                box_item_selected();
            }
        });
        add(box);

        sign.texts.add("<");
        sign.texts.add("≤");
        sign.texts.add("=");
        sign.texts.add(">");
        sign.texts.add("≥");
        sign.texts.add("⊃");
        sign.tooltips.add("Меньше");
        sign.tooltips.add("Меньше либо равно");
        sign.tooltips.add("Равно");
        sign.tooltips.add("Больше");
        sign.tooltips.add("Больше либо равно");
        sign.tooltips.add("Содержит");
        sign.setState(0);
        sign.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                sign_clicked(e);
            }
        });
        add(sign);

        ok.setToolTipText("Добавить");
        ok.setIcon(new ImageIcon("data/www.apache.org/hand.right.png"));
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ok_clicked();
            }
        });
        add(ok);

        //убрать фокусы, плохо смотрится
        for (Component c : getComponents()) {
            c.setFocusable(false);
        }
    }

    public expr(JTextArea area, Object[] values, dialog owner) {
        super(owner);
        this.area = area;

        comboField = ComboField.createComboField(owner);
        comboField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ok_clicked();
                }
            }
        });
        comboField.add_to_useful_container(this);
        comboField.setFocusable(true);

        box.setModel(new DefaultComboBoxModel(values));
        setxy(box, 3, 3);
        fit(box);

        append_right(sign, box);
        setsize(sign, box.getHeight(), box.getHeight());

        comboField.setSize(box.getWidth(), box.getHeight());
        //append только после setSize
        comboField.append_this_right_to(sign);

        append_middle_bottom(ok, sign);

        String item = (String) box.getSelectedItem();

        box_item_selected();
    }

    private void sign_clicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            sign.__switch(1);
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            sign.__switch(-1);
        }
    }

    private void chooseSign() {
        if (getFieldType(box.getSelectedItem().toString()).equals("@String")) {
            if (sign.texts.size() == 5) {
                sign.texts.add("⊃");
                sign.tooltips.add("Содержит");
            }
        } else {
            if (sign.texts.size() == 6) {
                sign.setState(0);
                sign.texts.remove(sign.texts.size() - 1);
                sign.tooltips.remove(sign.tooltips.size() - 1);
            }
        }
        if (box.getSelectedItem().toString().equals("Изображения")) {
            sign.clickable = false;
            sign.setState(sign.tooltips.size() - 1);
        } else {
            sign.clickable = true;
        }
    }

    private void ok_clicked() {
        String __field = box.getSelectedItem().toString();
        String __sign = sign.getText();
        String __value = comboField.getTextIfVisible();
        if (__value == null) {
            err_msg("Ошибка", owner);
            return;
        }
        if (parseFieldValue(__field, __value) == null) {
            err_msg("Ошибка ввода", owner);
            return;
        }
        if (__field.equals("Изображения")) {
            ThumbsDB.add(__value);
            Integer id = ThumbsDB.getIdByPath(__value);
            if (id == null) {
                err_msg("Не удалось добавить изображение", owner);
                return;
            }
            String inertString='"' + __field + '"'+__sign+'[' + id + ']' + ' ';
            area.insert(inertString,area.getCaretPosition());
            owner.dispose();
            return;
        }
        String inertString='"' + __field + '"'+__sign+'"' + __value + '"' + ' ';
        area.insert(inertString,area.getCaretPosition());
        owner.dispose();
    }

    private void box_item_selected() {
        chooseSign();
        comboField.selectWhatToShow(box.getSelectedItem().toString());
    }
}
