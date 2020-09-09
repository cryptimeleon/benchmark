package de.upb.crypto.benchmark.benchmarks;

import de.upb.crypto.craco.common.MessageBlock;
import de.upb.crypto.craco.common.RingElementPlainText;
import de.upb.crypto.craco.interfaces.PlainText;
import de.upb.crypto.craco.interfaces.signature.Signature;
import de.upb.crypto.craco.interfaces.signature.SignatureKeyPair;
import de.upb.crypto.craco.interfaces.signature.VerificationKey;
import de.upb.crypto.craco.sig.ps.PSPublicParameters;
import de.upb.crypto.craco.sig.ps18.PS18SignatureScheme;
import de.upb.crypto.craco.sig.ps18.PS18SigningKey;
import de.upb.crypto.craco.sig.ps18.PS18VerificationKey;
import de.upb.crypto.math.factory.BilinearGroup;
import de.upb.crypto.math.factory.BilinearGroupRequirement;
import de.upb.crypto.math.pairings.debug.count.CountingBilinearGroup;
import de.upb.crypto.math.pairings.debug.count.CountingBilinearGroupProvider;

public class PS18VerifyCountingBenchmark {

    public static void main(String[] args) {
        int[] numMessageParams = new int[] {1, 10 , 100};

        PS18SignatureScheme scheme;
        PlainText plainText;
        Signature signature;
        VerificationKey verificationKey;
        CountingBilinearGroup bilGroup;

        for (int numMessages : numMessageParams) {
            bilGroup = (CountingBilinearGroup) new CountingBilinearGroupProvider().provideBilinearGroup(
                    128,
                    new BilinearGroupRequirement(BilinearGroup.Type.TYPE_3)
            );
            PSPublicParameters pp = new PSPublicParameters(bilGroup);
            scheme = new PS18SignatureScheme(pp);
            SignatureKeyPair<? extends PS18VerificationKey, ? extends PS18SigningKey> keyPair =
                    scheme.generateKeyPair(numMessages);
            RingElementPlainText[] messages = new RingElementPlainText[numMessages];
            for (int i = 0; i < messages.length; i++) {
                messages[i] = new RingElementPlainText(pp.getZp().getUniformlyRandomElement());
            }
            plainText = new MessageBlock(messages);
            signature = scheme.sign(plainText, keyPair.getSigningKey());
            verificationKey = keyPair.getVerificationKey();
            bilGroup.resetCounters();
            System.out.println("********** Running benchmark with numMessages = " + numMessages + " *********");
            scheme.verify(plainText, signature, verificationKey);
            System.out.println(bilGroup.formatCounterData());
            bilGroup.resetCounters();
        }
    }
}
