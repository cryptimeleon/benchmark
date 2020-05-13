package de.upb.crypto.benchmark.sig.generic;

import de.upb.crypto.craco.interfaces.PlainText;
import de.upb.crypto.craco.interfaces.signature.SignatureKeyPair;
import de.upb.crypto.craco.interfaces.signature.SigningKey;
import de.upb.crypto.craco.interfaces.signature.StandardSignatureScheme;
import de.upb.crypto.craco.interfaces.signature.VerificationKey;

public abstract class SigBenchmarkParams {

    private StandardSignatureScheme scheme;
    private Object publicParameters;

    public SigBenchmarkParams() {
        scheme = null;
        publicParameters = null;
    }

    /**
     * Performs setup of signature scheme, if one exists.
     */
    public abstract void doSetup();

    /**
     * Performs key generation of scheme.
     */
    public abstract SignatureKeyPair<? extends VerificationKey, ? extends SigningKey> generateKeyPair();

    /**
     * Generates a fresh plaintext for testing signing/verification.
     */
    public abstract PlainText generatePlainText();

    public StandardSignatureScheme getScheme() {
        return scheme;
    }

    protected Object getPublicParameters() {
        return publicParameters;
    }

    public void setScheme(StandardSignatureScheme scheme) {
        this.scheme = scheme;
    }

    protected void setPublicParameters(Object publicParameters) {
        this.publicParameters = publicParameters;
    }
}
