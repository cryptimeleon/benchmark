package de.upb.crypto.benchmark.abe.generic;

import de.upb.crypto.craco.interfaces.CipherText;
import de.upb.crypto.craco.interfaces.DecryptionKey;
import de.upb.crypto.craco.interfaces.EncryptionKey;
import de.upb.crypto.craco.interfaces.PlainText;
import de.upb.crypto.craco.interfaces.abe.Attribute;
import de.upb.crypto.craco.interfaces.abe.BigIntegerAttribute;
import de.upb.crypto.craco.interfaces.abe.SetOfAttributes;
import de.upb.crypto.craco.interfaces.pe.PredicateEncryptionScheme;
import de.upb.crypto.craco.interfaces.policy.BooleanPolicy;
import de.upb.crypto.craco.interfaces.policy.Policy;
import de.upb.crypto.craco.interfaces.policy.ThresholdPolicy;

public class ABEBenchmarkConfigBuilder {

    /**
     * The attribute sets with the corresponding policy and the name of the tuple for nice printing.
     */
    private SetOfAttributesPolicyNameTriple[] setOfAttributesPolicyNameTriples;

    /**
     * Whether the scheme being tested is a ciphertext-policy scheme. Makes a difference in the benchmark as
     * the defined policies and attribute sets will be used for encryption/decryption keys, depending on scheme type.
     */
    private boolean isCPABE;
    private boolean isCPABEInitialized;

    /**
     * How many runs through the benchmark process to perform for warming up the JVM.
     */
    private int numWarmupRuns;
    /**
     * How often to do setup, i.e. generate master secret and scheme itself.
     * For each setup done, {@link this#numKeyGenerations} are performed.
     * This way, multiple setups can be tested with multiple runs per setup.
     */
    private int numSetups;
    /**
     * How many times to do the key generation per set up scheme.
     */
    private int numKeyGenerations;
    /**
     * How often to run {@link PredicateEncryptionScheme#encrypt(PlainText, EncryptionKey)}
     * and {@link PredicateEncryptionScheme#decrypt(CipherText, DecryptionKey)} per generated key pair.
     */
    private int numEncDecCycles;

    /**
     * Whether this benchmark run should print execution times and status updates during benchmark.
     */
    private boolean printDetails;

    public ABEBenchmarkConfigBuilder() {
        int[] attributeDefaults = new int[] {2, 4, 8, 16, 32, 64, 128};
        // build set of triples with boolean AND only and OR only policies for each attribute set
        setOfAttributesPolicyNameTriples = new SetOfAttributesPolicyNameTriple[attributeDefaults.length*2];
        for (int i = 0; i < attributeDefaults.length; ++i)  {
            SetOfAttributes validAttributes = new SetOfAttributes();
            for (int j = 0; j < attributeDefaults[i]; ++j) {
                BigIntegerAttribute att = new BigIntegerAttribute(j);
                validAttributes.add(att);
            }
            setOfAttributesPolicyNameTriples[2*i] = new SetOfAttributesPolicyNameTriple(
                    validAttributes,
                    new BooleanPolicy(BooleanPolicy.BooleanOperator.AND, validAttributes),
                    attributeDefaults[i] + " BigInt ALL AND gates"
            );
            setOfAttributesPolicyNameTriples[2*i+1] = new SetOfAttributesPolicyNameTriple(
                    validAttributes,
                    new BooleanPolicy(BooleanPolicy.BooleanOperator.OR, validAttributes),
                    attributeDefaults[i] + " BigInt ALL OR gates"
            );
        }

        numWarmupRuns = 1;
        numSetups = 1;
        numKeyGenerations = 1;
        numEncDecCycles = 1;
        printDetails = true;
    }

    public ABEBenchmarkConfig buildConfig() {
        if (!isCPABEInitialized) {
            throw new IllegalArgumentException("You must set the type of scheme which this config is for, CP or KP.");
        }
        return new ABEBenchmarkConfig(setOfAttributesPolicyNameTriples, isCPABE, numSetups, numKeyGenerations,
                numEncDecCycles, numWarmupRuns, printDetails);
    }

    public ABEBenchmarkConfigBuilder setSetOfAttributesPolicyNameTriples(SetOfAttributesPolicyNameTriple[]
                                                                                 setOfAttributesPolicyNameTriples) {
        this.setOfAttributesPolicyNameTriples = setOfAttributesPolicyNameTriples;
        return this;
    }

    public ABEBenchmarkConfigBuilder setIsCPABE(boolean isCPABE) {
        this.isCPABE = isCPABE;
        this.isCPABEInitialized = true;
        return this;
    }

    public ABEBenchmarkConfigBuilder setNumWarmupRuns(int numWarmupRuns) {
        this.numWarmupRuns = numWarmupRuns;
        return this;
    }

    public ABEBenchmarkConfigBuilder setNumSetups(int numSetups) {
        this.numSetups = numSetups;
        return this;
    }

    public ABEBenchmarkConfigBuilder setNumKeyGenerations(int numKeyGenerations) {
        this.numKeyGenerations = numKeyGenerations;
        return this;
    }

    public ABEBenchmarkConfigBuilder setNumEncDecCycles(int numEncDecCycles) {
        this.numEncDecCycles = numEncDecCycles;
        return this;
    }

    public ABEBenchmarkConfigBuilder setPrintDetails(boolean printDetails) {
        this.printDetails = printDetails;
        return this;
    }
}
