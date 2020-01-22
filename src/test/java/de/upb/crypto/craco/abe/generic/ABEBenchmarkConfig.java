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
     * The attribute sets with the corresponding policy and the name of the tuple for nice printing.
     */
    private SetOfAttributesPolicyNameTriple[] setOfAttributesPolicyNameTriples;

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

    public ABEBenchmarkConfig(SetOfAttributesPolicyNameTriple[] setOfAttributesPolicyNameTriple,
                              boolean isCPABE, int numSetups, int numKeyGenerations, int encDecCycles,
                              int numWarmupRuns, boolean printDetails) {
        this.setOfAttributesPolicyNameTriples = setOfAttributesPolicyNameTriple;
        this.isCPABE = isCPABE;
        this.numWarmupRuns = numWarmupRuns;
        this.numSetups = numSetups;
        this.numKeyGenerations = numKeyGenerations;
        this.encDecCycles = encDecCycles;
        this.printDetails = printDetails;
    }

    public SetOfAttributesPolicyNameTriple[] getSetOfAttributesPolicyNameTriples() {
        return setOfAttributesPolicyNameTriples;
    }

    /**
     * Need two indexes here, in the benchmark we won't know which index is the right one.
     */
    public KeyIndex getKeyIndexAt(int i) {
        if (isCPABE) {
            return setOfAttributesPolicyNameTriples[i].getSetOfAttributes();
        } else {
            return setOfAttributesPolicyNameTriples[i].getPolicy();
        }
    }

    public CiphertextIndex getCiphertextIndexAt(int i) {
        if (isCPABE) {
            return setOfAttributesPolicyNameTriples[i].getPolicy();
        } else {
            return setOfAttributesPolicyNameTriples[i].getSetOfAttributes();
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
