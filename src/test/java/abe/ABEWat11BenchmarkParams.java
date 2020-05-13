package abe;

import de.upb.crypto.benchmark.abe.generic.ABEBenchmarkParams;
import de.upb.crypto.craco.abe.cp.large.ABECPWat11;
import de.upb.crypto.craco.abe.cp.large.ABECPWat11PublicParameters;
import de.upb.crypto.craco.abe.cp.large.ABECPWat11Setup;
import de.upb.crypto.craco.common.GroupElementPlainText;
import de.upb.crypto.craco.interfaces.PlainText;
import de.upb.crypto.craco.interfaces.pe.CiphertextIndex;
import de.upb.crypto.craco.interfaces.pe.KeyIndex;


/**
 * Benchmark parameters
 * for the large universe construction of {@link ABECPWat11}}
 * <p>
 * [Wat11] Brent Waters. Ciphertext-policy attribute-based encryption: An
 * expressive, efficient, and provably secure realization. In Public Key
 * Cryptography, pages 53–70. Springer, 2011
 *
 * @author Raphael Heitjohann, adapted from Fabian Eidens' original benchmark
 */
public class ABEWat11BenchmarkParams extends ABEBenchmarkParams {

    @Override
    public void doSetup(KeyIndex kIndex, CiphertextIndex cIndex) {
        ABECPWat11Setup cpSetup = new ABECPWat11Setup();
        cpSetup.doKeyGenRandomOracle(60);
        ABECPWat11PublicParameters pp = cpSetup.getPublicParameters();
        this.setScheme(new ABECPWat11(pp));
        this.setMasterSecret(cpSetup.getMasterSecret());
        this.setPublicParameters(pp);
    }

    @Override
    public PlainText generatePlainText() {
        return new GroupElementPlainText(
                ((ABECPWat11PublicParameters) this.getPublicParameters()).getGroupGT().getUniformlyRandomNonNeutral()
        );
    }
}
