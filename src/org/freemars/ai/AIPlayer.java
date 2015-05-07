package org.freemars.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.freemars.player.FreeMarsPlayer;
import org.freerealm.Realm;
import org.freerealm.player.Player;
import org.freerealm.settlement.Settlement;
import org.freerealm.settlement.improvement.SettlementImprovementType;

/**
 *
 * @author Deniz ARIKAN
 */
public class AIPlayer extends FreeMarsPlayer {

    private static final Logger logger = Logger.getLogger(AIPlayer.class);
    private DecisionModel decisionModel;
    private final int maximumColonyCount;

    public AIPlayer(Realm realm) {
        super(realm);
        maximumColonyCount = 12;
    }

    public void play() {
        try {
            if (getStatus() == Player.STATUS_ACTIVE) {
                StringBuilder log = new StringBuilder();
                log.append("AI Player with id ").append(getId()).append(" and name \"").append(getName()).append("\" will play turn ");
                log.append(getRealm().getNumberOfTurns()).append(".");
                logger.info(log);
                long playStart = System.currentTimeMillis();
                getDecisionModel().play();
                long playTime = System.currentTimeMillis() - playStart;
                log.setLength(0);
                log.append("AI Player with id ").append(getId()).append(" and name \"").append(getName()).append("\" has played turn ");
                log.append(getRealm().getNumberOfTurns()).append(" in ").append(playTime).append(" miliseconds.");
                logger.info(log);
            }
        } catch (Exception exception) {
            String logInfo = "Exception while AI Player with id " + getId() + " and name \"" + getName() + "\" is playing turn " + getRealm().getNumberOfTurns() + ".";
            logger.error(logInfo);
            logger.error(exception);
            logger.error("Exception message :");
            logger.error(exception.getMessage());
            System.out.println("**************************************************");
            System.out.println("EXCEPTION EXCEPTION EXCEPTION EXCEPTION EXCEPTION ");
            System.out.println("**************************************************");
            System.exit(0);
        }
    }

    public DecisionModel getDecisionModel() {
        return decisionModel;
    }

    public void setDecisionModel(DecisionModel decisionModel) {
        this.decisionModel = decisionModel;
    }

    public int getMaximumColonyCount() {
        return maximumColonyCount;
    }

    List<Settlement> getSettlementsBuildingImprovementOfType(Integer typeId) {
        List<Settlement> settlementsBuildingImprovementOfType = new ArrayList<Settlement>();
        Iterator<Settlement> iterator = getSettlementsIterator();
        while (iterator.hasNext()) {
            Settlement settlement = iterator.next();
            if (settlement.getCurrentProduction() != null && settlement.getCurrentProduction() instanceof SettlementImprovementType) {
                SettlementImprovementType settlementImprovementType = (SettlementImprovementType) settlement.getCurrentProduction();
                if (settlementImprovementType.getId() == typeId) {
                    settlementsBuildingImprovementOfType.add(settlement);
                }
            }
        }
        return settlementsBuildingImprovementOfType;
    }
}
