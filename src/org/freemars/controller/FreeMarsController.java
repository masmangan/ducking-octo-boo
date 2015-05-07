package org.freemars.controller;

import org.freemars.controller.viewcommand.ViewCommandExecutionThread;
import org.freemars.controller.listener.MapPanelMouseListener;
import org.freemars.controller.listener.MiniMapPanelMouseListener;
import org.freemars.controller.listener.UnitDetailsPanelMouseListener;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import org.freemars.controller.action.DisplayMapEditorAction;
import org.freemars.controller.action.EndTurnAction;
import org.freemars.controller.action.MiniMapDefaultZoomAction;
import org.freemars.controller.action.DisplayHelpContentsAction;
import org.freemars.controller.action.file.ContinueGameAction;
import org.freemars.controller.action.file.DisplayMainMenuAction;
import org.freemars.controller.action.file.DisplayPreferencesDialogAction;
import org.freemars.controller.action.file.ExitGameAction;
import org.freemars.controller.action.file.LoadGameAction;
import org.freemars.controller.action.file.NewGameAction;
import org.freemars.controller.action.file.QuickLoadGameAction;
import org.freemars.editor.controller.EditorController;
import org.freerealm.executor.DefaultExecutor;
import org.freemars.model.FreeMarsModel;
import org.freemars.ui.mainmenu.MainMenuFrame;
import org.freemars.controller.shortcut.KeyboardShortcutHelper;
import org.freemars.controller.viewcommand.ViewCommand;
import org.freemars.ui.GameFrame;
import org.freemars.ui.wizard.newgame.NewGameWizard;
import org.freemars.model.wizard.newgame.NewGameOptions;
import org.freemars.player.FreeMarsPlayer;
import org.freemars.ui.help.FreeMarsHelpDialog;
import org.freemars.ui.player.preferences.FreeMarsPreferences;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.Command;
import org.freerealm.executor.Executor;
import org.freerealm.unit.Unit;

/**
 * @author Deniz ARIKAN
 */
public class FreeMarsController implements Executor {

    private final FreeMarsModel freeMarsModel;
    private final ActionManager actionManager;
    private JFrame currentFrame;
    private MainMenuFrame mainMenuFrame;
    private GameFrame gameFrame;
    private FreeMarsHelpDialog helpDialog;
    private final AutosaveManager autosaveManager;
    private AutoEndTurnHandler autoEndTurnHandler;
    private final MissionHelper missionHelper;
    private final Executor executor;
    private ViewCommandExecutionThread viewCommandExecutionThread;

    private NewTurnHandler newTurnHandler;
    private ActivePlayerHandler activePlayerHandler;
    private ActiveUnitHandler activeUnitHandler;
    private TurnEndedHandler turnEndedHandler;

    public FreeMarsController() {
        freeMarsModel = new FreeMarsModel();
        actionManager = new ActionManager(this);
        autosaveManager = new AutosaveManager(freeMarsModel);
        missionHelper = new MissionHelper();
        executor = new DefaultExecutor();
        readPreferences();
    }

    public CommandResult execute(Command command) {
        command.setExecutor(this);
        CommandResult commandResult = executor.execute(command);
        update(commandResult);
        return commandResult;
    }

    public void startViewCommandExecutionThread() {
        getViewCommandExecutionThread().start();
    }

    public void executeViewCommand(ViewCommand viewCommand) {
        getViewCommandExecutionThread().addCommandToQueue(viewCommand);
    }

    public void executeViewCommandImmediately(ViewCommand viewCommand) {
        getViewCommandExecutionThread().executeCommand(viewCommand);
    }

    public ViewCommandExecutionThread getViewCommandExecutionThread() {
        if (viewCommandExecutionThread == null) {
            viewCommandExecutionThread = new ViewCommandExecutionThread(this);
        }
        return viewCommandExecutionThread;
    }

    public void updateGameFrame() {
        getGameFrame().update();
    }

    public void updateActions() {
        actionManager.refresh();
    }

    private void update(CommandResult commandResult) {
        if (freeMarsModel.getMode() != FreeMarsModel.SIMULATION_MODE) {
            actionManager.refresh();
        }
        Command command = null;
        int updateType = commandResult.getUpdateType();
        switch (updateType) {
            case CommandResult.NEW_TURN_UPDATE:
                getNewTurnHandler().handleUpdate(this, commandResult);
                break;
            case CommandResult.ACTIVE_PLAYER_UPDATE:
                getActivePlayerHandler().handleUpdate(this, commandResult);
                break;
            case CommandResult.ACTIVE_UNIT_UPDATE:
                getActiveUnitHandler().handleUpdate(this, commandResult);
                break;
            case CommandResult.MISSION_ASSIGNED_UPDATE:
                new MissionAssignedHandler().handleUpdate(this, commandResult);
                break;
            case CommandResult.TURN_ENDED_UPDATE:
                command = getTurnEndedHandler().handleUpdate(this, commandResult);
                break;
            case CommandResult.UNIT_MOVEMENT_UPDATE:
                if (freeMarsModel.getMode() != FreeMarsModel.SIMULATION_MODE) {
                    new UnitMovementHandler().handleUpdate(this, commandResult);
                }
                break;
            case CommandResult.UNIT_STATUS_ACTIVATED_UPDATE:
                new UnitStatusActivatedHandler().handleUpdate(this, commandResult);
                break;
            case CommandResult.UNIT_STATUS_SUSPENDED_UPDATE:
                new UnitStatusSuspendedHandler().handleUpdate(this, commandResult);
                break;
            case CommandResult.EXPLORED_COORDINATES_ADDED_TO_PLAYER_UPDATE:
                new ExploredCoordinatesAddedToPlayerHandler().handleUpdate(this, commandResult);
                break;
            case CommandResult.UNIT_ORDER_ASSIGNED_UPDATE:
                new UnitOrderAssignedHandler().handleUpdate(this, commandResult);
                break;
            case CommandResult.UNIT_ORDER_EXECUTED_UPDATE:
                new UnitOrderExecutedHandler().handleUpdate(this, commandResult);
                break;
            case CommandResult.UNIT_ATTACKED_UPDATE:
                new UnitAttackedHandler().handleUpdate(this, commandResult);
                break;
            case CommandResult.PLAYER_REMOVED_UPDATE:
                new PlayerRemovedHandler().handleUpdate(this, commandResult);
                break;
            case CommandResult.VEGETATION_CHANGED_UPDATE:
                new VegetationChangedHandler().handleUpdate(this, commandResult);
                break;
            case CommandResult.TILE_IMPROVEMENT_ADDED_UPDATE:
                break;
            case CommandResult.PLAYER_END_TURN_UPDATE:
                command = new PlayerEndTurnHandler().handleUpdate(this, commandResult);
                break;
            case CommandResult.NEW_SETTLEMENT_UPDATE:
                new NewSettlementHandler().handleUpdate(this, commandResult);
                break;
            case CommandResult.COLLECTABLE_PROCESSED_UPDATE:
                new CollectableProcessedHandler().handleUpdate(this, commandResult);
                break;
            case CommandResult.REALM_INITIALIZE_UPDATE:
                new RealmInitializeHandler().handleUpdate(this, commandResult);
                break;
            case CommandResult.SETTLEMENT_CAPTURED_UPDATE:
                new SettlementCapturedHandler().handleUpdate(this, commandResult);
                break;
            case CommandResult.UNIT_SKIPPED_UPDATE:
                new UnitSkippedHandler().handleUpdate(this, commandResult);
                break;
            case CommandResult.UNIT_ORDERS_CLEARED_UPDATE:
                new UnitOrdersClearedHandler().handleUpdate(this, commandResult);
                break;
            case CommandResult.UNIT_REMOVED_UPDATE:
                new UnitRemovedHandler().handleUpdate(this, commandResult);
                break;
            case CommandResult.SETTLEMENT_POPULATION_UPDATED:
                new SettlementPopulationUpdatedHandler().handleUpdate(this, commandResult);
                break;
            case FreeMarsModel.PLAYER_DECLARED_INDEPENDENCE_UPDATE:
                new PlayerDeclaredIndependenceHandler().handleUpdate(this, commandResult);
                break;
            case FreeMarsModel.SPACESHIPS_SEIZED_UPDATE:
                new SpaceshipsSeizedHandler().handleUpdate(this, commandResult);
                break;
            case FreeMarsModel.UNITS_SEIZED_UPDATE:
                new UnitsSeizedHandler().handleUpdate(this, commandResult);
                break;
            case FreeMarsModel.EXPEDITIONARY_FORCE_LANDED_UPDATE:
                new ExpeditionaryForceLandedHandler().handleUpdate(this, commandResult);
                break;
            case FreeMarsModel.EXPEDITIONARY_FORCE_CHANGED_UPDATE:
                new ExpeditionaryForceChangedHandler().handleUpdate(this, commandResult);
                break;
            case FreeMarsModel.EARTH_TAX_RATE_CHANGED_UPDATE:
                new EarthTaxRateChangedHandler().handleUpdate(this, commandResult);
                break;
            case FreeMarsModel.RANDOM_EVENT_UPDATE:
                new RandomEventHandler().handleUpdate(this, commandResult);
                break;
        }
        if (command != null) {
            execute(command);
        }
        getAutoEndTurnHandler().handleUpdate(this, commandResult);
        if (freeMarsModel.getMode() != FreeMarsModel.SIMULATION_MODE) {
            if (updateType != CommandResult.NO_UPDATE) {
                if (isHumanPlayerActive()) {
                    updateGameFrame();
                }
            }
        }

    }

    public AutoEndTurnHandler getAutoEndTurnHandler() {
        if (autoEndTurnHandler == null) {
            autoEndTurnHandler = new AutoEndTurnHandler();
        }
        return autoEndTurnHandler;
    }

    public AbstractAction getAction(int actionId) {
        return actionManager.getAction(actionId);
    }

    public boolean isActivateEnabledForUnit(Unit unit) {
        return actionManager.isActivateEnabledForUnit(getFreeMarsModel(), unit);
    }

    public void displayMainMenuFrame() {
        displayFrame(getMainMenuFrame());
    }

    public void displayGameFrame() {
        actionManager.refresh();
        displayFrame(getGameFrame());
    }

    public void displayEditorFrame() {
        EditorController editorController = new EditorController();
        editorController.setQuitEditorAction(new DisplayMainMenuAction(this));
        editorController.displayEditorFrame();
        hideCurrentFrame();
    }

    public void displayMainMenuWindow() {
        getMainMenuFrame().displayMainMenuWindow();
    }

    public void hideMainMenuWindow() {
        getMainMenuFrame().hideMainMenuWindow();
    }

    public NewGameOptions displayNewGameWizard() {
        NewGameWizard newGameWizard = new NewGameWizard(getMainMenuFrame(), this);
        return newGameWizard.showNewGameWizardDialog();
    }

    public boolean isMainMenuFrameVisible() {
        return getMainMenuFrame().isVisible();
    }

    public JFrame getCurrentFrame() {
        return currentFrame;
    }

    public FreeMarsModel getFreeMarsModel() {
        return freeMarsModel;
    }

    public FreeMarsHelpDialog getHelpDialog() {
        if (helpDialog == null) {
            helpDialog = new FreeMarsHelpDialog(getCurrentFrame());
        }
        return helpDialog;
    }

    public void assignMissions(FreeMarsPlayer freeMarsPlayer) {
        missionHelper.assignMissions(this, freeMarsPlayer);
    }

    public void clearMissions() {
        missionHelper.clearMissions();
    }

    public void addMissionAssignment(MissionAssignment missionAssignment) {
        missionHelper.addMissionAssignment(missionAssignment);
    }

    protected AutosaveManager getAutosaveManager() {
        return autosaveManager;
    }

    private void displayFrame(JFrame frame) {
        boolean fullScreen = Boolean.valueOf(getFreeMarsModel().getFreeMarsPreferences().getProperty("full_screen"));
        if (getCurrentFrame() != null) {
            getCurrentFrame().setVisible(false);
            if (!getCurrentFrame().equals(frame)) {
                getCurrentFrame().dispose();
            }
        }
        if (fullScreen) {
            if (!frame.isUndecorated()) {
                frame.setVisible(false);
                frame.dispose();
                frame.setUndecorated(true);
            }
            if (frame.isResizable()) {
                frame.setResizable(false);
            }
        } else {
            if (frame.isUndecorated()) {
                frame.setVisible(false);
                frame.dispose();
                frame.setUndecorated(false);
            }
            if (!frame.isResizable()) {
                frame.setResizable(true);
            }
            frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        }
        frame.setVisible(true);
        setCurrentFrame(frame);
    }

    public void reDisplayCurrentFrame() {
        boolean isMainMenuDisplayed = false;
        if (getMainMenuFrame().isVisible() && getMainMenuFrame().getMenuWindow().isVisible()) {
            isMainMenuDisplayed = true;
        }
        displayFrame(getCurrentFrame());
        if (isMainMenuDisplayed) {
            getMainMenuFrame().displayMainMenuWindow();
        }
    }

    private void hideCurrentFrame() {
        if (getCurrentFrame() != null) {
            getCurrentFrame().setVisible(false);
            setCurrentFrame(null);
        }
    }

    private void setCurrentFrame(JFrame frame) {
        this.currentFrame = frame;
    }

    private MainMenuFrame getMainMenuFrame() {
        if (mainMenuFrame == null) {
            mainMenuFrame = new MainMenuFrame();
            mainMenuFrame.getMenuWindow().setContinueButtonAction(new ContinueGameAction(this));
            mainMenuFrame.getMenuWindow().setNewButtonAction(new NewGameAction(this));
            mainMenuFrame.getMenuWindow().setOpenButtonAction(new LoadGameAction(this));
            mainMenuFrame.getMenuWindow().setQuickLoadButtonAction(new QuickLoadGameAction(this));
            mainMenuFrame.getMenuWindow().setPreferencesButtonAction(new DisplayPreferencesDialogAction(this));
            mainMenuFrame.getMenuWindow().setEditorButtonAction(new DisplayMapEditorAction(this));
            mainMenuFrame.getMenuWindow().setMarsopediaButtonAction(new DisplayHelpContentsAction(this, null));
            mainMenuFrame.getMenuWindow().setExitButtonAction(new ExitGameAction(this));
        }
        return mainMenuFrame;
    }

    public GameFrame getGameFrame() {
        if (gameFrame == null) {
            gameFrame = new GameFrame(getFreeMarsModel());
            initGameFrame();
        }
        return gameFrame;
    }

    public void initGameFrame() {
        freeMarsModel.setGameFrame(getGameFrame());
        KeyboardShortcutHelper.assignKeyboardShortcuts(getGameFrame().getMapPanel(), this);
        MenuActionAssignmentHelper.assignMenuActions(this, getGameFrame());
        MapPanelMouseListener mapPanelMouseListener = new MapPanelMouseListener(this, getGameFrame().getMapPanel());
        getGameFrame().getMapPanel().addMouseListener(mapPanelMouseListener);
        getGameFrame().getMapPanel().addMouseMotionListener(mapPanelMouseListener);
        getGameFrame().getMapPanel().addMouseWheelListener(mapPanelMouseListener);
        getGameFrame().getMiniMapPanel().addMouseListener(new MiniMapPanelMouseListener(this, getGameFrame().getMiniMapPanel()));
        getGameFrame().setMiniMapZoomInButtonAction(getAction(ActionManager.MINI_MAP_ZOOM_IN_ACTION));
        getGameFrame().setMiniMapZoomOutButtonAction(getAction(ActionManager.MINI_MAP_ZOOM_OUT_ACTION));
        getGameFrame().setMiniMapDefaultZoomButtonAction(new MiniMapDefaultZoomAction(this));
        getGameFrame().setEndTurnButtonAction(new EndTurnAction(this));
        getGameFrame().setUnitDetailsPanelMouseListener(new UnitDetailsPanelMouseListener(this));
    }

    private void readPreferences() {
        FreeMarsPreferences freeMarsPreferences = new FreeMarsPreferences();
        freeMarsPreferences.readPreferences();
        getFreeMarsModel().setFreeMarsPreferences(freeMarsPreferences);
    }

    public boolean isHumanPlayerActive() {
        if (freeMarsModel.getHumanPlayer() == null) {
            return false;
        }
        if (freeMarsModel.getActivePlayer() == null) {
            return false;
        }
        return freeMarsModel.getHumanPlayer().equals(freeMarsModel.getActivePlayer());
    }

    private NewTurnHandler getNewTurnHandler() {
        if (newTurnHandler == null) {
            newTurnHandler = new NewTurnHandler();
        }
        return newTurnHandler;
    }

    private ActivePlayerHandler getActivePlayerHandler() {
        if (activePlayerHandler == null) {
            activePlayerHandler = new ActivePlayerHandler();
        }
        return activePlayerHandler;
    }

    private ActiveUnitHandler getActiveUnitHandler() {
        if (activeUnitHandler == null) {
            activeUnitHandler = new ActiveUnitHandler();
        }
        return activeUnitHandler;
    }

    private TurnEndedHandler getTurnEndedHandler() {
        if (turnEndedHandler == null) {
            turnEndedHandler = new TurnEndedHandler();
        }
        return turnEndedHandler;
    }
}
