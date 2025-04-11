package xyz.npgw.test.page.dialog;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;
import xyz.npgw.test.page.systemadministration.CompaniesAndBusinessUnitsPage;

public class AddBusinessUnitDialog extends BaseDialog {

    @Getter
    private final Locator companyNameField = locator("input[aria-label='Company name']");
    private final Locator closeButton = textExact("Close");

    public AddBusinessUnitDialog(Page page) {
        super(page);
    }

    public CompaniesAndBusinessUnitsPage clickOnCloseButton() {
        closeButton.click();

        return new CompaniesAndBusinessUnitsPage(getPage());
    }
}
