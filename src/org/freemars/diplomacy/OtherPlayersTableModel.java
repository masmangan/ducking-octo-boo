package org.freemars.diplomacy;

import java.util.Iterator;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import org.freemars.model.FreeMarsModel;
import org.freerealm.diplomacy.Diplomacy;
import org.freerealm.player.Player;

/**
 *
 * @author Deniz ARIKAN
 */
public class OtherPlayersTableModel extends AbstractTableModel {

    private Player player;
    private FreeMarsModel freeMarsModel;
    private Vector<Player> players;
    String[] columnNames = {"Id", "Player",
        "Nation", "Status"
    };

    public OtherPlayersTableModel(Player player, FreeMarsModel freeMarsModel) {
        this.player = player;
        this.freeMarsModel = freeMarsModel;
        players = new Vector<Player>();
        Iterator<Player> iterator = freeMarsModel.getPlayersIterator();
        while (iterator.hasNext()) {
            Player other = iterator.next();
            if (!player.equals(other)) {
                players.add(other);
            }
        }
    }

    public Player getPlayer(int row) {
        return players.get(row);
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col].toString();
    }

    public int getRowCount() {
        return players.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return getPlayer(rowIndex).getId();
        } else if (columnIndex == 1) {
            return getPlayer(rowIndex).getName();
        } else if (columnIndex == 2) {
            return getPlayer(rowIndex).getNation().getName();
        } else {
            int status = freeMarsModel.getRealm().getDiplomacy().getPlayerRelationStatus(player, getPlayer(rowIndex));
            switch (status) {
                case Diplomacy.NO_CONTACT:
                    return "No contact";
                case Diplomacy.AT_PEACE:
                    return "At peace";
                case Diplomacy.AT_WAR:
                    return "At war";
                case Diplomacy.ALLIED:
                    return "Allied";
                default:
                    return "No contact";
            }
        }
    }
}
