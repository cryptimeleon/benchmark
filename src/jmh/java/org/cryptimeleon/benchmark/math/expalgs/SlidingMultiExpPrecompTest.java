package org.cryptimeleon.benchmark.math.expalgs;

import de.upb.crypto.math.interfaces.structures.Group;
import de.upb.crypto.math.interfaces.structures.group.impl.GroupElementImpl;
import de.upb.crypto.math.pairings.generic.BilinearGroup;
import de.upb.crypto.math.pairings.mcl.MclBilinearGroup;
import de.upb.crypto.math.pairings.mcl.MclBilinearGroupImpl;
import de.upb.crypto.math.structures.groups.exp.ExponentiationAlgorithms;
import de.upb.crypto.math.structures.groups.exp.MultiExpTerm;
import de.upb.crypto.math.structures.groups.exp.Multiexponentiation;
import de.upb.crypto.math.structures.groups.lazy.LazyBilinearGroup;
import de.upb.crypto.math.structures.groups.lazy.LazyGroupElement;
import org.openjdk.jmh.annotations.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Tests whether precomputation of negative exponents functions correctly. Removing the
 * precomputation in the setup method should make it much slower since then precomputation
 * needs to be done in the benchmark method each iteration.
 */
@State(Scope.Thread)
public class SlidingMultiExpPrecompTest {
    @Param({"G1"})
    String groupSelection;

    Multiexponentiation multiexponentiation;

    @Setup(Level.Iteration)
    public void setup() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BilinearGroup bilGroup = new MclBilinearGroup();
        Group selectedGroup;
        switch (groupSelection) {
            case "G1":
                selectedGroup = bilGroup.getG1();
                break;
            case "G2":
                selectedGroup = bilGroup.getG2();
                break;
            case "GT":
                selectedGroup = bilGroup.getGT();
                break;
            default:
                throw new IllegalArgumentException("Invalid selected group " + groupSelection);
        }
        multiexponentiation = genMultiExp(selectedGroup, 15);
        //multiexponentiation.getTerms().forEach(t -> t.getPrecomputation().computeNegativePowers(8));
    }

    @Benchmark
    @Fork(value = 3)
    @BenchmarkMode(Mode.SingleShotTime)
    @Warmup(iterations = 10)
    @Measurement(iterations = 20)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureSliding() {
        return ExponentiationAlgorithms.interleavingSlidingWindowMultiExp(multiexponentiation, 8);
    }

    public static Multiexponentiation genMultiExp(Group group, int numTerms) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        SecureRandom secRand = new SecureRandom();
        Random rand = new Random(secRand.nextLong());
        Multiexponentiation multiexponentiation = new Multiexponentiation();
        Method getValue = LazyGroupElement.class.getDeclaredMethod("getConcreteValue");
        getValue.setAccessible(true);
        for (int i = 0; i < numTerms; ++i) {
            // ensure negativity
            long exponent = rand.nextLong();
            if (exponent > 0) {
                exponent = -exponent;
            }
            multiexponentiation.put(
                    new MultiExpTerm(
                            (GroupElementImpl) getValue.invoke(group.getUniformlyRandomNonNeutral()),
                            BigInteger.valueOf(exponent)
                    )
            );
        }
        return multiexponentiation;
    }
}
