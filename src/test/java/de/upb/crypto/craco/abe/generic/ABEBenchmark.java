package de.upb.crypto.craco.abe.generic;

import de.upb.crypto.craco.interfaces.CipherText;
import de.upb.crypto.craco.interfaces.DecryptionKey;
import de.upb.crypto.craco.interfaces.EncryptionKey;
import de.upb.crypto.craco.interfaces.PlainText;
import de.upb.crypto.craco.interfaces.pe.CiphertextIndex;
import de.upb.crypto.craco.interfaces.pe.KeyIndex;

/**
 * Performs a benchmark of an ABE scheme. Collects data for later analysis.
 *
 * @author Raphael Heitjohann
 */
public class ABEBenchmark {

    private ABEBenchmarkParams params;
    private ABEBenchmarkConfig config;
    private long startTime;
    private Long[][][] setupTimes;
    private Long[][][][] encKeyGenTimes;
    private Long[][][][] decKeyGenTimes;
    private Long[][][][][] encTimes;
    private Long[][][][][] decTimes;

    public ABEBenchmark(ABEBenchmarkParams params, ABEBenchmarkConfig config) {
        this.params = params;
        this.config = config;
        this.startTime = 0;
        if (config.getPolicyNamePairs().length < 1 || config.getSetOfAttributesNamePairs().length < 1) {
            throw new IllegalArgumentException("Need at least one policy and one set of attributes for benchmarking.");
        }
        setupTimes = new Long[config.getSetOfAttributesNamePairs().length][config.getPolicyNamePairs().length]
                [config.getNumSetups()];
        encKeyGenTimes = new Long[config.getSetOfAttributesNamePairs().length][config.getPolicyNamePairs().length]
                [config.getNumSetups()][config.getNumKeyGenerations()];
        decKeyGenTimes = new Long[config.getSetOfAttributesNamePairs().length][config.getPolicyNamePairs().length]
                [config.getNumSetups()][config.getNumKeyGenerations()];
        encTimes = new Long[config.getSetOfAttributesNamePairs().length][config.getPolicyNamePairs().length]
                [config.getNumSetups()][config.getNumKeyGenerations()][config.getEncDecCycles()];
        decTimes = new Long[config.getSetOfAttributesNamePairs().length][config.getPolicyNamePairs().length]
                [config.getNumSetups()][config.getNumKeyGenerations()][config.getEncDecCycles()];
    }

    public void doBenchmark() {
        System.out.println("-------- Performing Warmup Runs --------");
        for (int i = 0; i < config.getNumWarmupRuns(); ++i) {
            System.out.println("\t-- Warmup Run " + i + " --");
            doWarmupRun();
        }
        for (int iAtt = 0; iAtt < config.getSetOfAttributesNamePairs().length; ++iAtt) {
            System.out.println("-- Selected attributes '" + config.getSetOfAttributesNamePairs()[iAtt].getName()
                    + "' --");
            for (int iPol = 0; iPol < config.getPolicyNamePairs().length; ++iPol) {
                System.out.println("\t-- Selected policy '" + config.getPolicyNamePairs()[iPol].getName() + "' --");
                for (int i = 0; i < config.getNumSetups(); ++i) {
                    System.out.println("\t\t-- Benchmark Run " + i + " --");
                    doRun(i, iAtt, iPol);
                }
            }
        }

        System.out.println("-------- Finished Benchmark --------");
        // TODO: Some nice visualization would be nice and more options for analysis (ideas?)
        System.out.println("-------- Printing Summary --------");
        printSummary();
    }

    public void printSummary() {
        for (int iAtt = 0; iAtt < config.getSetOfAttributesNamePairs().length; ++iAtt) {
            System.out.println("Summary for attributes '" + config.getSetOfAttributesNamePairs()[iAtt].getName() + "'");
            for (int iPol = 0; iPol < config.getPolicyNamePairs().length; ++iPol) {
                System.out.println("\tSummary for policy '" + config.getPolicyNamePairs()[iPol].getName() + "'");
                System.out.println("\t\tAverage execution time of setup: "
                        + compute1DAverageTime(setupTimes[iAtt][iPol]));
                System.out.println("\t\tAverage execution time of encryption key gen: "
                        + compute2DAverageTime(encKeyGenTimes[iAtt][iPol]));
                System.out.println("\t\tAverage execution time of decryption key gen: "
                        + compute2DAverageTime(decKeyGenTimes[iAtt][iPol]));
                System.out.println("\t\tAverage execution time of encryption: "
                        + compute3DAverageTimes(encTimes[iAtt][iPol]));
                System.out.println("\t\tAverage execution time of decryption: "
                        + compute3DAverageTimes(decTimes[iAtt][iPol]));
            }
        }
    }

    public void doWarmupRun() {
        // TODO: How do we do this? Do we do same cycles as regular runs or just this one cycle?
        CiphertextIndex cind = config.getCiphertextIndexAt(0, 0);
        KeyIndex kind = config.getKeyIndexAt(0, 0);
        params.doSetup(kind, cind);
        EncryptionKey encryptionKey = params.getScheme().generateEncryptionKey(
                cind
        );
        DecryptionKey decryptionKey = params.getScheme().generateDecryptionKey(
                params.getMasterSecret(),
                kind
        );
        PlainText msg = params.generatePlainText();
        CipherText ct = params.getScheme().encrypt(msg, encryptionKey);
        params.getScheme().decrypt(ct, decryptionKey);
    }

    /**
     * Perform a benchmark run including setup.
     */
    public void doRun(int numRun, int numAtt, int numPol) {
        KeyIndex kind = config.getKeyIndexAt(numAtt, numPol);
        CiphertextIndex cind = config.getCiphertextIndexAt(numAtt, numPol);
        startTimer();
        params.doSetup(kind, cind);
        setupTimes[numAtt][numPol][numRun] = stopTimerAndMeasure("\t\t\tSetup");
        for (int i = 0; i < config.getNumKeyGenerations(); ++i) {
            if (config.isPrintDetails())
                System.out.println("\t\t\t-- Performing Key Generation Number " + i + " --");
            startTimer();
            EncryptionKey encryptionKey = params.getScheme().generateEncryptionKey(
                    cind
            );
            encKeyGenTimes[numAtt][numPol][numRun][i] = stopTimerAndMeasure("\t\t\t\tEncryptionKey Generation");

            startTimer();
            DecryptionKey decryptionKey = params.getScheme().generateDecryptionKey(
                    params.getMasterSecret(),
                    kind
            );
            decKeyGenTimes[numAtt][numPol][numRun][i] = stopTimerAndMeasure("\t\t\t\tDecryptionKey Generation");

            for (int j = 0; j < config.getEncDecCycles(); ++j) {
                if (config.isPrintDetails())
                    System.out.println("\t\t\t\t-- Performing Encrypt/Decrypt Cycle " + j + " --");

                PlainText msg = params.generatePlainText();

                startTimer();
                CipherText ct = params.getScheme().encrypt(msg, encryptionKey);

                encTimes[numAtt][numPol][numRun][i][j] = stopTimerAndMeasure("\t\t\t\t\tEncryption");

                startTimer();
                PlainText msgPrime = params.getScheme().decrypt(ct, decryptionKey);
                decTimes[numAtt][numPol][numRun][i][j] = stopTimerAndMeasure("\t\t\t\t\tDecryption");

                if (config.isPrintDetails())
                    System.out.println("\t\t\t\t\tEncryption/Decryption correct: " + msg.equals(msgPrime));
            }
        }
    }

    /**
     * Sets start time at now.
     */
    public void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public long stopTimerAndMeasure(String what) {
        long time = System.currentTimeMillis() - startTime;
        if (config.isPrintDetails())
            System.out.println(what + ": Total execution time: " + time + "ms");
        return time;
    }

    public Long[][][] getSetupTimes() {
        return setupTimes;
    }

    public Long[][][][] getEncKeyGenTimes() {
        return encKeyGenTimes;
    }

    public Long[][][][] getDecKeyGenTimes() {
        return decKeyGenTimes;
    }

    public Long[][][][][] getEncTimes() {
        return encTimes;
    }

    public Long[][][][][] getDecTimes() {
        return decTimes;
    }

    private long compute1DAverageTime(Long[] times) {
        long avg = 0;
        for (Long time : times) {
            avg += time;
        }
        return avg / times.length;
    }

    private long compute2DAverageTime(Long[][] times) {
        Long[] subAvgs = new Long[times.length];
        for (int i = 0; i < times.length; ++i) {
            subAvgs[i] = compute1DAverageTime(times[i]);
        }
        return compute1DAverageTime(subAvgs);
    }

    private long compute3DAverageTimes(Long[][][] times) {
        Long[] subAvgs = new Long[times.length];
        for (int i = 0 ; i < times.length; ++i) {
            subAvgs[i] = compute2DAverageTime(times[i]);
        }
        return compute1DAverageTime(subAvgs);
    }
}
