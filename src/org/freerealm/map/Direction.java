package org.freerealm.map;

/**
 *
 * @author Deniz ARIKAN
 */
public class Direction {

    private int id;
    private String shortName;

    public Direction(int id, String shortName) {
        this.id = id;
        this.shortName = shortName;
    }

    @Override
    public String toString() {
        return getShortName();
    }

    public int getId() {
        return id;
    }

    public String getShortName() {
        return shortName;
    }
}
