package org.freemars.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.freemars.controller.FreeMarsController;
import org.freemars.earth.command.BuyUnitFromEarthCommand;
import org.freemars.player.FreeMarsPlayer;
import org.freerealm.executor.command.BuySettlementProductionCommand;
import org.freerealm.player.mission.Mission;
import org.freerealm.player.mission.SettlementCountMission;
import org.freerealm.player.mission.SettlementImprovementCountMission;
import org.freerealm.settlement.Settlement;
import org.freerealm.settlement.improvement.SettlementImprovementType;
import org.freerealm.unit.UnitType;

/**
 *
 * @author Deniz ARIKAN
 */
public class WealthManager {

    private static final Logger logger = Logger.getLogger(WealthManager.class);
    private final FreeMarsController freeMarsController;
    private final AIPlayer aiPlayer;
    private ArrayList<UnitType> spaceshipTypesToBuy;

    private UnitType shuttleType;
    private UnitType freighterType;
    private UnitType bulkFreighterType;

    public WealthManager(FreeMarsController freeMarsController, AIPlayer freeMarsAIPlayer, DecisionModel decisionModel) {
        this.freeMarsController = freeMarsController;
        this.aiPlayer = freeMarsAIPlayer;
        initSpaceshipTypesToBuy();
    }

    public void manageWealth() {
        logger.info("Managing wealth...");
        checkSpaceshipPurchase();
        int freighterCount = aiPlayer.getUnitsOfType(freighterType).size();
        if (freeMarsController.getFreeMarsModel().getNumberOfTurns() > 120 && freighterCount == 0) {
            return;
        }
        int bulkFreighterCount = aiPlayer.getUnitsOfType(bulkFreighterType).size();
        if (freeMarsController.getFreeMarsModel().getNumberOfTurns() > 280 && bulkFreighterCount == 0) {
            return;
        }
        checkColonizerPurchase();
        checkTransporterPurchase();
        checkColonyProductionPurchase();
        logger.info("Wealth managed.");
    }

    private void initSpaceshipTypesToBuy() {
        shuttleType = freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Shuttle");
        freighterType = freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Freighter");
        bulkFreighterType = freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Bulk freighter");
        spaceshipTypesToBuy = new ArrayList<UnitType>();
        spaceshipTypesToBuy.add(freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Bulk freighter"));
        spaceshipTypesToBuy.add(freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Freighter"));
        spaceshipTypesToBuy.add(freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Shuttle"));
    }

    private void checkColonizerPurchase() {
        UnitType colonizerUnitType = freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Colonizer");
        if (colonizerPurchase()) {
            freeMarsController.execute(new BuyUnitFromEarthCommand(freeMarsController.getFreeMarsModel(), aiPlayer, colonizerUnitType));
        }
    }

    private boolean colonizerPurchase() {
        UnitType colonizerUnitType = freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Colonizer");
        if (freeMarsController.getFreeMarsModel().getEarthFlightModel().getEarthSellsAtPrice(colonizerUnitType) <= aiPlayer.getWealth()) {
            int settlementCount = aiPlayer.getSettlementCount();
            int colonizerCount = aiPlayer.getUnitManager().getUnitsOfTypeCount(colonizerUnitType);
            int colonyAndColonizerTotal = settlementCount + colonizerCount;
            Iterator<Mission> iterator = aiPlayer.getMissionsIterator();
            while (iterator.hasNext()) {
                Mission mission = iterator.next();
                if (mission.getStatus() == Mission.STATUS_ACTIVE && mission instanceof SettlementCountMission) {
                    SettlementCountMission settlementCountMission = (SettlementCountMission) mission;
                    if (settlementCountMission.getSettlementCount() > colonyAndColonizerTotal) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void checkTransporterPurchase() {
        if (freeMarsController.getFreeMarsModel().getNumberOfTurns() > FreeMarsPlayer.FREE_TRANSPORTER_MIN_TURN) {
            UnitType transporterUnitType = freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Transporter");
            int transporterCount = aiPlayer.getUnitManager().getUnitsOfTypeCount(transporterUnitType);
            if (transporterCount < (aiPlayer.getSettlementCount() / 2)) {
                freeMarsController.execute(new BuyUnitFromEarthCommand(freeMarsController.getFreeMarsModel(), aiPlayer, transporterUnitType));
            }
        }
    }

    private void checkSpaceshipPurchase() {
        int shuttleCount = aiPlayer.getUnitsOfType(shuttleType).size();
        if (shuttleCount < 3) {
            int price = freeMarsController.getFreeMarsModel().getEarthFlightModel().getEarthSellsAtPrice(shuttleType);
            if (aiPlayer.getWealth() >= price) {
                freeMarsController.execute(new BuyUnitFromEarthCommand(freeMarsController.getFreeMarsModel(), aiPlayer, shuttleType));
            }
        }
        int freighterCount = aiPlayer.getUnitsOfType(freighterType).size();
        if (freighterCount == 0) {
            int price = freeMarsController.getFreeMarsModel().getEarthFlightModel().getEarthSellsAtPrice(freighterType);
            if (aiPlayer.getWealth() >= price) {
                freeMarsController.execute(new BuyUnitFromEarthCommand(freeMarsController.getFreeMarsModel(), aiPlayer, freighterType));
            }
        }
        int bulkFreighterCount = aiPlayer.getUnitsOfType(bulkFreighterType).size();
        if (bulkFreighterCount == 0) {
            int price = freeMarsController.getFreeMarsModel().getEarthFlightModel().getEarthSellsAtPrice(bulkFreighterType);
            if (aiPlayer.getWealth() >= price) {
                freeMarsController.execute(new BuyUnitFromEarthCommand(freeMarsController.getFreeMarsModel(), aiPlayer, bulkFreighterType));
            }
        }
    }

    private void checkColonyProductionPurchase() {
        logger.info("Checking colony production purchase...");
        SettlementImprovementCountMission settlementImprovementCountMission = getCurrentSettlementImprovementCountMission();
        if (settlementImprovementCountMission != null && aiPlayer.getWealth() > 20000) {
            logger.info("Player " + aiPlayer.getName() + " has an active \"Settlement improvement count mission\".");
            Iterator<Integer> iterator = settlementImprovementCountMission.getTargetImprovementTypesIterator();
            while (iterator.hasNext()) {
                Integer typeId = iterator.next();
                int targetCount = settlementImprovementCountMission.getTargetCountForImprovementType(typeId);
                int currentCount = aiPlayer.getSettlementCountHavingImprovementType(typeId);
                int required = targetCount - currentCount;
                String settlementImprovementTypeName = freeMarsController.getFreeMarsModel().getRealm().getSettlementImprovementManager().getImprovement(typeId).getName();
                logger.info(settlementImprovementTypeName + " - Required : " + required + " Built : " + currentCount);
                if (required > 0) {
                    List<Settlement> settlements = aiPlayer.getSettlementsBuildingImprovementOfType(typeId);
                    for (Settlement settlement : settlements) {
                        logger.info("Buying production for " + settlementImprovementTypeName + " in " + settlement.getName() + ".");
                        freeMarsController.execute(new BuySettlementProductionCommand(freeMarsController.getFreeMarsModel().getRealm(), settlement, 1000));
                        required--;
                        if (required == 0) {
                            break;
                        }
                    }
                }
            }
        } else {
            logger.info("Colony production purchase not needed.");
        }

        SettlementImprovementType starportType = freeMarsController.getFreeMarsModel().getRealm().getSettlementImprovementManager().getImprovement("Starport");
        Iterator<Settlement> iterator = aiPlayer.getSettlementsIterator();
        while (iterator.hasNext()) {
            Settlement settlement = iterator.next();
            if (settlement.getCurrentProduction() != null) {
                if (settlement.hasImprovementType(starportType) && (aiPlayer.getWealth() > 50000)) {
                    freeMarsController.execute(new BuySettlementProductionCommand(freeMarsController.getFreeMarsModel().getRealm(), settlement, 100));
//                } else if (isCurrentProductionMissionRequested(settlement) && (aiPlayer.getWealth() > 30000)) {
//                    freeMarsController.execute(new BuySettlementProductionCommand(freeMarsController.getFreeMarsModel().getRealm(), settlement, 1000));
                } else if ((aiPlayer.getWealth() > 50000)) {
                    freeMarsController.execute(new BuySettlementProductionCommand(freeMarsController.getFreeMarsModel().getRealm(), settlement, 25));
                }
            }
        }
    }
    /*
     private boolean isCurrentProductionMissionRequested(Settlement settlement) {
     if (settlement.getCurrentProduction() instanceof SettlementImprovementType) {
     SettlementImprovementCountMission settlementImprovementCountMission = getCurrentSettlementImprovementCountMission();
     if (settlementImprovementCountMission != null) {
     SettlementImprovementType settlementImprovementType = (SettlementImprovementType) settlement.getCurrentProduction();
     int settlementCountHavingImprovementType = aiPlayer.getSettlementCountHavingImprovementType(settlementImprovementType.getId());
     if (settlementCountHavingImprovementType < settlementImprovementCountMission.getTargetCountForImprovementType(settlementImprovementType.getId())) {
     return true;
     }
     }
     }
     return false;
     }
     */

    private SettlementImprovementCountMission getCurrentSettlementImprovementCountMission() {
        Iterator<Mission> iterator = aiPlayer.getMissionsIterator();
        while (iterator.hasNext()) {
            Mission mission = iterator.next();
            if (mission.getStatus() == Mission.STATUS_ACTIVE && mission instanceof SettlementImprovementCountMission) {
                SettlementImprovementCountMission settlementImprovementCountMission = (SettlementImprovementCountMission) mission;
                return settlementImprovementCountMission;
            }
        }
        return null;
    }

}
