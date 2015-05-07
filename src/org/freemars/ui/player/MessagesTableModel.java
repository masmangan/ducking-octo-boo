package org.freemars.ui.player;

import java.awt.Image;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;
import org.freemars.earth.message.SpaceshipsSeizedMessage;
import org.freemars.earth.message.UnitsSeizedMessage;
import org.freemars.message.EarthTaxRateChangedMessage;
import org.freemars.message.ExpeditionaryForceChangedMessage;
import org.freemars.message.ExpeditionaryForceDefeatedMessage;
import org.freemars.message.ExpeditionaryForceLandedMessage;
import org.freemars.message.FirstArrivalToMarsMessage;
import org.freemars.message.NewColonyFoundedMessage;
import org.freemars.message.NotEnoughFertilizerMessage;
import org.freemars.message.SettlementCapturedMessage;
import org.freemars.message.UnitAttackedMessage;
import org.freemars.message.UnitRelocationCompletedMessage;
import org.freemars.random.event.ColonistsReturningToEarthMessage;
import org.freemars.random.event.CreditDonationMessage;
import org.freemars.random.event.EnergyLossMessage;
import org.freemars.random.event.FertilizerDonationMessage;
import org.freemars.random.event.FireMessage;
import org.freemars.random.event.FoodContaminationMessage;
import org.freemars.random.event.FreeColonistsMessage;
import org.freemars.random.event.HydrogenExplosionMessage;
import org.freemars.random.event.MeteorStrikeMessage;
import org.freemars.random.event.MiningAccidentMessage;
import org.freemars.random.event.OxygenLeakMessage;
import org.freemars.random.event.ShuttleAccidentMessage;
import org.freemars.random.event.TransporterAccidentMessage;
import org.freemars.random.event.WaterLeakMessage;
import org.freemars.ui.image.FreeMarsImageManager;
import org.freerealm.player.Message;
import org.freerealm.player.NotEnoughInputResourceForSettlementImprovementMessage;
import org.freerealm.player.NotEnoughPopulationForProductionMessage;
import org.freerealm.player.NotEnoughResourceForProductionMessage;
import org.freerealm.player.Player;
import org.freerealm.player.PlayerRemovedMessage;
import org.freerealm.player.ResourceWasteMessage;
import org.freerealm.player.SettlementImprovementCompletedMessage;
import org.freerealm.player.UnitCompletedMessage;
import org.freerealm.resource.Resource;
import org.freerealm.settlement.Settlement;
import org.freerealm.settlement.SettlementBuildable;
import org.freerealm.settlement.improvement.SettlementImprovement;
import org.freerealm.settlement.improvement.SettlementImprovementType;
import org.freerealm.unit.Unit;
import org.freerealm.unit.UnitType;

/**
 *
 * @author Deniz ARIKAN
 */
public class MessagesTableModel extends AbstractTableModel {

    Vector<Message> messages;

    public MessagesTableModel() {
        messages = new Vector<Message>();
    }

    @Override
    public String getColumnName(int col) {
        return "";
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Icon.class;
        }
        return Object.class;
    }

    public void addMessage(Message message) {
        messages.add(message);
        fireTableDataChanged();
    }

    public Message getMessageAt(int row) {
        return messages.get(row);
    }

    public int getRowCount() {
        return messages.size();
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return getMessageImage(rowIndex);
        } else {
            return messages.get(rowIndex).getText();
        }
    }

    private ImageIcon getMessageImage(int rowIndex) {
        Message message = messages.get(rowIndex);
        Image messageImage = null;
        if (message instanceof UnitRelocationCompletedMessage) {
            UnitRelocationCompletedMessage unitRelocationCompletedMessage = (UnitRelocationCompletedMessage) message;
            Unit unit = unitRelocationCompletedMessage.getUnit();
            messageImage = FreeMarsImageManager.getInstance().getImage(unit);
            messageImage = FreeMarsImageManager.createResizedCopy(messageImage, -1, 50, false, null);
        } else if (message instanceof ResourceWasteMessage) {
            ResourceWasteMessage resourceWasteMessage = (ResourceWasteMessage) message;
            Resource resource = resourceWasteMessage.getResource();
            messageImage = FreeMarsImageManager.getInstance().getImage(resource);
            messageImage = FreeMarsImageManager.createResizedCopy(messageImage, -1, 40, false, null);
        } else if (message instanceof SettlementImprovementCompletedMessage) {
            SettlementImprovementCompletedMessage settlementImprovementCompletedMessage = (SettlementImprovementCompletedMessage) message;
            Settlement settlement = settlementImprovementCompletedMessage.getSettlement();
            SettlementImprovementType settlementImprovementType = settlementImprovementCompletedMessage.getSettlementImprovementType();
            SettlementBuildable nextProduction = settlementImprovementCompletedMessage.getNextProduction();
            StringBuffer messageText = new StringBuffer("Colony of " + settlement.getName() + " has completed " + settlementImprovementType + ".");
            if (nextProduction != null) {
                messageText.append(" Now producing " + nextProduction + ".");
            } else {
                messageText.append(" Nothing is being produced now.");
            }
            settlementImprovementCompletedMessage.setText(messageText);
            messageImage = FreeMarsImageManager.getInstance().getImage(settlementImprovementType);
            messageImage = FreeMarsImageManager.createResizedCopy(messageImage, -1, 60, false, null);
        } else if (message instanceof UnitCompletedMessage) {
            UnitCompletedMessage unitCompletedMessage = (UnitCompletedMessage) message;
            Settlement settlement = unitCompletedMessage.getSettlement();
            Unit unit = unitCompletedMessage.getUnit();
            SettlementBuildable nextProduction = unitCompletedMessage.getNextProduction();
            StringBuffer messageText = new StringBuffer("Colony of " + settlement.getName() + " has produced \"" + unit.getName() + "\".");
            if (unitCompletedMessage.isContiuousProduction()) {
                messageText.append(" Colony is continuing to produce another " + unit.getType().getName() + ".");
            } else {
                if (nextProduction != null) {
                    messageText.append(" Now producing " + nextProduction + ".");
                } else {
                    messageText.append(" Nothing is being produced now.");
                }
            }
            unitCompletedMessage.setText(messageText);
            messageImage = FreeMarsImageManager.getInstance().getImage(unit.getType());
            messageImage = FreeMarsImageManager.createResizedCopy(messageImage, -1, 60, false, null);
        } else if (message instanceof NotEnoughPopulationForProductionMessage) {
            NotEnoughPopulationForProductionMessage notEnoughPopulationForProductionMessage = (NotEnoughPopulationForProductionMessage) message;
            messageImage = FreeMarsImageManager.getInstance().getImage(notEnoughPopulationForProductionMessage.getSettlement());
            messageImage = FreeMarsImageManager.createResizedCopy(messageImage, -1, 50, false, null);
        } else if (message instanceof NotEnoughResourceForProductionMessage) {
            NotEnoughResourceForProductionMessage notEnoughResourceForProductionMessage = (NotEnoughResourceForProductionMessage) message;
            messageImage = FreeMarsImageManager.getInstance().getImage(notEnoughResourceForProductionMessage.getResource());
            messageImage = FreeMarsImageManager.createResizedCopy(messageImage, -1, 50, false, null);
        } else if (message instanceof UnitAttackedMessage) {
            UnitAttackedMessage unitAttackedMessage = (UnitAttackedMessage) message;
            Unit defender = unitAttackedMessage.getDefender();
            messageImage = FreeMarsImageManager.getInstance().getImage(defender);
            messageImage = FreeMarsImageManager.createResizedCopy(messageImage, -1, 50, false, null);
        } else if (message instanceof SettlementCapturedMessage) {
            SettlementCapturedMessage settlementCapturedMessage = (SettlementCapturedMessage) message;
            Settlement settlement = settlementCapturedMessage.getSettlement();
            messageImage = FreeMarsImageManager.getInstance().getImage(settlement);
            messageImage = FreeMarsImageManager.createResizedCopy(messageImage, -1, 40, false, null);
        } else if (message instanceof FirstArrivalToMarsMessage) {
            messageImage = FreeMarsImageManager.getInstance().getImage("MARS_48x48");
        } else if (message instanceof PlayerRemovedMessage) {
            PlayerRemovedMessage playerRemovedMessage = (PlayerRemovedMessage) message;
            Player removedPlayer = playerRemovedMessage.getPlayer();
            messageImage = FreeMarsImageManager.getInstance().getImage(removedPlayer.getNation());
        } else if (message instanceof ExpeditionaryForceDefeatedMessage) {
            ExpeditionaryForceDefeatedMessage expeditionaryForceDefeatedMessage = (ExpeditionaryForceDefeatedMessage) message;
            Player removedPlayer = expeditionaryForceDefeatedMessage.getExpeditionaryForcePlayer();
            messageImage = FreeMarsImageManager.getInstance().getImage(removedPlayer.getNation());
        } else if (message instanceof ExpeditionaryForceChangedMessage) {
            ExpeditionaryForceChangedMessage expeditionaryForceChangedMessage = (ExpeditionaryForceChangedMessage) message;
            UnitType unitType = expeditionaryForceChangedMessage.getUpdatedUnits().keySet().iterator().next();
            messageImage = FreeMarsImageManager.getInstance().getImage(unitType);
            messageImage = FreeMarsImageManager.createResizedCopy(messageImage, -1, 50, false, null);
        } else if (message instanceof EarthTaxRateChangedMessage) {
            messageImage = FreeMarsImageManager.getInstance().getImage("EARTH_CREDITS");
        } else if (message instanceof NotEnoughFertilizerMessage) {
            NotEnoughFertilizerMessage notEnoughFertilizerMessage = (NotEnoughFertilizerMessage) message;
            messageImage = FreeMarsImageManager.getInstance().getImage(notEnoughFertilizerMessage.getFertilizerResource());
            messageImage = FreeMarsImageManager.createResizedCopy(messageImage, -1, 40, false, null);
        } else if (message instanceof MeteorStrikeMessage) {
            messageImage = FreeMarsImageManager.getInstance().getImage("METEOR");
        } else if (message instanceof OxygenLeakMessage) {
            messageImage = FreeMarsImageManager.getInstance().getImage("OXYGEN");
        } else if (message instanceof HydrogenExplosionMessage) {
            messageImage = FreeMarsImageManager.getInstance().getImage("HYDROGEN");
        } else if (message instanceof FireMessage) {
            messageImage = FreeMarsImageManager.getInstance().getImage("FIRE");
        } else if (message instanceof MiningAccidentMessage) {
            messageImage = FreeMarsImageManager.getInstance().getImage("MINING_ACCIDENT");
        } else if (message instanceof TransporterAccidentMessage) {
            messageImage = FreeMarsImageManager.getInstance().getImage("TRANSPORTER_ACCIDENT_EVENT");
        } else if (message instanceof ShuttleAccidentMessage) {
            messageImage = FreeMarsImageManager.getInstance().getImage("SHUTTLE_ACCIDENT_EVENT");
        } else if (message instanceof ColonistsReturningToEarthMessage) {
            messageImage = FreeMarsImageManager.getInstance().getImage("COLONISTS_RETURNING_TO_EARTH");
        } else if (message instanceof FreeColonistsMessage) {
            messageImage = FreeMarsImageManager.getInstance().getImage("FREE_COLONISTS_EVENT");
        } else if (message instanceof CreditDonationMessage) {
            messageImage = FreeMarsImageManager.getInstance().getImage("CREDIT_DONATION_EVENT");
        } else if (message instanceof FertilizerDonationMessage) {
            messageImage = FreeMarsImageManager.getInstance().getImage("FERTILIZER_DONATION_EVENT");
        } else if (message instanceof SpaceshipsSeizedMessage) {
            messageImage = FreeMarsImageManager.getInstance().getImage("SPACESHIPS_SEIZED");
        } else if (message instanceof UnitsSeizedMessage) {
            messageImage = FreeMarsImageManager.getInstance().getImage("UNITS_SEIZED");
        } else if (message instanceof WaterLeakMessage) {
            messageImage = FreeMarsImageManager.getInstance().getImage("WATER_LEAK_EVENT");
        } else if (message instanceof FoodContaminationMessage) {
            messageImage = FreeMarsImageManager.getInstance().getImage("FOOD_CONTAMINATION_EVENT");
        } else if (message instanceof EnergyLossMessage) {
            messageImage = FreeMarsImageManager.getInstance().getImage("ENERGY_LOSS_EVENT");
        } else if (message instanceof NewColonyFoundedMessage) {
            NewColonyFoundedMessage newColonyFoundedMessage = (NewColonyFoundedMessage) message;
            Settlement settlement = newColonyFoundedMessage.getSettlement();
            messageImage = FreeMarsImageManager.getInstance().getImage(settlement);
            messageImage = FreeMarsImageManager.createResizedCopy(messageImage, -1, 40, false, null);
        } else if (message instanceof ExpeditionaryForceLandedMessage) {
            Image image = FreeMarsImageManager.getInstance().getImage("UNIT_5_SE");
            messageImage = FreeMarsImageManager.createResizedCopy(image, -1, 50, false, null);
        } else if (message instanceof NotEnoughInputResourceForSettlementImprovementMessage) {
            NotEnoughInputResourceForSettlementImprovementMessage notEnoughInputResourceForSettlementImprovementMessage = (NotEnoughInputResourceForSettlementImprovementMessage) message;
            Settlement settlement = notEnoughInputResourceForSettlementImprovementMessage.getSettlement();
            Resource resource = notEnoughInputResourceForSettlementImprovementMessage.getResource();
            SettlementImprovement settlementImprovement = notEnoughInputResourceForSettlementImprovementMessage.getSettlementImprovement();
            StringBuilder messageText = new StringBuilder();
            messageText.append("Colony of ");
            messageText.append(settlement.getName());
            messageText.append(" does not have any ");
            messageText.append(resource.getName().toLowerCase());
            messageText.append(" to use in its ");
            messageText.append(settlementImprovement.getType().getName().toLowerCase());
            messageText.append(".");
            notEnoughInputResourceForSettlementImprovementMessage.setText(messageText.toString());
            Image image = FreeMarsImageManager.getInstance().getImage(notEnoughInputResourceForSettlementImprovementMessage.getSettlementImprovement());
            messageImage = FreeMarsImageManager.createResizedCopy(image, -1, 50, false, null);
        }
        if (messageImage != null) {
            return new ImageIcon(messageImage);
        } else {
            return new ImageIcon();
        }
    }
}
