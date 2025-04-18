package xyz.npgw.test.common.util;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ResponseUtils {

    public static void clickAndWaitForResponse(Page page, Locator locator, String endpoint) {
        page.waitForResponse(response
                        -> response.request().postData() != null
                        && response.request().url().contains(endpoint),
                locator::click);
    }
}
