package containers;

import static util.util.getPath;
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
import java.io.File;
import swing.dialog;
import swing.useful_container;
import swing.util;

public class FileList extends useful_container {

    public FileList(File fileArray[], dialog owner) {
        super(owner);
        JList fileList = new JList(fileArray);
        fileList.setCellRenderer(new FileRenderer());
        JScrollPane scrollPane = new JScrollPane(fileList);
        setxy(scrollPane, 3, 3);
        setsize(scrollPane, 500, 300);
        add(scrollPane);
        fileList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() != KeyEvent.VK_ENTER) {
                    return;
                }
                showFileInfo(fileArray[fileList.getSelectedIndex()]);
            }
        });
        fileList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                super.mouseClicked(evt);
                if (evt.getClickCount() != 2) {
                    return;
                }
                showFileInfo(fileArray[fileList.locationToIndex(evt.getPoint())]);
            }
        });
        fileList.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                if (fileList.getModel().getSize() == 0) {
                    return;
                }
                String path = getPath(fileArray[fileList.locationToIndex(new Point(e.getX(), e.getY()))]);
                if (path == null) {
                    return;
                }
                fileList.setToolTipText(!path.endsWith(".jpg") && !path.endsWith(".png") ? null : "<html><body>" + path + "<br><img src=\"file:data/thumbnails/" + Find.ThumbsDB.getIdByPath(path) + ".png\"></body></html>");
            }
        });
    }

    private void showFileInfo(File file) {
        dialog __dialog = new dialog(owner, file.getName());
        view __container = new view(file, __dialog);
        util.pop_dialog(__container, __dialog);
    }
}
