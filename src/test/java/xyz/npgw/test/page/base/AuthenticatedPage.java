package xyz.npgw.test.page.base;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.Header;
import xyz.npgw.test.page.LoginPage;

public class AuthenticatedPage extends BasePage {

    private final Header header;

    public AuthenticatedPage(Page page) {
        super(page);
        this.header = new Header(page);
    }

    public DashboardPage signIn(String user, String pwd, boolean isRemember) {
        return new LoginPage(getPage())
                .fillEmailField(user)
                .fillPasswordField(pwd)
                .clickRememberMeCheckbox(isRemember)
                .clickLoginButton();
    }

    public Header getHeader() {
        return header;
    }
}
