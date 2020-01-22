package de.upb.crypto.craco.abe.generic;

import de.upb.crypto.craco.interfaces.abe.SetOfAttributes;
import de.upb.crypto.craco.interfaces.policy.Policy;

public class SetOfAttributesPolicyNameTriple {

    private SetOfAttributes setOfAttributes;
    private Policy policy;
    private String name;

    public SetOfAttributesPolicyNameTriple(SetOfAttributes setOfAttributes, Policy policy, String name) {
        this.setOfAttributes = setOfAttributes;
        this.policy = policy;
        this.name = name;
    }

    public SetOfAttributes getSetOfAttributes() {
        return setOfAttributes;
    }

    public Policy getPolicy() {
        return policy;
    }

    public String getName() {
        return name;
    }
}
