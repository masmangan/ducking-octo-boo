package org.freerealm.map;

import org.freerealm.unit.Unit;

public interface PathFinder {

    public Path findPath(Unit unit, Coordinate target, boolean ignoreOtherPlayers);

    public Path findPath(Unit unit, Coordinate start, Coordinate target, boolean ignoreOtherPlayers);
}
