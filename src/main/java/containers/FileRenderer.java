package containers;

import java.awt.Component;
import java.io.File;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.filechooser.FileSystemView;

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
