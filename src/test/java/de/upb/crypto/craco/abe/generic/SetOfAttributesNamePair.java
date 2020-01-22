package de.upb.crypto.craco.abe.generic;

import de.upb.crypto.craco.interfaces.abe.SetOfAttributes;

public class SetOfAttributesNamePair {

    private SetOfAttributes attributes;
    /**
     * Name for this set of attributes. Makes printing benchmark results nicer.
     */
    private String name;

    public SetOfAttributesNamePair(SetOfAttributes attributes, String name) {
        this.attributes = attributes;
        this.name = name;
    }

    public SetOfAttributes getAttributes() {
        return attributes;
    }

    public String getName() {
        return name;
    }
}
