package xyz.npgw.test.page;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.BasePage;

public class AboutBlankPage extends BasePage {

    public AboutBlankPage(Page page) {
        super(page);
    }

    @Step("Navigate to '{url}' endpoint")
    public LoginPage navigate(String url) {
        getPage().navigate(url);

        return new LoginPage(getPage());
    }
}
