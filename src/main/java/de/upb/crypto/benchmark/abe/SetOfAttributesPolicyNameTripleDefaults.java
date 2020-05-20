package de.upb.crypto.benchmark.abe;

import de.upb.crypto.benchmark.abe.generic.SetOfAttributesPolicyNameTriple;
import de.upb.crypto.craco.interfaces.abe.BigIntegerAttribute;
import de.upb.crypto.craco.interfaces.abe.SetOfAttributes;
import de.upb.crypto.craco.interfaces.policy.BooleanPolicy;

public class SetOfAttributesPolicyNameTripleDefaults {

    public static SetOfAttributesPolicyNameTriple[] get() {
        int[] attributeDefaults = new int[] {2, 4, 8, 16, 32, 64, 128};
        // build set of triples with boolean AND only and OR only policies for each attribute set
        SetOfAttributesPolicyNameTriple[] setOfAttributesPolicyNameTriples =
                new SetOfAttributesPolicyNameTriple[attributeDefaults.length];
        for (int i = 0; i < attributeDefaults.length; ++i)  {
            SetOfAttributes validAttributes = new SetOfAttributes();
            for (int j = 0; j < attributeDefaults[i]; ++j) {
                BigIntegerAttribute att = new BigIntegerAttribute(j);
                validAttributes.add(att);
            }
            setOfAttributesPolicyNameTriples[i] = new SetOfAttributesPolicyNameTriple(
                    validAttributes,
                    new BooleanPolicy(BooleanPolicy.BooleanOperator.AND, validAttributes),
                    attributeDefaults[i] + " BigInt ALL AND gates"
            );
        }
        return setOfAttributesPolicyNameTriples;
    }
}
