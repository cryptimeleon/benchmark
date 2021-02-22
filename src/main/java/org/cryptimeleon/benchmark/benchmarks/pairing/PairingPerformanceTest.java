package org.cryptimeleon.benchmark.benchmarks.pairing;

import de.upb.crypto.math.expressions.group.GroupElementExpression;
import de.upb.crypto.math.interfaces.structures.Group;
import de.upb.crypto.math.interfaces.structures.GroupElement;
import de.upb.crypto.math.pairings.generic.BilinearMap;
import de.upb.crypto.math.pairings.type1.supersingular.SupersingularBilinearGroup;
import de.upb.crypto.math.pairings.type3.bn.BarretoNaehrigBilinearGroup;
import de.upb.crypto.math.structures.zn.Zn;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;

//@RunWith(value = Parameterized.class)
public class PairingPerformanceTest {
    private BilinearMap pairing;

    private ArrayList<GroupElement> g1Elements;
    private ArrayList<GroupElement> g2Elements;
    private ArrayList<BigInteger> exponents;
    GroupElementExpression expression;

    final int numberOfElements = 1;

    public PairingPerformanceTest(BilinearMap pairing) {
        this.pairing = pairing;
    }

    /*
     * Add pairings to test here.
     */
    //@Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<BilinearMap> initializePairings() {
        ArrayList<BilinearMap> pairings = new ArrayList<>();

        // Supersingular Tate Pairing
        pairings.add(new SupersingularBilinearGroup(80).getBilinearMap());

        // Barreto-Naehrig non-native
        pairings.add(new BarretoNaehrigBilinearGroup(128).getBilinearMap());
        // Barreto-Naehrig non-native, SFC-256
        pairings.add(new BarretoNaehrigBilinearGroup("SFC-256").getBilinearMap());

        return pairings;
    }

    //@Before
    public void setupTest() {
        Group g1 = pairing.getG1();
        Group g2 = pairing.getG2();
        expression = pairing.getGT().expr();

        // Generate test data
        g1Elements = new ArrayList<>();
        g2Elements = new ArrayList<>();
        exponents = new ArrayList<>();

        for (int i = 0; i < numberOfElements; i++) {
            g1Elements.add(g1.getUniformlyRandomElement());
            g2Elements.add(g2.getUniformlyRandomElement());
            exponents.add(new Zn(g1.size()).getUniformlyRandomElement().getInteger());
            expression.opPow(pairing.expr(g1Elements.get(i), g2Elements.get(i)), exponents.get(i));
        }

        System.out.println("Testing " + pairing.getClass().getSimpleName() + " with " + numberOfElements + " pairings...");
    }


    //@Test
    public void evaluatePairing() {
        long referenceTime = System.nanoTime();
        GroupElement result = this.expression.evaluate();
        System.out.println("Time to evaluate: " + (System.nanoTime() - referenceTime) / 1e6 + " ms");

        referenceTime = System.nanoTime();
        for (int i = 0; i < numberOfElements*10; i++) {
            pairing.apply(this.g1Elements.get(0), this.g2Elements.get(0));
        }
        System.out.println("Time to evaluate: " + (System.nanoTime() - referenceTime) / 1e6 / (numberOfElements * 10)  + " ms");

    }
}