package xyz.npgw.test.common.base;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.testng.ITestContext;
import org.testng.ITestListener;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

@Log4j2
public final class TestListener implements ITestListener {

    @SneakyThrows
    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);

        log.info("START SUITE");
        log.info("");

        log.info("create test accounts super/admin/user one test company and one business unit per each thread");
        log.info("create answer challenge for each user");
        log.info("store state for each role and expiration time");

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
