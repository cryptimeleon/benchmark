package org.cryptimeleon.benchmark.benchmarks;

import org.cryptimeleon.craco.common.plaintexts.MessageBlock;
import org.cryptimeleon.craco.common.plaintexts.PlainText;
import org.cryptimeleon.craco.common.plaintexts.RingElementPlainText;
import org.cryptimeleon.craco.sig.Signature;
import org.cryptimeleon.craco.sig.SignatureKeyPair;
import org.cryptimeleon.craco.sig.ps.PSPublicParameters;
import org.cryptimeleon.craco.sig.ps18.PS18SignatureScheme;
import org.cryptimeleon.craco.sig.ps18.PS18SigningKey;
import org.cryptimeleon.craco.sig.ps18.PS18VerificationKey;
import org.cryptimeleon.math.structures.groups.counting.CountingBilinearGroup;
import org.cryptimeleon.math.structures.groups.elliptic.BilinearGroup;

public class PS18VerifyCountingBenchmark {

    public static void main(String[] args) {
        int[] numMessageParams = new int[] {1};

        PS18SignatureScheme scheme;
        PlainText plainText;
        Signature signature;
        PS18VerificationKey verificationKey;
        CountingBilinearGroup bilGroup;

        for (int numMessages : numMessageParams) {
            bilGroup = new CountingBilinearGroup(128, BilinearGroup.Type.TYPE_3);
            PSPublicParameters pp = new PSPublicParameters(bilGroup);
            scheme = new PS18SignatureScheme(pp);
            SignatureKeyPair<? extends PS18VerificationKey, ? extends PS18SigningKey> keyPair =
                    scheme.generateKeyPair(numMessages);
            verificationKey = keyPair.getVerificationKey();
            RingElementPlainText[] messages = new RingElementPlainText[numMessages];
            for (int i = 0; i < messages.length; i++) {
                messages[i] = new RingElementPlainText(pp.getZp().getUniformlyRandomElement());
            }
            plainText = new MessageBlock(messages);
            signature = scheme.sign(plainText, keyPair.getSigningKey());
            // Get all representations to force computation
            signature.getRepresentation();
            verificationKey.getRepresentation();
            bilGroup.resetCounters();
            System.out.println("********** Running regular benchmark with numMessages = " + numMessages + " *********");
            scheme.verify(plainText, signature, verificationKey);
            System.out.println(bilGroup.formatCounterData());
            bilGroup.resetCounters();
        }
    }
}
