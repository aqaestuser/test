package xyz.npgw.test.run;

import io.qameta.allure.*;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.systemadministration.CompaniesAndBusinessUnitsPage;
import xyz.npgw.test.testdata.CreatorCompanyWithRandomName;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AddBusinessUnitTest extends BaseTest {

    @Test
    @TmsLink("213")
    @Epic("Companies and business units")
    @Feature("Adding button state verification")
    @Description("Verify 'Add business unit' button activation once some company is selected")
    public void testVerifyAvailabilityOfBusinessUnitButton() {
        CreatorCompanyWithRandomName company = CreatorCompanyWithRandomName.random();

        CompaniesAndBusinessUnitsPage page = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
                .clickAddCompanyButton()
                .fillCompanyNameField(company.getName())
                .fillCompanyTypeField(company.getType())
                .clickCreateButton()
                .selectCompany(company.getName());

        Allure.step("'Add business unit' button is enabled now");
        assertThat(page.getAddBusinessUnitButton()).isEnabled();
    }

    @Test
    @TmsLink("214")
    @Epic("Companies and business units")
    @Feature("Adding button state verification")
    @Description("Verify 'Add business unit' button is disabled if 'Select company' filter's field is cleaned")
    public void testVerifyAddBusinessUnitButtonDefaultState() {
        CompaniesAndBusinessUnitsPage page = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton();

        Allure.step("'Add business unit' button is disabled once no destination company is selected");
        assertThat(page.getAddBusinessUnitButton()).isDisabled();
    }
}
