package org.freerealm.map;

import java.util.ArrayList;

public class Path {

    private ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();

    public Path() {
    }

    @Override
    public String toString() {
        StringBuffer pathStringBuffer = new StringBuffer();
        for (int i = 0; i < getLength(); i++) {
            pathStringBuffer.append(getStep(i).toString());
            pathStringBuffer.append(" - ");
        }
        return pathStringBuffer.toString();
    }

    public int getLength() {
        return coordinates.size();
    }

    public Coordinate getStep(int index) {
        return (Coordinate) coordinates.get(index);
    }

    public int getX(int index) {
        return getStep(index).getAbscissa();
    }

    public int getY(int index) {
        return getStep(index).getOrdinate();
    }

    public void appendStep(int x, int y) {
        coordinates.add(new Coordinate(x, y));
    }

    public void prependStep(int x, int y) {
        coordinates.add(0, new Coordinate(x, y));
    }

    public boolean contains(Coordinate coordinate) {
        return coordinates.contains(coordinate);
    }
}
