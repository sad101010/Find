package containers;

import static util.util.getPath;
import static util.util.exit_with_err_msg;
import static swing.util.setsize;
import static swing.util.setxy;
import com.mycompany.Find;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import swing.dialog;
import swing.useful_container;
import swing.util;

public class FileList extends useful_container {

    private JList fileList;

    public FileList(File[] fileArray, dialog owner) {
        super(owner);
        fileList = new JList(fileArray);
        fileList.setCellRenderer(new FileRenderer());
        JScrollPane scrollPane = new JScrollPane(fileList);
        setxy(scrollPane, 0, 0);
        setsize(scrollPane, 500, 300);
        add(scrollPane);
        fileList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    int index = fileList.getSelectedIndex();
                    dialog __dialog = new dialog(owner, fileArray[index].getName());
                    view __container=new view(fileArray[index], __dialog);
                    util.pop_dialog(__container, __dialog);
                }
            }
        });
        fileList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = fileList.locationToIndex(evt.getPoint());
                    dialog __dialog = new dialog(owner, fileArray[index].getName());
                    view __container=new view(fileArray[index], __dialog);
                    util.pop_dialog(__container, __dialog);
                }
            }
        });
        fileList.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                JList list = (JList) e.getSource();
                if (list.getModel().getSize() == 0) {
                    return;
                }
                Point pt = new Point(e.getX(), e.getY());
                int pos = list.locationToIndex(pt);
                String path = getPath(fileArray[pos]);
                if (path == null) {
                    exit_with_err_msg("Был найден файл, для которого невозможно получить путь", owner);
                }
                fileList.setToolTipText(!path.endsWith(".jpg") && !path.endsWith(".png") ? null : "<html><body>" + path + "<br><img src=\"file:data/thumbnails/" + Find.ThumbsDB.getIdByPath(path) + ".png\"></body></html>");
            }
        });
    }
}

class FileRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        JLabel l = (JLabel) c;
        File f = (File) value;
        l.setText(f.getName());
        l.setIcon(FileSystemView.getFileSystemView().getSystemIcon(f));
        return l;
    }
}
