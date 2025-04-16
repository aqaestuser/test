package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.TestUtils;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.common.util.User;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.system.TeamPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TeamPageTest extends BaseTest {

    @Test
    @TmsLink("154")
    @Epic("System/Team")
    @Feature("Navigation")
    @Description("User navigate to 'System administration page' after clicking "
            + "on 'System administration' link on the header")
    public void testNavigateToSystemAdministrationPage() {
        TeamPage systemAdministrationPage = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink();

        Allure.step("Verify: System administration Page URL");
        assertThat(systemAdministrationPage.getPage()).hasURL(Constants.SYSTEM_PAGE_URL);

        Allure.step("Verify: System administration Page Title");
        assertThat(systemAdministrationPage.getPage()).hasTitle(Constants.SYSTEM_URL_TITLE);
    }

    @Test(dataProvider = "getUsers", dataProviderClass = TestDataProvider.class)
    @TmsLink("298")
    @Epic("System/Team")
    @Feature("Add user")
    @Description("Add users with roles [SUPER, ADMIN, USER] as super admin")
    public void testAddUser(User user) {
        TestUtils.createBusinessUnitIfNeeded(getApiRequestContext(), user);
        TestUtils.deleteUser(getApiRequestContext(), user);

        TeamPage teamPage = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickSelectCompanyDropdown()
                .clickCompanyInDropdown(user.companyName())
                .clickAddUserButton()
                .fillEmailField(user.email())
                .fillPasswordField(user.password())
                .setStatusActiveRadiobutton(user.enabled())
                .setUserRoleRadiobutton(user.userRole())
                .setAllowedBusinessUnits(user.merchantIds())
                .clickCreateButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlertMessage()).hasText("SUCCESSUser was created successfully");
    }
}
