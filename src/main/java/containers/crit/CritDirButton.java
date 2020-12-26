package containers.crit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import swing.button.transparent_button;
import static util.util.err_msg;
import static com.mycompany.Find.MainCatalog;
import java.awt.Window;
import javax.swing.JLabel;
import static swing.util.window_to_center;

public class CritDirButton extends transparent_button {

    JLabel dir_label;
    Window owner;

    public CritDirButton(JLabel dir_label, Window owner) {
        this.dir_label = dir_label;
        this.owner = owner;
        setIcon(new ImageIcon("data/www.apache.org/folder.png"));
        setToolTipText("Выбрать каталог для поиска");
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dir_clicked();
            }
        });
    }

    private void dir_clicked() {
        JFileChooser chooser = new JFileChooser(MainCatalog);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        window_to_center(chooser, owner);
        int ret = chooser.showDialog(owner, "Выбрать");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            MainCatalog = file;
            try {
                dir_label.setText(file.getCanonicalPath());
                dir_label.setToolTipText(file.getCanonicalPath());
            } catch (Exception | Error e) {
                err_msg("Ошибка получения имени каталога поиска", null);
            }
        }
    }
}
