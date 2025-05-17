package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.system.AcquirersPage;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static xyz.npgw.test.common.util.TestUtils.createAcquirer;
import static xyz.npgw.test.common.util.TestUtils.getAcquirer;

public class EditAcquirerDialogTest extends BaseTest {

    @Test
    @TmsLink("239")
    @Epic("System/Acquirers")
    @Feature("Edit acquirers")
    @Description("Verifies that all form field placeholders are set correctly")
    public void testVerifyPlaceholdersEditForm() {

        String acquirerName = "Acquirer for edit form";
        if (!getAcquirer(getApiRequestContext(), acquirerName)) {
            createAcquirer(getApiRequestContext(), acquirerName);
        }

        List<String> expectedPlaceholders = List.of(
                "Enter acquirer name",
                "Enter acquirer code",
                "Enter challenge URL",
                "Enter fingerprint URL",
                "Enter resource URL",
                "Enter notification queue",
                "Enter acquirer config"
        );

        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .getSelectAcquirer().typeAcquirerNameToSelectAcquirerInputField(acquirerName)
                .getSelectAcquirer().clickAcquirerInDropdown(acquirerName);

        List<String> actualPlaceholders = acquirersPage
                .getTable().clickEditAcquirerButton()
                .getPlaceholdersOrTextsFromFields();

        Allure.step("Verify placeholders match expected values for all fields");
        assertEquals(actualPlaceholders, expectedPlaceholders);
    }
}
