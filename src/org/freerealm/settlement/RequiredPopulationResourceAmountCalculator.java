package org.freerealm.settlement;

import org.freerealm.Realm;
import org.freerealm.modifier.GeneralModifier;
import org.freerealm.resource.Resource;

/**
 *
 * @author Deniz ARIKAN
 */
public class RequiredPopulationResourceAmountCalculator {

    private Realm realm;
    private int resourceId;
    private GeneralModifier[] modifiers;

    public RequiredPopulationResourceAmountCalculator(Realm realm, int resourceId, GeneralModifier[] modifiers) {
        this.realm = realm;
        this.resourceId = resourceId;
        this.modifiers = modifiers;
    }

    public RequiredPopulationResourceAmountCalculator(Realm realm, Resource resource, GeneralModifier[] modifiers) {
        this(realm, resource.getId(), modifiers);
    }

    public int getRequiredPopulationResourceAmount() {
        int requiredPopulationResourceAmount = realm.getRequiredPopulationResourceAmount(resourceId) + getModifier(resourceId);
        if (requiredPopulationResourceAmount < 0) {
            requiredPopulationResourceAmount = 0;
        }
        return requiredPopulationResourceAmount;
    }

    private int getModifier(int resourceId) {
        int modifier = 0;
        for (int i = 0; i < modifiers.length; i++) {
            GeneralModifier generalModifier = modifiers[i];
            modifier = modifier + generalModifier.getRequiredPopulationResourceAmountModifier(resourceId);
        }
        return modifier;
    }
}
