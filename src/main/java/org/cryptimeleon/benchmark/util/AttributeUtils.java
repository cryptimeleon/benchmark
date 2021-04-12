package org.cryptimeleon.benchmark.util;

import org.cryptimeleon.craco.common.attributes.BigIntegerAttribute;
import org.cryptimeleon.craco.common.attributes.SetOfAttributes;

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
