package xyz.npgw.test.common.base;

import lombok.extern.log4j.Log4j2;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import xyz.npgw.test.common.ProjectProperties;

@Log4j2
public final class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRY_COUNT = 1 + ProjectProperties.getAdditionalRetries();
    private int retryCount = 1;

    @Override
    public boolean retry(ITestResult testResult) {
        if (testResult.getStatus() == ITestResult.FAILURE && retryCount++ <= MAX_RETRY_COUNT) {
            log.info("RETRY >>> {}#{}", testResult.getTestClass().getName(), testResult.getName());
        }
        return testResult.getStatus() == ITestResult.FAILURE && retryCount++ <= MAX_RETRY_COUNT;
    }
}
