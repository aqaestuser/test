package xyz.npgw.test.page;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.BasePage;

public class AboutBlankPage extends BasePage {

    public AboutBlankPage(Page page) {
        super(page);
    }

    @Step("Navigate to '{url}' endpoint")
    public LoginPage navigate(String url) {
        getPage().navigate(url);
        getPage().waitForLoadState(LoadState.NETWORKIDLE);

        return new LoginPage(getPage());
    }
}
