package containers.expr;

import com.mycompany.Find;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.io.File;
import java.text.ParseException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;
import static meta.type.getFieldType;
import swing.button.transparent_button;
import swing.useful_container;
import static swing.util.append_right;
import static swing.util.append_middle_bottom;
import static swing.util.append_right_bottom;
import static swing.util.fit;
import static swing.util.setsize;
import static util.util.err_msg;

public class ComboField {

    private final JTextField textField = new JTextField();
    private final JFormattedTextField timeField = new JFormattedTextField();
    private final JFormattedTextField dateField = new JFormattedTextField();
    private final JLabel formatLabel = new JLabel();
    private final Window owner;
    private final transparent_button dir = new transparent_button();

    private void dir_clicked() {
        JFileChooser chooser = new JFileChooser(Find.imgDir);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int ret = chooser.showDialog(owner, "Выбрать");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            Find.imgDir = file.getParentFile();
            try {
                textField.setText(file.getCanonicalPath());
            } catch (Exception | Error e) {
                err_msg("Ошибка получения полного пути файла", owner);
            }
        }
    }

    public String getTextIfVisible() {
        if (textField.isVisible()) {
            return textField.getText();
        }
        if (timeField.isVisible()) {
            return timeField.getText();
        }
        if (dateField.isVisible()) {
            return dateField.getText();
        }
        return null;
    }

    public void setFocusable(boolean focusable) {
        textField.setFocusable(focusable);
        timeField.setFocusable(focusable);
        dateField.setFocusable(focusable);
    }

    public void addKeyListener(KeyAdapter keyAdapter) {
        textField.addKeyListener(keyAdapter);
        timeField.addKeyListener(keyAdapter);
        dateField.addKeyListener(keyAdapter);
    }

    public void add_to_useful_container(useful_container contatiner) {
        contatiner.add(textField);
        contatiner.add(timeField);
        contatiner.add(dateField);
        contatiner.add(dir);
        contatiner.add(formatLabel);
    }

    public void selectWhatToShow(String field) {
        textField.setVisible(false);
        timeField.setVisible(false);
        dateField.setVisible(false);
        formatLabel.setVisible(false);
        switch (getFieldType(field)) {
            case "@TimeBean":
                timeField.setVisible(true);
                formatLabel.setText("Формат - чч:мм:сс");
                fit(formatLabel);
                append_middle_bottom(formatLabel, textField);
                formatLabel.setVisible(true);
                break;
            case "@Date":
                dateField.setVisible(true);
                formatLabel.setText("Формат - дд.мм.гггг");
                fit(formatLabel);
                append_middle_bottom(formatLabel, textField);
                formatLabel.setVisible(true);
                break;
            case "@String":
            case "@Long":
                textField.setVisible(true);
                formatLabel.setVisible(false);
                break;
        }
        dir.setVisible(field.equals("Изображения"));
    }

    public void setSize(int width, int height) {
        setsize(textField, width, height);
        setsize(timeField, width, height);
        setsize(dateField, width, height);
    }

    //если вызвать до setSize не сработает
    public void append_this_right_to(Component c) {
        append_right(textField, c);
        append_right(timeField, c);
        append_right(dateField, c);
        append_right_bottom(dir, textField);
    }

    private ComboField(Window owner) throws ParseException {
        MaskFormatter timeFormatter = new MaskFormatter();
        MaskFormatter dateFormatter = new MaskFormatter();
        timeFormatter.setMask("##:##:##");
        dateFormatter.setMask("##.##.####");
        timeFormatter.install(timeField);
        dateFormatter.install(dateField);
        timeField.setColumns(timeFormatter.getMask().length());
        dateField.setColumns(dateFormatter.getMask().length());
        this.owner = owner;
        dir.setIcon(new ImageIcon("data/www.apache.org/folder.png"));
        dir.setToolTipText("Выбрать изображение");
        dir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dir_clicked();
            }
        });
    }

    public static ComboField createComboField(Window owner) {
        ComboField comboField;
        try {
            comboField = new ComboField(owner);
        } catch (Exception | Error e) {
            return null;
        }
        return comboField;
    }
}
