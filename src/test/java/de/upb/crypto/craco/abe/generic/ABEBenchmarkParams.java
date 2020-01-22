package de.upb.crypto.craco.abe.generic;

import de.upb.crypto.craco.interfaces.EncryptionKey;
import de.upb.crypto.craco.interfaces.PlainText;
import de.upb.crypto.craco.interfaces.pe.CiphertextIndex;
import de.upb.crypto.craco.interfaces.pe.KeyIndex;
import de.upb.crypto.craco.interfaces.pe.MasterSecret;
import de.upb.crypto.craco.interfaces.pe.PredicateEncryptionScheme;

/**
 * Parameter class used for the ABE benchmarks.
 *
 * @author Raphael Heitjohann
 */
public abstract class ABEBenchmarkParams {

    private MasterSecret masterSecret;
    /**
     * Public parameters of the current scheme. Not needed by benchmarks but useful for generating
     * parameters such as new messages.
     */
    private Object publicParameters;
    private PredicateEncryptionScheme scheme;

    public ABEBenchmarkParams() {
        masterSecret = null;
        publicParameters = null;
        scheme = null;
    }

    public MasterSecret getMasterSecret() {
        return masterSecret;
    }

    public PredicateEncryptionScheme getScheme() {
        return scheme;
    }

    protected Object getPublicParameters() {
        return publicParameters;
    }

    public void setMasterSecret(MasterSecret masterSecret) {
        this.masterSecret = masterSecret;
    }

    public void setScheme(PredicateEncryptionScheme scheme) {
        this.scheme = scheme;
    }

    protected void setPublicParameters(Object publicParameters) {
        this.publicParameters = publicParameters;
    }

    /**
     * This method should generate the predicate encryption scheme object and
     * the master secret and store them in this class. Then they can be used for benchmarking.
     */
    public abstract void doSetup(KeyIndex kIndex, CiphertextIndex cIndex);

    /**
     * Produces a {@link PlainText} object for testing
     * {@link PredicateEncryptionScheme#encrypt(PlainText, EncryptionKey)}. Ciphertext is then used for decrypting.
     */
    public abstract PlainText generatePlainText();
}
