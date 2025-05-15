package xyz.npgw.test.common.base;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.testng.ITestContext;
import org.testng.ITestListener;

import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j2
public final class TestListener implements ITestListener {

    @SneakyThrows
    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);

        log.info("START SUITE");
        log.info("");

        log.debug("create test accounts super/admin/user one test company and one business unit per each thread");
        log.debug("create answer challenge for each user");
        log.debug("store state for each role and expiration time");

        context.setAttribute("testRunId", new SimpleDateFormat("_MMdd_HHmmss").format(new Date()));
    }

    @Override
    public void onFinish(ITestContext context) {
        ITestListener.super.onFinish(context);

        log.info("");
        log.debug("testRunId - {}", context.getAttribute("testRunId"));
        log.info("FINISH SUITE");
    }
}
