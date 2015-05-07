package org.freemars.ai;

import java.util.Iterator;
import org.freemars.colony.FreeMarsColony;
import org.freemars.controller.FreeMarsController;
import org.freerealm.settlement.improvement.SettlementImprovementType;
import org.freerealm.executor.command.AddToSettlementProductionQueueCommand;
import org.freerealm.executor.command.ClearSettlementProductionQueueCommand;
import org.freerealm.player.mission.Mission;
import org.freerealm.player.mission.SettlementImprovementCountMission;
import org.freerealm.resource.Resource;
import org.freerealm.settlement.Settlement;
import org.freerealm.settlement.SettlementBuildable;
import org.freerealm.unit.UnitType;

/**
 *
 * @author Deniz ARIKAN
 */
public class ColonyProductionManager {

    private final AIPlayer aiPlayer;
    private final FreeMarsController freeMarsController;

    public ColonyProductionManager(FreeMarsController freeMarsController, AIPlayer aiPlayer) {
        this.freeMarsController = freeMarsController;
        this.aiPlayer = aiPlayer;
    }

    protected void manage(FreeMarsColony freeMarsColony) {
        SettlementBuildable settlementBuildable = getFutureProduction(freeMarsColony);
        if (settlementBuildable != null) {
            freeMarsController.execute(new ClearSettlementProductionQueueCommand(freeMarsColony));
            freeMarsController.execute(new AddToSettlementProductionQueueCommand(freeMarsColony, settlementBuildable));
        }
    }

    private SettlementBuildable getFutureProduction(FreeMarsColony freeMarsColony) {

        SettlementImprovementCountMission settlementImprovementCountMission = getCurrentSettlementImprovementCountMission();
        if (settlementImprovementCountMission != null) {
            Iterator<Integer> iterator = settlementImprovementCountMission.getTargetImprovementTypesIterator();
            while (iterator.hasNext()) {
                Integer settlementImprovementTypeId = iterator.next();
                SettlementImprovementType settlementImprovementType = freeMarsController.getFreeMarsModel().getRealm().getSettlementImprovementManager().getImprovement(settlementImprovementTypeId);
                int required = settlementImprovementCountMission.getTargetCountForImprovementType(settlementImprovementTypeId);
                int currentCount = aiPlayer.getSettlementCountHavingImprovementType(settlementImprovementType.getId());
                if (currentCount < required) {
                    if (freeMarsColony.canBuild(settlementImprovementType)) {
                        return settlementImprovementType;
                    }
                }
            }
        }

        SettlementImprovementType granaryType = freeMarsController.getFreeMarsModel().getRealm().getSettlementImprovementManager().getImprovement("Granary");
        if (freeMarsColony.canBuild(granaryType)) {
            return granaryType;
        }

        SettlementImprovementType waterExtractorType = freeMarsController.getFreeMarsModel().getRealm().getSettlementImprovementManager().getImprovement("Water extractor");
        if (freeMarsColony.canBuild(waterExtractorType)) {
            return waterExtractorType;
        }
        if (freeMarsColony.getPopulation() > 200) {
            SettlementImprovementType solarArrayType = freeMarsController.getFreeMarsModel().getRealm().getSettlementImprovementManager().getImprovement("Solar array");
            if (freeMarsColony.canBuild(solarArrayType)) {
                return solarArrayType;
            }
        }
        SettlementImprovementType hydrogenCollectorType = freeMarsController.getFreeMarsModel().getRealm().getSettlementImprovementManager().getImprovement("Hydrogen collector");
        if (freeMarsColony.canBuild(hydrogenCollectorType)) {
            return hydrogenCollectorType;
        }

        if (freeMarsColony.getPopulation() > 400) {
            SettlementImprovementType foodSiloType = freeMarsController.getFreeMarsModel().getRealm().getSettlementImprovementManager().getImprovement("Food silo");
            if (freeMarsColony.canBuild(foodSiloType)) {
                return foodSiloType;
            }
        }

        Resource ironResource = freeMarsController.getFreeMarsModel().getRealm().getResourceManager().getResource("Iron");
        if (freeMarsColony.getResourceProductionFromTerrain(ironResource) > 80) {
            SettlementImprovementType steelWorksType = freeMarsController.getFreeMarsModel().getRealm().getSettlementImprovementManager().getImprovement("Steel works");
            if (freeMarsColony.canBuild(steelWorksType)) {
                return steelWorksType;
            }
        }

        Resource silicaResource = freeMarsController.getFreeMarsModel().getRealm().getResourceManager().getResource("Silica");
        if (freeMarsColony.getResourceProductionFromTerrain(silicaResource) > 80) {
            SettlementImprovementType glassWorksType = freeMarsController.getFreeMarsModel().getRealm().getSettlementImprovementManager().getImprovement("Glass works");
            if (freeMarsColony.canBuild(glassWorksType)) {
                return glassWorksType;
            }
        }

        Resource mineralsResource = freeMarsController.getFreeMarsModel().getRealm().getResourceManager().getResource("Minerals");
        if (freeMarsColony.getResourceQuantity(mineralsResource) > 5000 || freeMarsColony.getResourceProductionFromTerrain(mineralsResource) > 50) {
            SettlementImprovementType chemicalWorksType = freeMarsController.getFreeMarsModel().getRealm().getSettlementImprovementManager().getImprovement("Chemical works");
            if (freeMarsColony.canBuild(chemicalWorksType)) {
                return chemicalWorksType;
            }
        }

        UnitType engineerUnitType = freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Engineer");
        int engineerCount = aiPlayer.getUnitManager().getUnitsOfTypeCount(engineerUnitType);
        int engineerProductionCount = aiPlayer.getSettlementManager().getSettlementsProducingBuildableCount(engineerUnitType);
        int engineersRequiredCount = ((int) Math.ceil((double) aiPlayer.getSettlementCount() / 3)) + 1;
        if (engineersRequiredCount > engineerCount + engineerProductionCount) {
            return engineerUnitType;
        }
        SettlementImprovementType workshopType = freeMarsController.getFreeMarsModel().getRealm().getSettlementImprovementManager().getImprovement("Workshop");
        if (freeMarsColony.canBuild(workshopType)) {
            return workshopType;
        }
        UnitType transporterUnitType = freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Transporter");
        int transporterCount = aiPlayer.getUnitManager().getUnitsOfTypeCount(transporterUnitType);
        int transporterProductionCount = aiPlayer.getSettlementManager().getSettlementsProducingBuildableCount(transporterUnitType);
        if (transporterCount + transporterProductionCount < 6) {
            return transporterUnitType;
        }

        SettlementImprovementType wallType = freeMarsController.getFreeMarsModel().getRealm().getSettlementImprovementManager().getImprovement("Wall");
        if (freeMarsColony.canBuild(wallType)) {
            return wallType;
        }
        return null;
    }

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
