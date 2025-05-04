package xyz.npgw.test.run;

import com.microsoft.playwright.Locator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.DashboardPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EditBusinessUnitDialogTest extends BaseTest {

    @Test
    @TmsLink("387")
    @Epic("System/Companies and business units")
    @Feature("Edit business unit")
    @Description("Verify that the title of the 'Edit Business Unit' dialog matches the expected result")
    public void testVerifyTitleEditBusinessUnitDialog() {
        String buName = "NewBUForEdit";
        String companyName = "CompanyForBU";

        TestUtils.deleteCompany(getApiRequestContext(), companyName);
        TestUtils.createCompany(getApiRequestContext(), companyName);
        TestUtils.createBusinessUnit(getApiRequestContext(), companyName, buName);

        Locator dialogTitle = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(companyName)
                .clickEditBusinessUnitButton()
                .getDialogHeader();

        Allure.step("Verify: the header contains the expected title text");
        assertThat(dialogTitle).hasText("Edit business unit");
    }
}
