package org.freemars.diplomacy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.freemars.ui.util.FreeMarsDialog;
import org.freerealm.player.Player;

/**
 *
 * @author Deniz ARIKAN
 */
public class DiplomacyDialog extends FreeMarsDialog {

    private final int FRAME_WIDTH = 450;
    private final int FRAME_HEIGHT = 260;
    private JPanel mainPanel;
    private JScrollPane otherPlayersScrollPane;
    private JTable otherPlayersTable;
    private JPanel footerPanel;
    private JButton declareWarButton;
    private JButton closeButton;
    private DiplomacyDialogModel model;

    public DiplomacyDialog(Frame owner) {
        super(owner);
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Diplomacy");
        initializeWidgets();
    }

    public void setModel(DiplomacyDialogModel model) {
        this.model = model;
    }

    public void display() {
        pack();
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - FRAME_WIDTH) / 2, (screenSize.height - FRAME_HEIGHT) / 2, FRAME_WIDTH, FRAME_HEIGHT);
        setVisible(true);
        toFront();
    }

    public void setOtherPlayersTableMouseAdapter(MouseAdapter mouseAdapter) {
        getOtherPlayersTable().addMouseListener(mouseAdapter);
    }

    public void setDeclareWarAction(Action action) {
        getDeclareWarButton().setAction(action);
    }

    public void setCloseAction(Action action) {
        getCloseButton().setAction(action);
    }

    public Player getSelectedOtherPlayer() {
        return ((OtherPlayersTableModel) getOtherPlayersTable().getModel()).getPlayer(getOtherPlayersTable().getSelectedRow());
    }

    private void initializeWidgets() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(getMainPanel(), BorderLayout.CENTER);
        getContentPane().add(getFooterPanel(), BorderLayout.PAGE_END);
    }

    private JPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(getOtherPlayersScrollPane());
        }
        return mainPanel;
    }

    private JScrollPane getOtherPlayersScrollPane() {
        if (otherPlayersScrollPane == null) {
            otherPlayersScrollPane = new JScrollPane(getOtherPlayersTable());
        }
        return otherPlayersScrollPane;
    }

    private JTable getOtherPlayersTable() {
        if (otherPlayersTable == null) {
            otherPlayersTable = new JTable();
        }
        return otherPlayersTable;
    }

    private JPanel getFooterPanel() {
        if (footerPanel == null) {
            footerPanel = new JPanel();
            footerPanel.add(getDeclareWarButton());
            footerPanel.add(getCloseButton());
        }
        return footerPanel;
    }

    private JButton getDeclareWarButton() {
        if (declareWarButton == null) {
            declareWarButton = new JButton();
        }
        return declareWarButton;
    }

    private JButton getCloseButton() {
        if (closeButton == null) {
            closeButton = new JButton();
        }
        return closeButton;
    }
}
