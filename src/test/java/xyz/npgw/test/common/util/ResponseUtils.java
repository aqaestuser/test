package xyz.npgw.test.common.util;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ResponseUtils {

    public static void clickAndWaitForText(Page page, Locator locator, String text) {
        locator.click();
        page.getByText(text, new Page.GetByTextOptions().setExact(true)).waitFor();
    }
}
