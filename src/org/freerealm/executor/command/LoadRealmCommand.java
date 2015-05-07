package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.Realm;
import org.w3c.dom.Node;
import org.freerealm.xmlwrapper.RealmXMLWrapper;

/**
 * Command class to load a realm from a node.
 * @author Deniz ARIKAN
 */
public class LoadRealmCommand extends AbstractCommand {

    private Node node;

    /**
     * Constructs a LoadRealmCommand using node<br>
     * @param file
     */
    public LoadRealmCommand(Realm realm, Node node) {
        super(realm);
        this.node = node;
    }

    /**
     * Executes command to load a realm from file.
     * @param realm
     * @return CommandResult
     */
    public CommandResult execute() {
        (new RealmXMLWrapper(getRealm())).initializeFromNode(getRealm(), node);
        return new CommandResult(CommandResult.RESULT_OK, "", CommandResult.REALM_INITIALIZE_UPDATE);
    }
}

