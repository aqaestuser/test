package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EditCompanyDialogTest extends BaseTest {

    @Test
    @TmsLink("266")
    @Epic("System/Companies and business units")
    @Feature("Edit company")
    @Description("Edit company info and save")
    public void testEditCompanyInfoAndSave() {
        TestUtils.deleteCompany(getApiRequestContext(), "Kate");
        TestUtils.createCompanyIfNeeded(getApiRequestContext(), "Kate");

        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .refreshDashboard()
                .clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany("Kate")
                .clickEditCompanyButton()
                .fillCompanyTypeField("LLC")
                .fillCompanyDescriptionField("Description of company business model")
                .fillCompanyWebsiteField("https://google.com")
                .fillCompanyPrimaryContactField("John Doe")
                .fillCompanyEmailField("google@gmail.com")
                .fillCompanyCountryField("FR")
                .fillCompanyStateField("Provence")
                .fillCompanyZipField("75001")
                .fillCompanyCityField("Paris")
                .fillCompanyPhoneField("+1234567890123")
                .fillCompanyMobileField("+1234567890123")
                .fillCompanyFaxField("+1234567890123")
                .clickSaveChangesButton();

        Allure.step("Verify: success message is displayed");
        assertThat(companiesAndBusinessUnitsPage.getAlert().getMessage()).hasText(
                "SUCCESSCompany was updated successfully");
    }
}
