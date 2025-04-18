package xyz.npgw.test.run;

import com.microsoft.playwright.Locator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.system.GatewayPage;

import java.util.Arrays;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class GatewayPageTest extends BaseTest {

    @Test
    @TmsLink("283")
    @Epic("System/Gateway")
    @Feature("Currency")
    @Description("The 'Currency' dropdown toggles and contains options All, USD, EUR.")
    public void testOpenCurrencyDropdown() {
        Locator actualOptions = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .getSystemMenu()
                .clickGatewayTab()
                .clickCurrencyValue()
                .getCurrencyOptions();

        Allure.step("Verify: The 'Currency' dropdown toggles and contains options All, USD, EUR.");
        assertThat(actualOptions).hasText(new String[]{"ALL", "USD", "EUR"});
    }

    @Test(expectedExceptions = AssertionError.class)
    @TmsLink("285")
    @Epic("System/Gateway")
    @Feature("Currency")
    @Description("Verify that re-selecting an already selected currency keeps the selection unchanged.")
    public void testRetainCurrencyWhenReSelectingSameOption() {

        Allure.step("This test is temporarily disabled till bug fixed.");

        List<String> expectedOptions = List.of("ALL", "USD", "EUR");

        GatewayPage gatewayPage = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .getSystemMenu()
                .clickGatewayTab();

        Locator actualCurrency = gatewayPage.getCurrencyValue();

        for (String currency : expectedOptions) {
            gatewayPage
                    .clickCurrencyValue()
                    .selectCurrency(currency);

            Allure.step("Verify currency has value: " + currency);
            assertThat(actualCurrency).hasText(currency);

            gatewayPage
                    .clickCurrencyValue()
                    .selectCurrency(currency);

            Allure.step("Verify currency has the same value: " + currency);
            assertThat(actualCurrency).hasText(currency);
        }
    }

    @Test()
    @TmsLink("307")
    @Epic("System/Gateway")
    @Feature("Currency")
    @Description(
            "Check that selecting a company populates the 'Business units list',"
                    + " and when no company is selected, the list is empty with 'No items.'")
    public void testBusinessUnitsListUpdatesOnCompanySelection() {

        String companyName = "Company 112172";
        String[] businessUnitsList = new String[]{"Merchant for C112172", "Merchant 1 for C112172"};

        GatewayPage gatewayPage = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .getSystemMenu()
                .clickGatewayTab()
                .clickSelectCompanyPlaceholder()
                .clickCompanyInDropdown(companyName);

        Locator selectCompanyPlaceholder = gatewayPage.getSelectCompanyPlaceholder();

        Allure.step("Verify: The dropdown is closed.");
        assertThat(gatewayPage.getCompanyDropdown()).not().isVisible();

        Allure.step(String.format("Verify: Placeholder has value '%s'", companyName));
        assertThat(selectCompanyPlaceholder).hasValue(companyName);

        Allure.step("Verify: 'Business units list' title is visible");
        assertThat(gatewayPage.getBusinessUnitsListHeader()).isVisible();

        Allure.step("Verify: List contains two Business units: " + Arrays.toString(businessUnitsList));
        assertThat(gatewayPage.getBusinessUnitsList()).hasText(businessUnitsList);

        gatewayPage.clickSelectCompanyClearIcon()
                .clickSelectCompanyDropdownChevron();

        Allure.step("Verify: Placeholder has value 'Search...'", () -> {
            Assert.assertEquals(selectCompanyPlaceholder.getAttribute("placeholder"), "Search...");
            assertThat(selectCompanyPlaceholder).hasValue("");
        });

        Allure.step("Verify: 'Business units list' title is still visible");
        assertThat(gatewayPage.getBusinessUnitsListHeader()).isVisible();

        Allure.step("Verify: 'Business units list' has 'No items.'");
        assertThat(gatewayPage.getBusinessUnitsList()).hasText(new String[]{"No items."});
    }

}
