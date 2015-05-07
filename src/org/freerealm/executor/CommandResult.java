package org.freerealm.executor;

import java.util.HashMap;

/**
 * Class used to contain command execution results that have a return code and
 * text message. Execution result return code can be <tt>RESULT_OK</tt> or
 * <tt>RESULT_ERROR</tt>.
 *
 * @author Deniz ARIKAN
 */
public class CommandResult {

    /**
     * Code meaning execution is completed successfully.
     */
    public static int RESULT_OK = 0;

    /**
     * Code meaning execution has failed.
     */
    public static int RESULT_ERROR = 1;

    /**
     * Code meaning that execution of command resulted in no update to realm.
     */
    public static final int NO_UPDATE = 0;

    /**
     * Code meaning that execution of command resulted initialization of realm.
     */
    public static final int REALM_INITIALIZE_UPDATE = 1;

    /**
     * Code meaning that execution of command changed active player of realm.
     */
    public static final int ACTIVE_PLAYER_UPDATE = 2;

    /**
     * Code meaning that execution of command changed active unit of player.
     */
    public static final int ACTIVE_UNIT_UPDATE = 3;

    /**
     * Code meaning that a turn has ended.
     */
    public static final int TURN_ENDED_UPDATE = 4;

    /**
     * Code meaning that new turn has started.
     */
    public static final int NEW_TURN_UPDATE = 5;

    /**
     * Code meaning that a player has ended its turn.
     */
    public static final int PLAYER_END_TURN_UPDATE = 6;

    /**
     * Code meaning that new settlement has been built.
     */
    public static final int NEW_SETTLEMENT_UPDATE = 7;

    /**
     * Code meaning that a unit has moved.
     */
    public static final int UNIT_MOVEMENT_UPDATE = 8;

    /**
     * Code meaning that explored coordinates have been added to a player.
     */
    public static final int EXPLORED_COORDINATES_ADDED_TO_PLAYER_UPDATE = 9;

    /**
     * Code meaning that an order has been assigned to a unit.
     */
    public static final int UNIT_ORDER_ASSIGNED_UPDATE = 10;

    /**
     * Code meaning that an order has been executed by a unit. Code does not
     * mean that order has been finished, unit may or may not continue to
     * execute the order.
     */
    public static final int UNIT_ORDER_EXECUTED_UPDATE = 11;

    /**
     * Code meaning that diplomacy status between any two players has changed.
     */
    public static final int DIPLOMATIC_STATUS_UPDATE = 12;

    /**
     * Code meaning that a mission has been assigned to a player.
     */
    public static final int MISSION_ASSIGNED_UPDATE = 13;

    /**
     * Code meaning that a mission has been removed from a player.
     */
    public static final int MISSION_REMOVED_UPDATE = 14;

    /**
     * Code meaning that an improvement has been added to a tile.
     */
    public static final int TILE_IMPROVEMENT_ADDED_UPDATE = 15;

    /**
     * Code meaning that vegetation of a tile has changed or has been removed.
     */
    public static final int VEGETATION_CHANGED_UPDATE = 16;

    /**
     * Code meaning that a unit's orders have been cleared.
     */
    public static final int UNIT_ORDERS_CLEARED_UPDATE = 17;

    /**
     * Code meaning that a unit attacked another unit.
     */
    public static final int UNIT_ATTACKED_UPDATE = 18;

    /**
     * Code meaning that a unit is skipped.
     */
    public static final int UNIT_SKIPPED_UPDATE = 19;

    /**
     * Code meaning that a unit's status is set to activated.
     */
    public static final int UNIT_STATUS_ACTIVATED_UPDATE = 20;

    /**
     * Code meaning that a unit's status is set to suspended.
     */
    public static final int UNIT_STATUS_SUSPENDED_UPDATE = 21;

    /**
     * Code meaning that a unit has been removed from realm.
     */
    public static final int UNIT_REMOVED_UPDATE = 22;

    /**
     * Code meaning that a unit attacked another unit.
     */
    public static final int SETTLEMENT_CAPTURED_UPDATE = 23;

    /**
     * Code meaning that a player has been removed from the realm.
     */
    public static final int PLAYER_REMOVED_UPDATE = 24;

    /**
     * Code meaning that a player's wealth has increased.
     */
    public static final int PLAYER_WEALTH_ADDED_UPDATE = 25;

    /**
     * Code meaning that a player's wealth has decreased.
     */
    public static final int PLAYER_WEALTH_REMOVED_UPDATE = 26;

    /**
     * Code meaning that a collectable has been processed.
     */
    public static final int COLLECTABLE_PROCESSED_UPDATE = 27;

    /**
     * Code meaning that a quantity of resource has been transferred.
     */
    public static final int RESOURCE_TRANSFERRED_UPDATE = 28;

    /**
     * Code meaning that population of a settlement has been updated.
     */
    public static final int SETTLEMENT_POPULATION_UPDATED = 29;

    /**
     * Constructs a new CommandResult with given code and text.
     *
     * @param code Code of new CommandResult
     * @param text Return text of new CommandResult
     */
    private int code;
    private String text;
    private int updateType = NO_UPDATE;
    private HashMap<String, Object> parameters;

    public CommandResult(int code, String text) {
        this.code = code;
        this.text = text;
        parameters = new HashMap<String, Object>();
    }

    public CommandResult(int code, String text, int updateType) {
        this(code, text);
        this.updateType = updateType;
    }

    /**
     * Used to get Code of command execution result.
     *
     * @return int
     */
    public int getCode() {
        return code;
    }

    /**
     * Used to get Text of command execution result.
     *
     * @return String
     */
    public String getText() {
        return text;
    }

    /**
     * @return the updateType
     */
    public int getUpdateType() {
        return updateType;
    }

    public void putParameter(String parameterName, Object parameterValue) {
        parameters.put(parameterName, parameterValue);
    }

    public Object getParameter(String parameterName) {
        try {
            return parameters.get(parameterName);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
}
