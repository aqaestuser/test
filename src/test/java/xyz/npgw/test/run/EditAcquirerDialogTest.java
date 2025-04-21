package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.page.DashboardPage;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class EditAcquirerDialogTest extends BaseTest {

    @Ignore("Bug")
    @Test
    @TmsLink("239")
    @Epic("System/Acquirers")
    @Feature("Edit acquirers")
    @Description("Verifies that all form field placeholders are set correctly")
    public void testVerifyPlaceholdersEditForm() {
        List<String> expectedPlaceholders = List.of(
                "Enter acquirer name", "Enter acquirer code", "Enter challenge URL", "Enter fingerprint URL",
                "Enter resource URL", "Enter notification queue", "Enter priority", "Enter acquirer config",
                "Search...", "Select timezone");

        List<String> actualPlaceholders = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .getSystemMenu()
                .clickAcquirersTab()
                .clickEditButtonForAcquirer("acquirer1")
                .clearInputFields()
                .getPlaceholdersOrTextsFromFields();

        Allure.step("Verify placeholders match expected values for all fields");
        assertEquals(actualPlaceholders, expectedPlaceholders);
    }
}
