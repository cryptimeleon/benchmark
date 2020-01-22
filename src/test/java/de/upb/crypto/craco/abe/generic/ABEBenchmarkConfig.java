package de.upb.crypto.craco.abe.generic;

import de.upb.crypto.craco.interfaces.CipherText;
import de.upb.crypto.craco.interfaces.DecryptionKey;
import de.upb.crypto.craco.interfaces.EncryptionKey;
import de.upb.crypto.craco.interfaces.PlainText;
import de.upb.crypto.craco.interfaces.abe.SetOfAttributes;
import de.upb.crypto.craco.interfaces.pe.CiphertextIndex;
import de.upb.crypto.craco.interfaces.pe.KeyIndex;
import de.upb.crypto.craco.interfaces.pe.PredicateEncryptionScheme;

public class ABEBenchmarkConfig {

    /**
     * The attribute sets to test with their names.
     */
    private SetOfAttributesNamePair[] setOfAttributesNamePairs;

    /**
     * The different policies to test with their names.
     */
    private PolicyNamePair[] policyNamePairs;

    /**
     * Whether the scheme being tested is a ciphertext-policy scheme. Makes a difference in the benchmark as
     * the defined policies and attribute sets will be used for encryption/decryption keys, depending on scheme type.
     */
    private boolean isCPABE;

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
    private int encDecCycles;

    /**
     * Whether this benchmark run should print execution times and status updates during benchmark.
     */
    private boolean printDetails;

    public ABEBenchmarkConfig(SetOfAttributesNamePair[] attributeSets, PolicyNamePair[] policies,
                              boolean isCPABE, int numSetups, int numKeyGenerations, int encDecCycles,
                              int numWarmupRuns, boolean printDetails) {
        this.setOfAttributesNamePairs = attributeSets;
        this.policyNamePairs = policies;
        this.isCPABE = isCPABE;
        this.numWarmupRuns = numWarmupRuns;
        this.numSetups = numSetups;
        this.numKeyGenerations = numKeyGenerations;
        this.encDecCycles = encDecCycles;
        this.printDetails = printDetails;
    }

    public SetOfAttributesNamePair[] getSetOfAttributesNamePairs() {
        return setOfAttributesNamePairs;
    }

    public PolicyNamePair[] getPolicyNamePairs() {
        return policyNamePairs;
    }

    /**
     * Need two indexes here, in the benchmark we won't know which index is the right one.
     */
    public KeyIndex getKeyIndexAt(int iAtt, int iPol) {
        if (isCPABE) {
            return setOfAttributesNamePairs[iAtt].getAttributes();
        } else {
            return policyNamePairs[iPol].getPolicy();
        }
    }

    public String getKeyIndexNameAt(int iAtt, int iPol) {
        if (isCPABE) {
            return setOfAttributesNamePairs[iAtt].getName();
        } else {
            return policyNamePairs[iPol].getName();
        }
    }

    public CiphertextIndex getCiphertextIndexAt(int iAtt, int iPol) {
        if (isCPABE) {
            return policyNamePairs[iPol].getPolicy();
        } else {
            return setOfAttributesNamePairs[iAtt].getAttributes();
        }
    }

    public String getCiphertextIndexNameAt(int iAtt, int iPol) {
        if (isCPABE) {
            return policyNamePairs[iPol].getName();
        } else {
            return setOfAttributesNamePairs[iAtt].getName();
        }
    }

    public boolean isCPABE() {
        return isCPABE;
    }

    public int getNumSetups() {
        return numSetups;
    }

    public int getNumWarmupRuns() {
        return numWarmupRuns;
    }

    public int getNumKeyGenerations() {
        return numKeyGenerations;
    }

    public int getEncDecCycles() {
        return encDecCycles;
    }

    public boolean isPrintDetails() {
        return printDetails;
    }
}
