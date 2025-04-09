package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.page.AddBusinessUnitDialog;
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

        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
                .clickAddCompanyButton()
                .fillCompanyNameField(company.getName())
                .fillCompanyTypeField(company.getType())
                .clickCreateButton()
                .waitUntilAlertIsGone()
                .selectCompanyInTheFilter(company.getName());

        Allure.step("'Add business unit' button is enabled and 'Edit company' button is presented");
        assertThat(companiesAndBusinessUnitsPage.getAddBusinessUnitButton()).isEnabled();
        assertThat(companiesAndBusinessUnitsPage.getEditCompanyButton()).isVisible();
    }

    @Test
    @TmsLink("214")
    @Epic("Companies and business units")
    @Feature("Adding button state verification")
    @Description("Verify 'Add business unit' button is disabled if 'Select company' filter's field is cleaned")
    public void testVerifyAddBusinessUnitButtonDefaultState() {
        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton();

        Allure.step("'Add business unit' button is disabled once no destination company is selected");
        assertThat(companiesAndBusinessUnitsPage.getAddBusinessUnitButton()).isDisabled();
    }

    @Test
    @TmsLink("238")
    @Epic("Companies and business units")
    @Feature("'Company name' field verification")
    @Description("Verify that 'Company name' field is prefilled and impossible to change")
    public void testCompanyNameFieldDefaultState() {
        CreatorCompanyWithRandomName company = CreatorCompanyWithRandomName.random();

        AddBusinessUnitDialog addBusinessUnitDialog = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
                .clickAddCompanyButton()
                .fillCompanyNameField(company.getName())
                .fillCompanyTypeField(company.getType())
                .clickCreateButton()
                .waitUntilAlertIsGone()
                .selectCompanyInTheFilter(company.getName())
                .clickOnAddBusinessUnitButton();

        Allure.step("Verify that Company name field is read-only and prefilled created company");
        assertThat(addBusinessUnitDialog.getCompanyNameField()).hasValue(company.getName());
        assertThat(addBusinessUnitDialog.getCompanyNameField()).hasAttribute("aria-readonly", "true");
    }

    @Test
    @TmsLink("241")
    @Epic("Companies and business units")
    @Feature("'Close' button verification")
    @Description("Verify that a new Merchant wasn't added once click 'Close' button")
    public void testCloseButtonAndDiscardChanges() {
        CreatorCompanyWithRandomName company = CreatorCompanyWithRandomName.random();

        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
                .clickAddCompanyButton()
                .fillCompanyNameField(company.getName())
                .fillCompanyTypeField(company.getType())
                .clickCreateButton()
                .waitUntilAlertIsGone()
                .selectCompanyInTheFilter(company.getName())
                .clickOnAddBusinessUnitButton()
                .clickOnCloseButton();

        Allure.step("The table is empty and 'No rows to display.' is displayed");
        assertThat(companiesAndBusinessUnitsPage.getBusinessUnitEmptyList()).hasText("No rows to display.");
    }
}
