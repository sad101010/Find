package containers.crit;

import static com.mycompany.Find.MainCatalog;
import static com.mycompany.Find.MainFindThread;
import static expr.check.initExpr;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import swing.button.png_roll;
import static util.util.err_msg;
import static util.util.yn_promt;

public class CritFindButton extends png_roll {

    private final Window owner;
    private final JTextArea area;

    public CritFindButton(Window owner, JTextArea area) {
        this.owner = owner;
        this.area = area;
        icons.add(new ImageIcon("data/www.apache.org/hand.right.png"));
        icons.add(new ImageIcon("data/www.apache.org/hand.up.png"));
        tooltips.add("Начать поиск");
        tooltips.add("Остановить поиск");
        setState(0);
        setMultiClickThreshhold(1000);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                find_clicked();
            }
        });
    }

    private void find_clicked() {
        if (MainCatalog == null) {
            err_msg("Каталог поиска не выбран", owner);
            return;
        }
        if (getState() == 1) {
            int ret = yn_promt("Остановить поиск?", owner);
            if (ret == JOptionPane.YES_OPTION) {
                System.err.println("tryin interrupt");
                MainFindThread.kill();
                __switch(1);
            }
            return;
        }
        if(!initExpr(area.getText())){
            err_msg("Ошибка в выражении", owner);
            return;
        }
        MainFindThread = new threads.mainFindThread(this, owner);
        MainFindThread.start();
        __switch(1);
    }
}
