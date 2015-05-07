package org.freerealm.executor.command;

import org.freerealm.Realm;
import org.freerealm.executor.CommandResult;
import org.freerealm.tile.Collectable;
import org.freerealm.unit.Unit;

/**
 *
 * @author Deniz ARIKAN
 */
public class ProcessCollectableCommand extends AbstractCommand {

    private Unit unit;
    private Collectable collectable;

    public ProcessCollectableCommand(Realm realm, Unit unit, Collectable collectable) {
        super(realm);
        this.unit = unit;
        this.collectable = collectable;
    }

    public CommandResult execute() {
        collectable.collected(getRealm(), unit);
        CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.COLLECTABLE_PROCESSED_UPDATE);
        commandResult.putParameter("unit", unit);
        commandResult.putParameter("collectable", collectable);
        commandResult.putParameter("collected_coordinate", unit.getCoordinate());
        return commandResult;
    }
}
