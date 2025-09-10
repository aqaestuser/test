package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.base.BaseTestForSingleLogin;
import xyz.npgw.test.page.dashboard.AdminDashboardPage;
import xyz.npgw.test.page.dialog.merchant.GenerateTokenConfirmDialog;
import xyz.npgw.test.page.dialog.merchant.SecretTokenDialog;
import xyz.npgw.test.page.system.AdminBusinessUnitsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TestAdminCompaniesAndBusinessUnitsTest extends BaseTestForSingleLogin {

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        super.openSiteAccordingRole();
    }

    @Test
    @TmsLink("???")
    @Epic("System/Business units")
    @Feature("Generate token")
    @Description("Generate API secret token as company admin")
    public void testGenerateTokenAsTestAdmin() {
        GenerateTokenConfirmDialog generateTokenConfirmDialog = new AdminDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickBusinessUnitsTab()
                .getTable().clickCopyBusinessUnitIdToClipboardButton(Constants.BUSINESS_UNIT_FOR_TEST_RUN)
                .getTable().clickGenerateSecretTokenButton(Constants.BUSINESS_UNIT_FOR_TEST_RUN);

        assertThat(generateTokenConfirmDialog.getDialogHeader())
                .hasText("MerchantCompanyForTestRunOnly Inc. secret token");
        assertThat(generateTokenConfirmDialog.getContent())
                .hasText("Doing this will deactivate any current token in use");

        SecretTokenDialog secretTokenDialog = generateTokenConfirmDialog
                .clickGenerateButton()
                .clickCopySecretToken();

        assertThat(secretTokenDialog.getDialogHeader()).hasText("MerchantCompanyForTestRunOnly Inc. secret token");
    }

    @Test
    @TmsLink("691")
    @Epic("System/Business units")
    @Feature("Settings")
    @Description("The company info block can be hidden and shown via settings.")
    public void testToggleCompanyInfoVisibilityViaSettingsAsAdmin() {
        AdminBusinessUnitsPage adminBusinessUnitsPage = new AdminDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickBusinessUnitsTab()
                .clickSettings()
                .checkHideCompanyInfo();

        Allure.step("Verify: company info block is hidden after selecting 'Hide' in settings");
        assertThat(adminBusinessUnitsPage.getCompanyInfoBlock()).isHidden();

        adminBusinessUnitsPage
                .checkShowCompanyInfo();

        Allure.step("Verify: company info block is visible again after selecting 'Show' in settings");
        assertThat(adminBusinessUnitsPage.getCompanyInfoBlock()).isVisible();
    }
}
