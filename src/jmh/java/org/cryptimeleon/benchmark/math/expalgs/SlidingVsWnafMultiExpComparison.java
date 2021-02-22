package org.cryptimeleon.benchmark.math.expalgs;

import de.upb.crypto.math.interfaces.structures.group.impl.GroupElementImpl;
import de.upb.crypto.math.pairings.generic.BilinearGroup;
import de.upb.crypto.math.pairings.type1.supersingular.SupersingularBilinearGroup;
import de.upb.crypto.math.structures.groups.exp.ExponentiationAlgorithms;
import de.upb.crypto.math.structures.groups.exp.MultiExpTerm;
import de.upb.crypto.math.structures.groups.exp.Multiexponentiation;
import de.upb.crypto.math.structures.groups.exp.SmallExponentPrecomputation;
import de.upb.crypto.math.structures.groups.lazy.LazyGroupElement;
import de.upb.crypto.math.interfaces.structures.Group;
import org.openjdk.jmh.annotations.*;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class SlidingVsWnafMultiExpComparison {

    @Param({"G1"})
    String groupSelection;

    Multiexponentiation wnafMultiexponentiation;
    Multiexponentiation slidingMultiexponentiation;


    @Setup(Level.Iteration)
    public void setup() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BilinearGroup bilGroup = new SupersingularBilinearGroup(128);
        //BilinearGroup bilGroup = new MclBilinearGroupProvider().provideBilinearGroup();
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
        wnafMultiexponentiation = genMultiExp(selectedGroup, 15);
        slidingMultiexponentiation = new Multiexponentiation();
        wnafMultiexponentiation.getTerms().forEach(
                t -> slidingMultiexponentiation.put(t.getBase(), t.getExponent(),
                        new SmallExponentPrecomputation(t.getBase()))
        );
        wnafMultiexponentiation.getTerms().forEach(
                t -> t.getPrecomputation().compute(8, false)
        );
        slidingMultiexponentiation.getTerms().forEach(
                t -> t.getPrecomputation().compute(8, false)
        );
        slidingMultiexponentiation.getTerms().forEach(
                t -> t.getPrecomputation().computeNegativePowers(8, true)
        );
    }

    @Benchmark
    @Fork(value = 4)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 10, time = 1)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureSliding() {
        return ExponentiationAlgorithms.interleavingSlidingWindowMultiExp(slidingMultiexponentiation,
                8);
    }

    @Benchmark
    @Fork(value = 4)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 10, time = 1)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureWnaf() {
        return ExponentiationAlgorithms.interleavingWnafMultiExp(wnafMultiexponentiation, 8);
    }

    public static Multiexponentiation genMultiExp(Group group, int numTerms) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        SecureRandom secRand = new SecureRandom();
        Random rand = new Random(secRand.nextLong());
        Multiexponentiation multiexponentiation = new Multiexponentiation();
        Method getValue = LazyGroupElement.class.getDeclaredMethod("getConcreteValue");
        getValue.setAccessible(true);
        for (int i = 0; i < numTerms; ++i) {
            long exponent = rand.nextInt();
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