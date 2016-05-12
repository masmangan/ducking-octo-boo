package org.freemars.ai;

import java.util.List;

import org.freemars.controller.FreeMarsController;
import org.freemars.unit.automater.PeppaAutomater;
import org.freemars.unit.automater.ScoutAutomater;
import org.freerealm.executor.command.UnitSetAutomaterCommand;
import org.freerealm.unit.Unit;
import org.freerealm.unit.UnitType;

/**
 *
 * @author Deniz ARIKAN
 */
public class ScoutManager {

    private final FreeMarsController freeMarsController;
    private final AIPlayer aiPlayer;

    public ScoutManager(FreeMarsController freeMarsController, AIPlayer aiPlayer) {
        this.freeMarsController = freeMarsController;
        this.aiPlayer = aiPlayer;
    }

    public void manage() {
        UnitType scoutUnitType = freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Scout");
        List<Unit> scouts = aiPlayer.getUnitsOfType(scoutUnitType);
        for (Unit scout : scouts) {
            if (scout.getAutomater() == null) {
                ScoutAutomater scoutAutomater = new ScoutAutomater();
                scoutAutomater.setFreeMarsController(freeMarsController);
                UnitSetAutomaterCommand unitSetAutomaterCommand = new UnitSetAutomaterCommand(scout, scoutAutomater);
                freeMarsController.execute(unitSetAutomaterCommand);
            }
        }
        
        UnitType peppaUnitType = freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Peppa");
        List<Unit> peppas = aiPlayer.getUnitsOfType(peppaUnitType);
        for (Unit peppa : peppas) {
            if (peppa.getAutomater() == null) {
            	PeppaAutomater peppaAutomater = new PeppaAutomater();
                peppaAutomater.setFreeMarsController(freeMarsController);
                UnitSetAutomaterCommand unitSetAutomaterCommand = new UnitSetAutomaterCommand(peppa, peppaAutomater);
                freeMarsController.execute(unitSetAutomaterCommand);
            }
        }
    }
}
