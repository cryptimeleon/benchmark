package de.upb.crypto.benchmark.util;

import de.upb.crypto.craco.interfaces.abe.BigIntegerAttribute;
import de.upb.crypto.craco.interfaces.abe.SetOfAttributes;

public class AttributeUtils {

    public static SetOfAttributes genAttributes(int count) {
        SetOfAttributes attributes = new SetOfAttributes();
        for (int j = 0; j < count; ++j) {
            BigIntegerAttribute att = new BigIntegerAttribute(j);
            attributes.add(att);
        }
        return attributes;
    }
}
