package org.freemars.mission;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.freemars.model.FreeMarsModel;
import org.freerealm.player.Player;

/**
 *
 * @author Deniz ARIKAN
 */
public class MissionsTable extends JTable {

    private MissionsTableModel missionsTableModel;

    public MissionsTable(FreeMarsModel freeMarsModel, Player player) {
        missionsTableModel = new MissionsTableModel(freeMarsModel, player);
        this.setModel(missionsTableModel);
        setShowGrid(false);
        setShowHorizontalLines(true);
        setShowVerticalLines(false);
        setRowSelectionAllowed(false);
        setColumnSelectionAllowed(false);
        setCellSelectionEnabled(false);
        getTableHeader().setReorderingAllowed(false);
        getColumnModel().getColumn(0).setPreferredWidth(300);
        getColumnModel().getColumn(2).setPreferredWidth(30);
    }

    @Override
    public int getRowHeight() {
        return 30;
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        Component component = (Component) super.getCellRenderer(row, column);
        if (row % 2 == 1) {
            component.setBackground(new Color(240, 240, 240));
        } else {
            component.setBackground(Color.WHITE);
        }
        return (TableCellRenderer) component;
    }
}
