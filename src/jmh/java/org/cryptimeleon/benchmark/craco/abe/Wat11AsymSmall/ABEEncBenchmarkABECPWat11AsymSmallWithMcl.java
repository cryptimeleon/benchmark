package org.cryptimeleon.benchmark.craco.abe.Wat11AsymSmall;

import org.cryptimeleon.benchmark.util.AttributeUtils;
import de.upb.crypto.craco.abe.cp.small.asymmetric.ABECPWat11AsymSmall;
import de.upb.crypto.craco.abe.cp.small.asymmetric.ABECPWat11AsymSmallSetup;
import de.upb.crypto.craco.common.GroupElementPlainText;
import de.upb.crypto.craco.common.interfaces.policy.BooleanPolicy;
import de.upb.crypto.math.pairings.mcl.MclBilinearGroupProvider;
import org.openjdk.jmh.annotations.Setup;

public class ABEEncBenchmarkABECPWat11AsymSmallWithMcl extends ABEEncBenchmarkABECPWat11AsymSmall {

    @Setup
    public void setup() {
        // setup is always run before each iteration sequence, so before first warmup iteration
        ABECPWat11AsymSmallSetup setup = new ABECPWat11AsymSmallSetup();
        setup.doKeyGen(new MclBilinearGroupProvider().provideBilinearGroup(), AttributeUtils.genAttributes(attrSize));
        scheme = new ABECPWat11AsymSmall(setup.getPublicParameters());
        encKey = scheme.generateEncryptionKey(
                new BooleanPolicy(BooleanPolicy.BooleanOperator.AND, AttributeUtils.genAttributes(attrSize))
        );
        plainText = new GroupElementPlainText(
                setup.getPublicParameters().getGroupGT().getUniformlyRandomNonNeutral()
        );
    }
}
