package de.scoopwoftware.jmeter.techtalk;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;

import java.util.Random;

public class TechTalkJavaSampleClient extends AbstractJavaSamplerClient {

    private final Logger logger = getNewLogger();

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        int bound = context.getIntParameter("bound");
        int threshold = context.getIntParameter("threshold");

        SampleResult sampleResult = new SampleResult();
        sampleResult.sampleStart();

        logger.info("bound: {}", bound);
        logger.info("threshold: {}", threshold);

        int sleep = new Random().nextInt(bound);
        logger.info("sleep: {}", sleep);
        try {
            Thread.sleep(sleep);
            sampleResult.sampleEnd();
            if (sleep > threshold) {
                sampleResult.setSuccessful(false);
                sampleResult.setResponseMessage("over threshold (" + sleep + ")");
            } else {
                sampleResult.setSuccessful(true);
                sampleResult.setResponseMessage("OK(" + sleep + ")");
            }
        } catch (InterruptedException e) {
            sampleResult.sampleEnd();
            sampleResult.setSuccessful(false);
            sampleResult.setResponseMessage("InterruptedException");
        }

        return sampleResult;
    }

    @Override
    public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();
        arguments.addArgument("bound", "1000");
        arguments.addArgument("threshold", "500");
        return arguments;
    }


    @Override
    public void setupTest(JavaSamplerContext context) {
        logger.info("Setup test {}", Thread.currentThread().getName());
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {
        logger.info("Tear down {}", Thread.currentThread().getName());
    }

}
