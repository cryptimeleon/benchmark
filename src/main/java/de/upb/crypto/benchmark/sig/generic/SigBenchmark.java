package de.upb.crypto.benchmark.sig.generic;

import de.upb.crypto.benchmark.generic.BenchmarkDataAnalyzer;
import de.upb.crypto.craco.interfaces.PlainText;
import de.upb.crypto.craco.interfaces.signature.Signature;
import de.upb.crypto.craco.interfaces.signature.SignatureKeyPair;
import de.upb.crypto.craco.interfaces.signature.SigningKey;
import de.upb.crypto.craco.interfaces.signature.VerificationKey;

public class SigBenchmark {

    private SigBenchmarkParams params;
    private SigBenchmarkConfig config;
    private long startTime;
    private Long[] setupTimes;
    private Long[][] keyGenTimes;
    private Long[][][] signTimes;
    private Long[][][] vfyTimes;

    public SigBenchmark(SigBenchmarkParams params, SigBenchmarkConfig config) {
        this.params = params;
        this.config = config;
        this.startTime = 0;
        setupTimes = new Long[config.getNumSetups()];
        keyGenTimes = new Long[config.getNumSetups()][config.getNumKeyGenerations()];
        signTimes = new Long[config.getNumSetups()][config.getNumKeyGenerations()]
                [config.getSignVfyCycles()];
        vfyTimes = new Long[config.getNumSetups()][config.getNumKeyGenerations()]
                [config.getSignVfyCycles()];
    }

    public void doBenchmark() {
        for (int i = 0; i < config.getNumWarmupRuns(); ++i) {
            System.out.println("-- Warmup Run " + i + " --");
            doWarmupRun();
        }
        for (int i = 0; i < config.getNumSetups(); ++i) {
            System.out.println("-- Benchmark Run " + i + " --");
            doRun(i);
        }
        System.out.println("-------- Finished Benchmark --------");
        System.out.println("-------- Printing Summary --------");
        printSummary();
    }

    public void printSummary() {
        System.out.println("Average execution time of setup: "
                + BenchmarkDataAnalyzer.compute1DAverage(setupTimes) + "ms");
        System.out.println("Average execution time of key gen: "
                + BenchmarkDataAnalyzer.compute2DAverage(keyGenTimes) + "ms");
        System.out.println("Average execution time of signing: "
                + BenchmarkDataAnalyzer.compute3DAverage(signTimes) + "ms");
        System.out.println("Average execution time of verification: "
                + BenchmarkDataAnalyzer.compute3DAverage(vfyTimes) + "ms");
    }

    private void doWarmupRun() {
        // TODO: How do we do this? Do we do same cycles as regular runs or just this one cycle?
        params.doSetup();
        SignatureKeyPair<? extends VerificationKey, ? extends SigningKey> keyPair
            = params.generateKeyPair();
        PlainText msg = params.generatePlainText();
        Signature sig = params.getScheme().sign(msg, keyPair.getSigningKey());
        params.getScheme().verify(msg, sig, keyPair.getVerificationKey());
    }

    private void doRun(int numRun) {
        startTimer();
        params.doSetup();
        setupTimes[numRun] = stopTimerAndMeasure("Setup");
        for (int i = 0; i < config.getNumKeyGenerations(); ++i) {
            if (config.isPrintDetails())
                System.out.println("\t-- Performing Key Generation Number " + i + " --");
            startTimer();
            SignatureKeyPair<? extends VerificationKey, ? extends SigningKey> keyPair
                    = params.generateKeyPair();
            keyGenTimes[numRun][i] = stopTimerAndMeasure("\tKey Generation");

            for (int j = 0; j < config.getSignVfyCycles(); ++j) {
                if (config.isPrintDetails())
                    System.out.println("\t\t-- Performing Sign/Verify Cycle " + j + " --");
                PlainText msg = params.generatePlainText();

                startTimer();
                Signature sig = params.getScheme().sign(msg, keyPair.getSigningKey());
                signTimes[numRun][i][j] = stopTimerAndMeasure("\t\t Signing");

                startTimer();
                boolean successful = params.getScheme().verify(msg, sig, keyPair.getVerificationKey());
                vfyTimes[numRun][i][j] = stopTimerAndMeasure("\t\t Verification");

                if (config.isPrintDetails())
                    System.out.println("\t\t Verification successfull: " + successful);
            }
        }
    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
    }

    private long stopTimerAndMeasure(String what) {
        long time = System.currentTimeMillis() - startTime;
        if (config.isPrintDetails())
            System.out.println(what + ": Total execution time: " + time + "ms");
        return time;
    }
}
