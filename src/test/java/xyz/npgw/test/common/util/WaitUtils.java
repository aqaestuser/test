package xyz.npgw.test.common.util;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.TimeoutError;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.client.Transaction;
import xyz.npgw.test.common.client.TransactionResponse;
import xyz.npgw.test.common.entity.Status;

import java.util.concurrent.TimeUnit;

@Log4j2
public class WaitUtils {

    @SneakyThrows
    public static TransactionResponse waitUntil(APIRequestContext request, TransactionResponse transactionResponse, Status status) {
        String callerMethodName = getCallerMethodName();

        double timeout = ProjectProperties.getDefaultTimeout() * 3;
        do {
            transactionResponse = Transaction.getTransactionById(request, transactionResponse);
            TimeUnit.MILLISECONDS.sleep(300);
            timeout -= 100;
            if (timeout <= 0) {
                throw new TimeoutError("Waiting for '%s'".formatted(callerMethodName));
            }
        } while (transactionResponse.status() != null && !transactionResponse.status().equals(status));
        log.info("Wait for {} took {}ms", callerMethodName, ProjectProperties.getDefaultTimeout() * 3 - timeout);

        return transactionResponse;
    }

    private static String getCallerMethodName() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length > 1) {
            StackTraceElement callerElement = stackTraceElements[3];
            return callerElement.getMethodName();
        }
        return "Empty caller method name";
    }
}
