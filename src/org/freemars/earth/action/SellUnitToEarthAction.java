package org.freemars.earth.action;

import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.freemars.controller.FreeMarsController;
import org.freemars.earth.EarthFlightModel;
import org.freemars.earth.command.SellResourceToEarthCommand;
import org.freerealm.unit.Unit;
import org.freemars.earth.ui.EarthDialog;
import org.freemars.model.FreeMarsModel;
import org.freemars.player.FreeMarsPlayer;
import org.freemars.ui.util.FreeMarsOptionPane;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.RemoveUnitCommand;
import org.freerealm.executor.command.WealthAddCommand;
import org.freerealm.resource.Resource;

/**
 *
 * @author Deniz ARIKAN
 */
public class SellUnitToEarthAction extends AbstractAction {

    private FreeMarsController freeMarsController;
    private EarthDialog earthDialog;
    private Unit unit;

    public SellUnitToEarthAction(FreeMarsController freeMarsController, EarthDialog earthDialog, Unit unit) {
        this.freeMarsController = freeMarsController;
        this.earthDialog = earthDialog;
        this.unit = unit;
    }

    public void actionPerformed(ActionEvent e) {
        FreeMarsModel freeMarsModel = freeMarsController.getFreeMarsModel();
        FreeMarsPlayer player = (FreeMarsPlayer) freeMarsModel.getRealm().getPlayerManager().getActivePlayer();
        if (player.hasDeclaredIndependence()) {
            FreeMarsOptionPane.showMessageDialog(earthDialog, "We do not buy from rebels!", "Rejected");
        } else {
            Object[] options = {"Yes", "No, thanks"};
            int price = freeMarsModel.getEarthFlightModel().getEarthSellsAtPrice(unit.getType()) / 2;
            String formattedPrice = new DecimalFormat().format(price);
            int value = JOptionPane.showOptionDialog(earthDialog,
                    "Sell your \"" + unit + "\" for " + formattedPrice + "?",
                    "Sell unit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
            if (value == JOptionPane.YES_OPTION) {

                Iterator<Resource> iterator = unit.getContainedResourcesIterator();
                while (iterator.hasNext()) {
                    Resource resource = iterator.next();
                    int quantity = unit.getResourceQuantity(resource);
                    if (quantity > 0) {
                        CommandResult result = freeMarsController.execute(new SellResourceToEarthCommand(freeMarsController, unit, resource, quantity));
                        if (result.getCode() == CommandResult.RESULT_OK) {
                            int totalPrice = ((Integer) result.getParameter("total_price"));
                            int taxRate = (Integer) result.getParameter("tax_rate");
                            int taxAmount = (Integer) result.getParameter("tax_amount");
                            int netIncome = (Integer) result.getParameter("net_income");
                            String formattedQuantity = new DecimalFormat().format(quantity);
                            String formattedTotalPrice = new DecimalFormat().format(totalPrice);
                            earthDialog.addUnitInfoText(formattedQuantity + " " + resource.getName() + " sold for " + formattedTotalPrice + "\n");
                            if (taxAmount > 0) {
                                String formattedTaxAmount = new DecimalFormat().format(taxAmount);
                                earthDialog.addUnitInfoText("Colonial tax : -" + formattedTaxAmount + " (" + taxRate + "%)\n");
                                String formattedNetIncome = new DecimalFormat().format(netIncome);
                                earthDialog.addUnitInfoText("Net income : " + formattedNetIncome + "\n");
                            }
                            String formattedWealth = new DecimalFormat().format(unit.getPlayer().getWealth());
                            earthDialog.addUnitInfoText("New treasury : " + formattedWealth + " " + freeMarsModel.getRealm().getProperty("currency_unit") + "\n\n");
                        }
                    }
                }
                freeMarsController.execute(new WealthAddCommand(player, price));
                freeMarsModel.getEarthFlightModel().removeUnitLocation(unit);
                freeMarsController.execute(new RemoveUnitCommand(freeMarsController.getFreeMarsModel().getRealm(), player, unit));
                earthDialog.addUnitInfoText(unit.getType().getName() + " has been sold for " + formattedPrice + "\n\n");
                earthDialog.update(EarthFlightModel.SELL_UNIT_TO_EARTH_UPDATE);
            }
        }
    }
}
