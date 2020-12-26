package containers.crit;

import containers.FileList;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import swing.button.transparent_button;
import swing.dialog;
import static swing.util.pop_dialog;
import util.breader;
import static util.util.err_msg;

public class CritResultsButton extends transparent_button {

    private final Window owner;

    public CritResultsButton(Window owner) {
        this.owner = owner;
        setIcon(new ImageIcon("data/www.apache.org/index.png"));
        setToolTipText("Результаты");
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                clicked();
            }
        });
    }

    private void clicked() {
        dialog __dialog = new dialog(owner, "Результаты");
        ArrayList<File> fileList = new ArrayList();
        breader reader;
        try {
            reader = new breader("data/results.txt");
        } catch (Exception | Error e) {
            err_msg("Ошибка чтения результатов", owner);
            return;
        }
        while (true) {
            String line;
            try {
                line = reader.readLine();
                fileList.add(new File(line));
            } catch (Exception | Error e) {
                break;
            }
        }
        try {
            reader.close();
        } catch (Exception | Error e) {
            err_msg("Ошибка чтения результатов", owner);
        }
        File[] fileArray = new File[fileList.size()];
        for (int i = 0; i < fileList.size(); i++) {
            fileArray[i] = fileList.get(i);
        }
        pop_dialog(new FileList(fileArray, __dialog), __dialog);
    }
}
