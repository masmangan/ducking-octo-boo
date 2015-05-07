package org.freemars.editor;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import org.freerealm.tile.TileType;

/**
 *
 * @author Deniz ARIKAN
 */
public class ResizeMapDialog extends JDialog {

    public static final int CONFIRM = 0;
    public static final int CANCEL = 1;
    private static final int FRAME_WIDTH = 300;
    private static final int FRAME_HEIGHT = 180;
    private JLabel mapWidthLabel;
    private JLabel mapHeightLabel;
    private JLabel defaultTileTypeLabel;
    private JSpinner mapWidthSpinner;
    private JSpinner mapHeightSpinner;
    private JComboBox defaultTileComboBox;
    private JButton confirmButton;
    private JButton cancelButton;
    private EditorModel data;
    private int returnValue;
    private int mapWidth;
    private int mapHeight;
    private TileType selectedTileType;

    public ResizeMapDialog(Frame owner, EditorModel data) {
        super(owner);
        this.data = data;
        setModal(true);
        setTitle("Resize map");
        getContentPane().setLayout(new GridLayout(4, 2, 10, 10));
        initializeWidgets();
        returnValue = CANCEL;
    }

    public void display() {
        pack();
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - FRAME_WIDTH) / 2, (screenSize.height - FRAME_HEIGHT) / 2, FRAME_WIDTH, FRAME_HEIGHT);
        setVisible(true);
        toFront();
    }

    public int getReturnValue() {
        return returnValue;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public TileType getSelectedTileType() {
        return selectedTileType;
    }

    private void initializeWidgets() {
        getContentPane().add(getMapWidthLabel());
        getContentPane().add(getMapWidthSpinner());

        getContentPane().add(getMapHeightLabel());
        getContentPane().add(getMapHeightSpinner());

        getContentPane().add(getDefaultTileTypeLabel());
        getContentPane().add(getDefaultTileComboBox());

        getContentPane().add(getConfirmButton());
        getContentPane().add(getCancelButton());
    }

    private JLabel getMapWidthLabel() {
        if (mapWidthLabel == null) {
            mapWidthLabel = new JLabel("Map width :");
        }
        return mapWidthLabel;
    }

    private JLabel getMapHeightLabel() {
        if (mapHeightLabel == null) {
            mapHeightLabel = new JLabel("Map height :");
        }
        return mapHeightLabel;
    }

    private JLabel getDefaultTileTypeLabel() {
        if (defaultTileTypeLabel == null) {
            defaultTileTypeLabel = new JLabel("Default tile type :");
        }
        return defaultTileTypeLabel;
    }

    public JSpinner getMapWidthSpinner() {
        if (mapWidthSpinner == null) {
            mapWidthSpinner = new JSpinner(new SpinnerNumberModel(data.getRealm().getMap().getWidth(), 40, 120, 5));
        }
        return mapWidthSpinner;
    }

    public JSpinner getMapHeightSpinner() {
        if (mapHeightSpinner == null) {
            mapHeightSpinner = new JSpinner(new SpinnerNumberModel(data.getRealm().getMap().getHeight(), 40, 120, 5));
        }
        return mapHeightSpinner;
    }

    private JComboBox getDefaultTileComboBox() {
        if (defaultTileComboBox == null) {
            defaultTileComboBox = new JComboBox();
            Iterator<TileType> iterator = data.getRealm().getTileTypeManager().getTileTypesIterator();
            while (iterator.hasNext()) {
                TileType tileType = iterator.next();
                defaultTileComboBox.addItem(tileType);
            }
        }
        return defaultTileComboBox;
    }

    private JButton getConfirmButton() {
        if (confirmButton == null) {
            confirmButton = new JButton("Confirm");
            confirmButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    mapWidth = Integer.parseInt(getMapWidthSpinner().getValue().toString());
                    mapHeight = Integer.parseInt(getMapHeightSpinner().getValue().toString());
                    selectedTileType = (TileType) getDefaultTileComboBox().getSelectedItem();
                    returnValue = CONFIRM;
                    dispose();
                }
            });
        }
        return confirmButton;
    }

    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    returnValue = CANCEL;
                    dispose();
                }
            });
        }
        return cancelButton;
    }
}
