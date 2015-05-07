package org.freemars.earth.ui;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import org.freerealm.resource.Resource;
import org.freerealm.resource.ResourceStorer;

/**
 *
 * @author Deniz ARIKAN
 */
public class UnitResourcesTableModel extends AbstractTableModel {

    private ResourceStorer resourceStorer;
    private Vector<Vector> tableVector = null;
    String[] columnNames = {"Resource",
        "Quantity"
    };

    public UnitResourcesTableModel() {
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
        if (getResourceStorer() != null) {
            Iterator<Resource> resourceIterator = getResourceStorer().getContainedResourcesIterator();
            while (resourceIterator.hasNext()) {
                Resource resource = resourceIterator.next();
                Vector<String> rowVector = new Vector<String>();
                rowVector.add(resource.toString());
                rowVector.add(String.valueOf(getResourceStorer().getResourceQuantity(resource)));
                tableVector.add(rowVector);
            }
        }
        fireTableDataChanged();
    }

    public ResourceStorer getResourceStorer() {
        return resourceStorer;
    }

    public void setResourceStorer(ResourceStorer resourceStorer) {
        this.resourceStorer = resourceStorer;
        refresh();
    }
}
