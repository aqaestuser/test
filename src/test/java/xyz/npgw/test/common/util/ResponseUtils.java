package xyz.npgw.test.common.util;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.TimeoutError;
import lombok.extern.log4j.Log4j2;

import java.util.regex.Pattern;

@Log4j2
public class ResponseUtils {

    public static void clickAndWaitForResponse(Page page, Locator locator, String endpoint) {
        page.waitForResponse(
                response -> response.url().contains(endpoint),
                new Page.WaitForResponseOptions().setTimeout(11111),
                locator::click
        );
        page.waitForTimeout(1000);
    }

    public static void clickAndWaitForText(Page page, Locator locator, String text) {
        locator.click();
        page.getByText(text, new Page.GetByTextOptions().setExact(true))
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(6000));
    }

    public static void clickAndWaitForResponse(Page page, Locator locator, Pattern pattern, int timeout) {
        try {
            page.waitForResponse(
                    response -> pattern.matcher(response.url()).matches(),
                    new Page.WaitForResponseOptions().setTimeout(timeout),
                    locator::click
            );
        } catch (TimeoutError e) {
            log.info("custom wait {}ms for response {} failed {}", timeout, pattern.toString(), e.getMessage());
        }
    }

    public static void clickAndWaitForResponseDbg(Page page, Locator locator, Pattern pattern, int timeout) {
        try {
            page.waitForResponse(
                    response -> {
                        log.info("deb got {} waiting for {}", response.url(), pattern.toString());
                        return pattern.matcher(response.url()).matches();
                    },
                    new Page.WaitForResponseOptions().setTimeout(timeout),
                    locator::click
            );
        } catch (TimeoutError e) {
            log.info("deb custom wait {}ms for response {} failed {}", timeout, pattern.toString(), e.getMessage());
        }
    }
}
