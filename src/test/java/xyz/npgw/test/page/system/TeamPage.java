package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import xyz.npgw.test.page.common.TableTrait;
import xyz.npgw.test.page.dialog.user.AddUserDialog;

@Log4j2
public class TeamPage extends BaseSystemPage implements TableTrait {

    private final Locator companiesAndBusinessUnitsTabButton = tab("Companies and business units");
    private final Locator alertMessage = locator("div[role='alert']");
    private final Locator companyNameDropdownList = locator("[role='option']");
    private final Locator selectCompanyDropdown = locator("[aria-label='Show suggestions']:nth-child(2)");
    private final Locator lastDropdownOption = locator("[role='option']:last-child");

    public TeamPage(Page page) {
        super(page);
    }

    @Step("Click 'Companies and business units' button")
    public CompaniesAndBusinessUnitsPage clickCompaniesAndBusinessUnitsTabButton() {
        companiesAndBusinessUnitsTabButton.click();

        return new CompaniesAndBusinessUnitsPage(getPage());
    }

    @Step("Click 'Select company' dropdown")
    public TeamPage clickSelectCompanyDropdown() {
        getPage().waitForTimeout(1000);
        selectCompanyDropdown.click();

        return this;
    }

    @Step("Click '{companyName}' company in dropdown")
    public TeamPage clickCompanyInDropdown(String companyName) {
        String lastSeenText = "";

        while (true) {
            Locator options = companyNameDropdownList;
            int count = options.count();

            for (int i = 0; i < count; i++) {
                String text = options.nth(i).innerText().trim();
                if (text.equals(companyName)) {
                    options.nth(i).click();
                    return this;
                }
            }

            String currentLastText = options.nth(count - 1).innerText().trim();
            if (currentLastText.equals(lastSeenText)) {
                break;
            }

            lastSeenText = currentLastText;

            lastDropdownOption.scrollIntoViewIfNeeded();
        }

        Assert.fail("Company '" + companyName + "' not found in dropdown.");

        return this;
    }

    @Step("Click 'Add user' button")
    public AddUserDialog clickAddUserButton() {
        getPage().getByTestId("AddUserButtonTeamPage").click();

        return new AddUserDialog(getPage());
    }

    public Locator getAlertMessage() {
        alertMessage.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        return alertMessage;
    }
}
