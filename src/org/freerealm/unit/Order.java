package org.freerealm.unit;

import org.freerealm.Realm;
import org.freerealm.executor.Executor;

/**
 *
 * @author Deniz ARIKAN
 */
public interface Order {

    public boolean isExecutable();

    public void execute();

    public String getName();

    public int getRemainingTurns();

    public Realm getRealm();

    public void setRealm(Realm realm);

    public int getTurnGiven();

    public void setTurnGiven(int turnGiven);

    public boolean isComplete();

    public void setComplete(boolean complete);

    public Unit getUnit();

    public void setUnit(Unit unit);

    public String getSymbol();

    public void setSymbol(String symbol);

    public Executor getExecutor();

    public void setExecutor(Executor executor);
}
