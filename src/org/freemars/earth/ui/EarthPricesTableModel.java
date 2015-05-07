package org.freemars.earth.ui;

import java.util.Iterator;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import org.freemars.earth.EarthFlightModel;
import org.freerealm.resource.Resource;

/**
 *
 * @author Deniz ARIKAN
 */
public class EarthPricesTableModel extends AbstractTableModel {

    private EarthFlightModel earthFlightModel;
    private Vector<Vector> tableVector;
    String[] columnNames = {"Resource",
        "Buy", "Sell"
    };

    public EarthPricesTableModel(EarthFlightModel earthFlightModel) {
        this.earthFlightModel = earthFlightModel;
        tableVector = new Vector<Vector>();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col].toString();
    }

    public int getRowCount() {
        return tableVector.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Vector rowVector = (Vector) tableVector.get(rowIndex);
        return rowVector.get(columnIndex);
    }

    public void refresh() {
        tableVector.clear();
        Iterator resourceIterator = earthFlightModel.getRealm().getResourceManager().getResourcesIterator();
        while (resourceIterator.hasNext()) {
            Resource resource = (Resource) resourceIterator.next();
            Vector<String> rowVector = new Vector<String>();
            rowVector.add(resource.getName());
            rowVector.add(String.valueOf(earthFlightModel.getEarthSellsAtPrice(resource)));
            rowVector.add(String.valueOf(earthFlightModel.getEarthBuysAtPrice(resource)));
            tableVector.add(rowVector);
        }
        fireTableDataChanged();
    }
}
