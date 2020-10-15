package de.upb.crypto.benchmark.math.expalgs;

import de.upb.crypto.math.factory.BilinearGroup;
import de.upb.crypto.math.factory.BilinearGroupFactory;
import de.upb.crypto.math.interfaces.structures.group.impl.GroupElementImpl;
import de.upb.crypto.math.pairings.mcl.MclBilinearGroupProvider;
import de.upb.crypto.math.structures.groups.exp.ExponentiationAlgorithms;
import de.upb.crypto.math.structures.groups.exp.MultiExpTerm;
import de.upb.crypto.math.structures.groups.exp.Multiexponentiation;
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

    Multiexponentiation multiexponentiation;
    Multiexponentiation convertedMultiexp;

    @Setup(Level.Iteration)
    public void setup() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BilinearGroupFactory fac = new BilinearGroupFactory(128);
        fac.setRequirements(BilinearGroup.Type.TYPE_3);
        BilinearGroup bilGroup = fac.createBilinearGroup();
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
        //convertedMultiexp = ExponentiationAlgorithms.convertToNonNegativeExponents(multiexponentiation);
    }

    @Benchmark
    @Fork(value = 3)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 1, time = 5)
    @Measurement(iterations = 2, time = 5)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureSlidingA1() {
        return ExponentiationAlgorithms.interleavingSlidingWindowMultiExpA1(multiexponentiation, 4);
    }

    @Benchmark
    @Fork(value = 3)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 1, time = 5)
    @Measurement(iterations = 2, time = 5)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureSlidingA2() {
        return ExponentiationAlgorithms.interleavingSlidingWindowMultiExpA2(multiexponentiation, 4);
    }

    @Benchmark
    @Fork(value = 3)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 1, time = 5)
    @Measurement(iterations = 2, time = 5)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureWnaf() {
        return ExponentiationAlgorithms.interleavingWnafMultiExp(multiexponentiation, 4);
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