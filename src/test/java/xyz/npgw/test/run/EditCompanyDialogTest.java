package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EditCompanyDialogTest extends BaseTest {

    private static final String COMPANY_NAME = "%s Kate".formatted(RUN_ID);

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createCompany(getApiRequestContext(), COMPANY_NAME);
    }

    @Test
    @TmsLink("266")
    @Epic("System/Companies and business units")
    @Feature("Edit company")
    @Description("Edit company info and save")
    public void testEditCompanyInfoAndSave() {
        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
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
        assertThat(companiesAndBusinessUnitsPage.getAlert().getMessage())
                .hasText("SUCCESSCompany was updated successfully");
    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);
        super.afterClass();
    }
}
