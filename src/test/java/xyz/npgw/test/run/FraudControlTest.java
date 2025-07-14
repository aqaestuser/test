package xyz.npgw.test.run;

import com.microsoft.playwright.Locator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.entity.FraudControl;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.system.FraudControlPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FraudControlTest extends BaseTest {

    private static final FraudControl FRAUD_CONTROL = FraudControl.builder()
            .controlName("ControlEverything")
            .controlCode("8848")
            .controlDisplayName("ControlDisplay")
            .controlConfig("default")
            .build();

    @Test
    @TmsLink("891")
    @Epic("System/Fraud Control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Add Active Fraud Control")
    public void testAddActiveFraudControl() {
        FraudControlPage page = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .clickAddFraudControl()
                .fillFraudControlNameField(FRAUD_CONTROL.getControlName())
                .fillFraudControlCodeField(FRAUD_CONTROL.getControlCode())
                .fillFraudControlConfigField(FRAUD_CONTROL.getControlConfig())
                .fillFraudControlDisplayNameField(FRAUD_CONTROL.getControlDisplayName())
                .checkActiveRadiobutton()
                .clickCreateButton();

        Locator row = page.getTable().getRow(FRAUD_CONTROL.getControlName()).first();

        Allure.step("Verify that all the data are presented in the row");
        assertThat(row).containsText(FRAUD_CONTROL.getControlCode());
        assertThat(row).containsText(FRAUD_CONTROL.getControlName());
        assertThat(row).containsText(FRAUD_CONTROL.getControlConfig());
        assertThat(row).containsText(FRAUD_CONTROL.getControlDisplayName());
        assertThat(row).containsText("Active");
    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteFraudControl(getApiRequestContext(), FRAUD_CONTROL.getControlName());
        super.afterClass();
    }
}
