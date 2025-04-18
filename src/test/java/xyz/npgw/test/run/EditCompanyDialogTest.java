package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.opentest4j.AssertionFailedError;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EditCompanyDialogTest extends BaseTest {

    @Test()
    @TmsLink("266")
    @Epic("System/Companies and business units")
    @Feature("Edit company")
    @Description("Edit company info and save")
    public void testEditCompanyInfoAndSave() {
        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
                .clickSelectCompanyDropdown()
                .clickCompanyInDropdown("Kate")
                .clickEditCompanyButton()
                .fillCompanyNameField("Kate1")
                .fillCompanyTypeField("LLC")
                .fillCompanyDescriptionField("Description of company business model")
                .fillCompanyWebsiteField("google.com")
                .fillCompanyPrimaryContactField("John Doe")
                .fillCompanyEmailField("google@gmail.com")
                .fillCompanyCountryField("France")
                .fillCompanyStateField("Provence")
                .fillCompanyZipField("75001")
                .fillCompanyCityField("Paris")
                .fillCompanyPhoneField("+33 1 22-83-56-11")
                .fillCompanyMobileField("+33 7 22-83-56-11")
                .fillCompanyFaxField("84952235611")
                .clickSaveChangesButton();

        Allure.step("Verify: success message is displayed");
        assertThat(companiesAndBusinessUnitsPage.getAlertMessage()).hasText(
                "SUCCESSCompany was updated successfully");
    }
}
