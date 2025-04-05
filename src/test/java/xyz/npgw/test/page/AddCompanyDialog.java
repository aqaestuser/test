package xyz.npgw.test.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import xyz.npgw.test.page.base.BasePage;

import java.util.List;

public class AddCompanyDialog extends BasePage {

    private final Locator addCompanyDialogHeader = locator("section header");
    private final Locator allFieldPlaceholders = locator("[data-slot='input']:not([placeholder='Search...'])");

    public AddCompanyDialog(Page page) {
        super(page);
    }

    public Locator getAddCompanyDialogHeader() {
        return addCompanyDialogHeader;
    }

    public List<String> getAllFieldPlaceholders() {
        allFieldPlaceholders.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        return allFieldPlaceholders.all().stream().map(l -> l.getAttribute("placeholder")).toList();
    }
}
