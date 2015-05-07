package org.freemars.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.freerealm.map.Map;

/**
 *
 * @author Deniz ARIKAN
 */
public class MapInformationDialog extends JDialog {

    private static final int FRAME_WIDTH = 200;
    private static final int FRAME_HEIGHT = 200;
    private JPanel mapInformationPanel;
    private JPanel closeDialogPanel;
    private JButton closeDialogButton;
    private Map map;

    public MapInformationDialog(Frame owner, Map map) {
        super(owner);
        setModal(true);
        setTitle("Map information");
        this.map = map;
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(getMapInformationPanel(), BorderLayout.CENTER);
        getContentPane().add(getCloseDialogPanel(), BorderLayout.PAGE_END);
    }

    public void display() {
        pack();
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - FRAME_WIDTH) / 2, (screenSize.height - FRAME_HEIGHT) / 2, FRAME_WIDTH, FRAME_HEIGHT);
        setVisible(true);
        toFront();
    }

    private JPanel getMapInformationPanel() {
        if (mapInformationPanel == null) {
            mapInformationPanel = new JPanel(new GridLayout(2, 2));
            mapInformationPanel.add(new JLabel("Map width : "));
            mapInformationPanel.add(new JLabel(String.valueOf(map.getWidth())));
            mapInformationPanel.add(new JLabel("Map height : "));
            mapInformationPanel.add(new JLabel(String.valueOf(map.getHeight())));
        }
        return mapInformationPanel;
    }

    private JPanel getCloseDialogPanel() {
        if (closeDialogPanel == null) {
            closeDialogPanel = new JPanel();
            closeDialogPanel.add(getCloseDialogButton());
        }
        return closeDialogPanel;
    }

    private JButton getCloseDialogButton() {
        if (closeDialogButton == null) {
            closeDialogButton = new JButton("Close");
            closeDialogButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
        }
        return closeDialogButton;
    }
}
