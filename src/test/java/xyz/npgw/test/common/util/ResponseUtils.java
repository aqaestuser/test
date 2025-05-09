package xyz.npgw.test.common.util;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

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
        page.getByText(text, new Page.GetByTextOptions().setExact(true)).waitFor();
    }
}
