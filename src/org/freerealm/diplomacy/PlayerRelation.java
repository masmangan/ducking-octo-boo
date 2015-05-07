package org.freerealm.diplomacy;

import org.freerealm.player.Player;

/**
 *
 * @author Deniz ARIKAN
 */
public class PlayerRelation implements Comparable<PlayerRelation> {

    private Player player1;
    private Player player2;

    public PlayerRelation(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public boolean equals(PlayerRelation playerRelation) {
        if (getPlayer1().equals(playerRelation.getPlayer1()) && getPlayer2().equals(playerRelation.getPlayer2())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return getPlayer1() + " - " + getPlayer2();
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int compareTo(PlayerRelation playerRelation) {
        if (getPlayer1().equals(playerRelation.getPlayer1()) && getPlayer2().equals(playerRelation.getPlayer2())) {
            return 0;
        }
        if (getPlayer1().equals(playerRelation.getPlayer2()) && getPlayer2().equals(playerRelation.getPlayer1())) {
            return 0;
        }
        return 1;
    }
}
