package org.cryptimeleon.benchmark.craco.abe.Wat11AsymSmall;

import org.cryptimeleon.benchmark.util.AttributeUtils;
import org.cryptimeleon.craco.common.plaintexts.GroupElementPlainText;
import org.cryptimeleon.craco.common.policies.BooleanPolicy;
import org.cryptimeleon.math.structures.groups.elliptic.type3.mcl.MclBilinearGroup;
import org.cryptimeleon.predenc.abe.cp.small.asymmetric.ABECPWat11AsymSmall;
import org.cryptimeleon.predenc.abe.cp.small.asymmetric.ABECPWat11AsymSmallSetup;
import org.openjdk.jmh.annotations.Setup;

public class ABEEncBenchmarkABECPWat11AsymSmallWithMcl extends ABEEncBenchmarkABECPWat11AsymSmall {

    @Setup
    public void setup() {
        // setup is always run before each iteration sequence, so before first warmup iteration
        ABECPWat11AsymSmallSetup setup = new ABECPWat11AsymSmallSetup();
        setup.doKeyGen(new MclBilinearGroup(), AttributeUtils.genAttributes(attrSize));
        scheme = new ABECPWat11AsymSmall(setup.getPublicParameters());
        encKey = scheme.generateEncryptionKey(
                new BooleanPolicy(BooleanPolicy.BooleanOperator.AND, AttributeUtils.genAttributes(attrSize))
        );
        plainText = new GroupElementPlainText(
                setup.getPublicParameters().getGroupGT().getUniformlyRandomNonNeutral()
        );
    }
}
