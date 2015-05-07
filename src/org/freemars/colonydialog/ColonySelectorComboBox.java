package org.freemars.colonydialog;

import javax.swing.JComboBox;
import org.freerealm.settlement.Settlement;

/**
 *
 * @author arikande
 */
public class ColonySelectorComboBox extends JComboBox {

    public void addColony(Settlement settlement) {
        addItem(settlement);
    }
/*
    public City getSelectedColony() {
        ColonySelectorComboBoxItem selectedItem = (ColonySelectorComboBoxItem) getSelectedItem();
        if (selectedItem != null) {
            return selectedItem.getCity();
        } else {
            return null;
        }
    }
*/
    public void setSelectedColony(Settlement settlement) {
        for (int i = 0; i < getItemCount(); i++) {
            ColonySelectorComboBoxItem item = (ColonySelectorComboBoxItem) getItemAt(i);
            if (item.getSettlement().equals(settlement)) {
                super.setSelectedItem(item);
                return;
            }
        }
    }

    class ColonySelectorComboBoxItem {

        private Settlement settlement;

        private ColonySelectorComboBoxItem(Settlement settlement) {
            this.settlement = settlement;
        }

        @Override
        public String toString() {
            return getSettlement().getName();
        }

        private Settlement getSettlement() {
            return settlement;
        }
    }
}
