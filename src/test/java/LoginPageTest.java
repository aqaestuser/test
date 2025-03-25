import org.testng.annotations.Test;
import page.HomePage;
import page.LoginPage;
import runner.BaseTest;
import testdata.Constants;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPageTest extends BaseTest {
    private static final int bogusCONSTANT = 1;

    @Test
    public void testNavigateToLoginPage() {

        LoginPage loginPage = new LoginPage(getPage());
// aa
        assertThat(loginPage.getPage()).hasURL(Constants.LOGIN_PAGE_URL);
        //assertThat(loginPage.getPage()).hasTitle(Constants.BASE_URL_TITLE);
    }

    @Test
    public void testLogin() {

        HomePage homePage = new LoginPage(getPage())
                .fillEmailField(Constants.USER_EMAIL)
                .fillPasswordField(Constants.USER_PASSWORD)
                .clickLoginButton();

        assertThat(homePage.getPage()).hasURL(Constants.DASHBOARD_PAGE_URL);
        assertThat(homePage.getPage()).hasTitle(Constants.DASHBOARD_URL_TITLE);
    }
}
