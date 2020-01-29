package de.upb.crypto.craco.sig;

import de.upb.crypto.craco.common.MessageBlock;
import de.upb.crypto.craco.common.RingElementPlainText;
import de.upb.crypto.craco.interfaces.PlainText;
import de.upb.crypto.craco.interfaces.signature.SignatureKeyPair;
import de.upb.crypto.craco.interfaces.signature.SigningKey;
import de.upb.crypto.craco.interfaces.signature.StandardMultiMessageSignatureScheme;
import de.upb.crypto.craco.interfaces.signature.VerificationKey;
import de.upb.crypto.craco.sig.generic.SigBenchmarkParams;
import de.upb.crypto.craco.sig.ps.PSPublicParameters;
import de.upb.crypto.craco.sig.ps.PSPublicParametersGen;
import de.upb.crypto.craco.sig.ps.PSSignatureScheme;
import de.upb.crypto.math.pairings.mcl.MclBilinearGroup;
import de.upb.crypto.math.pairings.mcl.MclPairing;

public class PSBenchmarkParamsMcl extends SigBenchmarkParams {

    private int numMessages = 15;

    @Override
    public void doSetup() {
        PSPublicParameters pp = new PSPublicParameters(new MclBilinearGroup().getBilinearMap());
        PSSignatureScheme psScheme = new PSSignatureScheme(pp);
        this.setScheme(psScheme);
        this.setPublicParameters(pp);
    }

    @Override
    public SignatureKeyPair<? extends VerificationKey, ? extends SigningKey> generateKeyPair() {
        return ((StandardMultiMessageSignatureScheme) this.getScheme()).generateKeyPair(numMessages);
    }

    @Override
    public PlainText generatePlainText() {
        RingElementPlainText[] messages = new RingElementPlainText[numMessages];
        for (int i = 0; i < messages.length; i++) {
            messages[i] = new RingElementPlainText(
                    ((PSPublicParameters) this.getPublicParameters()).getZp().getUniformlyRandomElement()
            );
        }
        return new MessageBlock(messages);
    }
}
