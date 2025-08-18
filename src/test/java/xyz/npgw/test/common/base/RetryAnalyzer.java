package xyz.npgw.test.common.base;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import xyz.npgw.test.common.ProjectProperties;

public final class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRY_COUNT = 1 + ProjectProperties.getAdditionalRetries();
    private int retryCount = 1;

    @Override
    public boolean retry(ITestResult testResult) {
        return testResult.getStatus() == ITestResult.FAILURE && retryCount++ <= MAX_RETRY_COUNT;
    }
}
