package xyz.npgw.test.run;

import com.microsoft.playwright.Locator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import xyz.npgw.test.common.UserRole;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.AboutBlankPage;
import xyz.npgw.test.page.dialog.merchant.EditBusinessUnitDialog;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;

public class EditBusinessUnitDialogTest extends BaseTest {
    private final String companyName = "CompanyForBuEdit";
    private final String buName = "NewBUForEdit";
    private final List<String> expectedFieldsLabel = List.of(
            "Company name",
            "Business unit name"
    );

    @Test
    @TmsLink("387")
    @Epic("System/Companies and business units")
    @Feature("Edit business unit")
    @Description("Verify that the title of the 'Edit Business Unit' dialog matches the expected result")
    public void testVerifyTitleEditBusinessUnitDialog(@Optional("UNAUTHORISED") String userRole) {
        TestUtils.createCompanyIfNeeded(getApiRequestContext(), companyName);
        TestUtils.createMerchantIfNeeded(getApiRequestContext(), companyName, buName);

        Locator dialogTitle = new AboutBlankPage(getPage())
                .navigate("/login")
                .loginAs(UserRole.SUPER)
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(companyName)
                .clickEditBusinessUnitButton()
                .getDialogHeader();

        Allure.step("Verify: the header contains the expected title text");
        assertThat(dialogTitle).hasText("Edit business unit");
    }

    @Test
    @TmsLink("501")
    @Epic("System/Companies and business units")
    @Feature("Edit business unit")
    @Description("Verify that the label of each field is correct")
    public void testVerifyLabelOfEachField(@Optional("UNAUTHORISED") String userRole) {
        TestUtils.createCompanyIfNeeded(getApiRequestContext(), companyName);
        TestUtils.createMerchantIfNeeded(getApiRequestContext(), companyName, buName);

        EditBusinessUnitDialog editBusinessUnitDialog = new AboutBlankPage(getPage())
                .navigate("/login")
                .loginAs(UserRole.SUPER)
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(companyName)
                .clickEditBusinessUnitButton();

        List<String> actualLabelList = editBusinessUnitDialog.getAllFieldsLabel();

        Allure.step("Verify: all labels are correct for each field");
        assertThat(editBusinessUnitDialog.getFieldLabel()).hasText(new String[]{"Company name", "Business unit name"});
    }
}
