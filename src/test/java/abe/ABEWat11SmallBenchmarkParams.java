package abe;

import de.upb.crypto.benchmark.abe.generic.ABEBenchmarkParams;
import de.upb.crypto.craco.abe.cp.small.ABECPWat11Small;
import de.upb.crypto.craco.abe.cp.small.ABECPWat11SmallPublicParameters;
import de.upb.crypto.craco.abe.cp.small.ABECPWat11SmallSetup;
import de.upb.crypto.craco.common.GroupElementPlainText;
import de.upb.crypto.craco.interfaces.PlainText;
import de.upb.crypto.craco.interfaces.abe.Attribute;
import de.upb.crypto.craco.interfaces.abe.BigIntegerAttribute;
import de.upb.crypto.craco.interfaces.abe.SetOfAttributes;
import de.upb.crypto.craco.interfaces.pe.CiphertextIndex;
import de.upb.crypto.craco.interfaces.pe.KeyIndex;
import de.upb.crypto.craco.interfaces.policy.ThresholdPolicy;


public class ABEWat11SmallBenchmarkParams extends ABEBenchmarkParams {

    @Override
    public void doSetup(KeyIndex kIndex, CiphertextIndex cIndex) {
        if (!(kIndex instanceof SetOfAttributes)) {
            throw new IllegalArgumentException("KeyIndex is not a collection of attributes.");
        }
        ABECPWat11SmallSetup setup = new ABECPWat11SmallSetup();
        setup.doKeyGen(60,(SetOfAttributes) kIndex, false);
        ABECPWat11SmallPublicParameters pp = setup.getPublicParameters();
        this.setPublicParameters(pp);
        this.setMasterSecret(setup.getMasterSecret());
        this.setScheme(new ABECPWat11Small(pp));
    }

    @Override
    public PlainText generatePlainText() {
        return new GroupElementPlainText(
                ((ABECPWat11SmallPublicParameters) this.getPublicParameters()).getGroupGT().getUniformlyRandomNonNeutral()
        );
    }
}
