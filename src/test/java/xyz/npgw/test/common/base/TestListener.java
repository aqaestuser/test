package xyz.npgw.test.common.base;

import lombok.extern.log4j.Log4j2;
import org.testng.ITestContext;
import org.testng.ITestListener;

import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j2
public final class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);

        log.info("START SUITE");
        log.info("");
        String testRunId = new SimpleDateFormat("_MMdd_HHmmss").format(new Date());
        context.setAttribute("testRunId", testRunId);
    }

    @Override
    public void onFinish(ITestContext context) {
        ITestListener.super.onFinish(context);

        log.info("");
        log.info("testRunId - {}", context.getAttribute("testRunId"));
        log.info("FINISH SUITE");
    }
}
