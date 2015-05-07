package org.freemars.ai;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.freemars.command.LoadColonistsCommand;
import org.freemars.controller.FreeMarsController;
import org.freerealm.executor.command.TransferResourceCommand;
import org.freerealm.executor.command.UnitAdvanceToCoordinateCommand;
import org.freerealm.resource.Resource;
import org.freerealm.resource.ResourceStorer;
import org.freerealm.settlement.Settlement;
import org.freerealm.unit.Unit;
import org.freerealm.unit.UnitType;

/**
 *
 * @author Deniz ARIKAN
 */
public class TransporterManager {

    private static final Logger logger = Logger.getLogger(TransporterManager.class);

    private final FreeMarsController freeMarsController;
    private final AIPlayer aiPlayer;
    private ArrayList<Resource> exportableResources;
    private final Resource energyResource;
    private final int starportImprovementTypeId;

    public TransporterManager(FreeMarsController freeMarsController, AIPlayer freeMarsAIPlayer) {
        this.freeMarsController = freeMarsController;
        this.aiPlayer = freeMarsAIPlayer;
        initExportableResources();
        energyResource = freeMarsController.getFreeMarsModel().getRealm().getResourceManager().getResource("Energy");
        starportImprovementTypeId = freeMarsController.getFreeMarsModel().getRealm().getSettlementImprovementManager().getImprovement("Starport").getId();
    }

    public void manage() {
        UnitType transporterUnitType = freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Transporter");
        List<Unit> transporters = aiPlayer.getUnitsOfType(transporterUnitType);
        logger.info(transporters.size() + " transporters will be managed.");
        for (Unit transporter : transporters) {
            if (transporter.getCoordinate() != null) {
                logger.info("Managing transporter \"" + transporter.getName() + "\"...");
                Settlement nearestColonyWithStarport = findNearestColonyWithStarport(transporter);
                if (nearestColonyWithStarport != null) {
                    logger.info("Nearest colony with starport is " + nearestColonyWithStarport.getName() + ".");
                    if (transporter.getRemainingCapacity() > 0) {
                        logger.info("Transporter named \"" + transporter.getName() + "\" has remaining capacity for " + transporter.getRemainingCapacity() + " units.");
                        Settlement nearestColonyToGetResources = findColonyToGetResources(transporter, nearestColonyWithStarport);
                        if (nearestColonyToGetResources != null) {
                            logger.info("Nearest colony to get resources is " + nearestColonyToGetResources.getName() + ".");
                            freeMarsController.execute(new UnitAdvanceToCoordinateCommand(freeMarsController.getFreeMarsModel().getRealm(), transporter, nearestColonyToGetResources.getCoordinate()));
                            if (transporter.getCoordinate().equals(nearestColonyToGetResources.getCoordinate())) {
                                transferAllExportableResources(nearestColonyToGetResources, transporter);
                            }
/*
                            if (transporter.getContainedPopulation() > 0) {
                                logger.info("Unloading " + transporter.getContainedPopulation() + " from " + transporter.getName() + ".");
                            }
        */
                        }
                    } else if (transporter.getRemainingCapacity() == 0) {
                        logger.info("Transporter named \"" + transporter.getName() + "\" has no remaining capacity.");
                        if (nearestColonyWithStarport != null) {
                            logger.info("Nearest colony with starport is " + nearestColonyWithStarport.getName() + ".");
                            freeMarsController.execute(new UnitAdvanceToCoordinateCommand(freeMarsController.getFreeMarsModel().getRealm(), transporter, nearestColonyWithStarport.getCoordinate()));
                            if (transporter.getCoordinate().equals(nearestColonyWithStarport.getCoordinate())) {
                                transferAllExportableResources(transporter, nearestColonyWithStarport);
                                /*
                                if (nearestColonyWithStarport.getPopulation() > 500) {
                                    freeMarsController.execute(new LoadColonistsCommand(freeMarsController, nearestColonyWithStarport, transporter, 2));
                                }
                                        */
                            }
                        }
                    }
                }
            }
        }
    }

    private void initExportableResources() {
        exportableResources = new ArrayList<Resource>();
        exportableResources.add(freeMarsController.getFreeMarsModel().getRealm().getResourceManager().getResource("Hydrogen"));
        exportableResources.add(freeMarsController.getFreeMarsModel().getRealm().getResourceManager().getResource("Steel"));
        exportableResources.add(freeMarsController.getFreeMarsModel().getRealm().getResourceManager().getResource("Chemicals"));
        exportableResources.add(freeMarsController.getFreeMarsModel().getRealm().getResourceManager().getResource("Glass"));
        exportableResources.add(freeMarsController.getFreeMarsModel().getRealm().getResourceManager().getResource("Lumber"));
        exportableResources.add(freeMarsController.getFreeMarsModel().getRealm().getResourceManager().getResource("Magnesium"));
        exportableResources.add(freeMarsController.getFreeMarsModel().getRealm().getResourceManager().getResource("Iron"));
        exportableResources.add(freeMarsController.getFreeMarsModel().getRealm().getResourceManager().getResource("Silica"));
        exportableResources.add(freeMarsController.getFreeMarsModel().getRealm().getResourceManager().getResource("Minerals"));
    }

    private Settlement findColonyToGetResources(Unit transporter, Settlement starportColony) {
        Settlement colonyToGetResources = null;
        if (starportColony != null) {
            int totalExportableResourcesQuantity = 0;
            logger.info("Finding colony to get resources for " + transporter.getName() + transporter.getCoordinate() + ".");
            List<Settlement> nearSettlements = freeMarsController.getFreeMarsModel().getRealm().getSettlementsNearCoordinate(transporter.getCoordinate(), 0, 7, aiPlayer);
            logger.info("There are " + nearSettlements.size() + " colonies nearby.");
            for (Settlement settlement : nearSettlements) {
                if (!settlement.hasImprovementType(starportImprovementTypeId)) {
                    int settlementExportableResourcesQuantity = findTotalExportableResourcesInColony(settlement, starportColony);
                    if (settlementExportableResourcesQuantity > totalExportableResourcesQuantity) {
                        colonyToGetResources = settlement;
                        totalExportableResourcesQuantity = settlementExportableResourcesQuantity;
                    }
                }
            }
        }
        return colonyToGetResources;
    }

    private int findTotalExportableResourcesInColony(Settlement settlement, Settlement starportColony) {
        int totalExportableResourcesInColony = 0;
        for (Resource exportableResource : exportableResources) {
            if (starportColony.getRemainingCapacity(exportableResource) > 2000) {
                totalExportableResourcesInColony = totalExportableResourcesInColony + settlement.getResourceQuantity(exportableResource);
            }
        }
        return totalExportableResourcesInColony;
    }

    private void transferAllExportableResources(ResourceStorer source, ResourceStorer destination) {
        freeMarsController.execute(new TransferResourceCommand(source, destination, energyResource, 300));
        for (Resource exportableResource : exportableResources) {
            int amount = source.getResourceQuantity(exportableResource);
            freeMarsController.execute(new TransferResourceCommand(source, destination, exportableResource, amount));
        }
    }

    private Settlement findNearestColonyWithStarport(Unit transporter) {
        for (int i = 0; i < 12; i++) {
            List<Settlement> nearSettlements = freeMarsController.getFreeMarsModel().getRealm().getSettlementsNearCoordinate(transporter.getCoordinate(), i, i + 1, aiPlayer);
            for (Settlement settlement : nearSettlements) {
                if (settlement.hasImprovementType(starportImprovementTypeId)) {
                    return settlement;
                }
            }
        }
        logger.info("No colony with starport found for coordinate " + transporter.getCoordinate() + ".");
        return null;
    }

}
