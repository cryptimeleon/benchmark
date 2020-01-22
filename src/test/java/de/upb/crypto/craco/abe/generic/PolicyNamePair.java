package de.upb.crypto.craco.abe.generic;

import de.upb.crypto.craco.interfaces.policy.Policy;

public class PolicyNamePair {

    private Policy policy;
    private String name;

    public PolicyNamePair(Policy policy, String name) {
        this.policy = policy;
        this.name = name;
    }

    public Policy getPolicy() {
        return policy;
    }

    public String getName() {
        return name;
    }
}
