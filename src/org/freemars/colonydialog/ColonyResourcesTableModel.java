package org.freemars.colonydialog;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;
import org.freemars.ui.image.FreeMarsImageManager;
import org.freerealm.modifier.GeneralModifier;
import org.freerealm.resource.Resource;
import org.freerealm.settlement.RequiredPopulationResourceAmountCalculator;

/**
 *
 * @author Deniz ARIKAN
 */
public class ColonyResourcesTableModel extends AbstractTableModel {

    private ColonyDialogModel model;
    private final ArrayList<ArrayList> tableData;
    String[] columnNames = {"<html><b>Resource</b></html>",
        "<html><b>Production</b></html>",
        "<html><b>Total stock</b></html>"
    };

    public ColonyResourcesTableModel() {
        tableData = new ArrayList<ArrayList>();
    }

    public void setModel(ColonyDialogModel model) {
        this.model = model;
    }

    @Override
    public Class getColumnClass(int column) {
        if (column == 0) {
            return Icon.class;
        }
        return Object.class;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col].toString();
    }

    public int getRowCount() {
        return tableData.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        ArrayList rowVector = (ArrayList) tableData.get(rowIndex);
        return rowVector;
    }

    public int getTotalCapacity(int rowIndex) {
        ArrayList rowVector = (ArrayList) tableData.get(rowIndex);
        return Integer.parseInt(rowVector.get(4).toString());
    }

    public void refresh() {
        if (model != null) {
            tableData.clear();
            Iterator<Resource> resourcesIterator = model.getColony().getResources().keySet().iterator();
            while (resourcesIterator.hasNext()) {
                Resource resource = resourcesIterator.next();
                ArrayList rowData = new ArrayList();
                Image image = FreeMarsImageManager.getInstance().getImage(resource);
                image = FreeMarsImageManager.createResizedCopy(image, 34, 34, false, null);
                Icon icon = new ImageIcon(image);
                int quantity = model.getColony().getResourceQuantity(resource);
                int production = model.getColony().getResourceProductionFromTerrain(resource) + model.getColony().getResourceProductionFromImprovements(resource);
                int consumption = findResourceConsumption(resource);
                rowData.add(icon);
                rowData.add(resource.toString());
                rowData.add(quantity);
                rowData.add(production);
                rowData.add(consumption);
                rowData.add(model.getColony().getTotalCapacity(resource));
                tableData.add(rowData);
                fireTableDataChanged();
            }
        }
    }
    /*
     public void refresh() {
     if (model != null) {
     tableData.clear();
     for (Entry entry : model.getColony().getResources().entrySet()) {
     Resource resource = (Resource) entry.getKey();
     int quantity = (Integer) entry.getValue();
     ArrayList rowData = new ArrayList();
     Image image = FreeMarsImageManager.getInstance().getImage(resource);
     image = FreeMarsImageManager.createResizedCopy(image, 34, 34, false, null);
     Icon icon = new ImageIcon(image);
     rowData.add(icon);
     rowData.add(resource.toString());
     Vector<Integer> resourceProductionValues = new Vector<Integer>();
     resourceProductionValues.add(model.getColony().getResourceProductionFromTerrain(resource) + model.getColony().getResourceProductionFromImprovements(resource));
     resourceProductionValues.add(findResourceConsumption(resource));
     rowData.add(resourceProductionValues);
     Vector<Integer> resourceQuantityValues = new Vector<Integer>();
     resourceQuantityValues.add(quantity);
     resourceQuantityValues.add(model.getColony().getTotalCapacity(resource));
     rowData.add(resourceQuantityValues);
     tableData.add(rowData);
     fireTableDataChanged();
     }
     }
     }
     */

    private int findResourceConsumption(Resource resource) {
        GeneralModifier[] modifiers = new GeneralModifier[]{model.getColony().getPlayer()};
        RequiredPopulationResourceAmountCalculator requiredPopulationResourceAmountCalculator = new RequiredPopulationResourceAmountCalculator(model.getRealm(), resource, modifiers);
        int requiredPopulationResourceAmount = requiredPopulationResourceAmountCalculator.getRequiredPopulationResourceAmount();
        int resourceConsumption = requiredPopulationResourceAmount * model.getColony().getPopulation();
        if (resource.getName().equals("Fertilizer") && model.getColony().isAutoUsingFertilizer()) {
            int fertilizerQuantityPerTile = Integer.parseInt(model.getRealm().getProperty("fertilizer_quantity_per_tile"));
            resourceConsumption = resourceConsumption + model.getColony().getFertilizedCoordinatesSize() * fertilizerQuantityPerTile;
        }
        return resourceConsumption;
    }
}
