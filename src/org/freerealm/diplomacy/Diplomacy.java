package org.freerealm.diplomacy;

import java.util.HashMap;
import java.util.Iterator;
import org.freerealm.player.Player;

/**
 *
 * @author Deniz ARIKAN
 */
public class Diplomacy {

    public static final int NO_CONTACT = 0;
    public static final int AT_PEACE = 1;
    public static final int AT_WAR = 2;
    public static final int ALLIED = 3;
    private HashMap<PlayerRelation, Integer> playerRelations;

    public Diplomacy() {
        playerRelations = new HashMap<PlayerRelation, Integer>();
    }

    public void addPlayerRelation(Player player1, Player player2, int status) {
        playerRelations.put(new PlayerRelation(player1, player2), status);
    }

    public int getPlayerRelationStatus(Player player1, Player player2) {
        Iterator<PlayerRelation> iterator = playerRelations.keySet().iterator();
        while (iterator.hasNext()) {
            PlayerRelation playerRelation = iterator.next();
            if (playerRelation.equals(new PlayerRelation(player1, player2))) {
                return playerRelations.get(playerRelation);
            }
        }
        return NO_CONTACT;
    }

    public int getPlayerRelationStatus(PlayerRelation checkpPlayerRelation) {
        Iterator<PlayerRelation> iterator = playerRelations.keySet().iterator();
        while (iterator.hasNext()) {
            PlayerRelation playerRelation = iterator.next();
            if (playerRelation.equals(checkpPlayerRelation)) {
                return playerRelations.get(playerRelation);
            }
        }
        return NO_CONTACT;
    }

    public Iterator<PlayerRelation> getPlayerRelationsIterator() {
        return playerRelations.keySet().iterator();
    }
}
