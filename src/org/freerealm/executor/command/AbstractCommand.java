package org.freerealm.executor.command;

import java.util.Properties;
import org.freerealm.Realm;
import org.freerealm.executor.Command;
import org.freerealm.executor.Executor;

/**
 *
 * @author Deniz ARIKAN
 */
public abstract class AbstractCommand implements Command {

    private Executor executor;
    private Realm realm;
    private Properties properties;

    public AbstractCommand() {
    }

    public AbstractCommand(Realm realm) {
        this.realm = realm;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
        }
        return properties;
    }

    public void addProperty(Object key, Object value) {
        getProperties().put(key, value);
    }

    public Object getProperty(Object key) {
        return getProperties().get(key);
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    protected Realm getRealm() {
        return realm;
    }

}
