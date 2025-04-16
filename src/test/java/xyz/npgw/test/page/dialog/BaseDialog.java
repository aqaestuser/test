package xyz.npgw.test.page.dialog;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.base.BaseModel;

import java.util.List;

@Getter
public abstract class BaseDialog extends BaseModel {

    private final Locator dialogHeader = locator("section header");
    private final Locator banner = dialog().getByRole(AriaRole.BANNER);
    private final Locator closeButton = dialog().getByText("Close");
    private final Locator closeIcon = dialog().getByLabel("Close");
    private final Locator requiredFields = dialog().locator("[required]");
    private final Locator allInputFields = dialog().getByRole(AriaRole.TEXTBOX);
    private final Locator fieldsWithPlaceholder = dialog()
            .locator("input[placeholder], textarea[placeholder], span[data-slot='value']");

    public BaseDialog(Page page) {
        super(page);
    }

    public List<String> getPlaceholdersOrTextsFromFields() {
        fieldsWithPlaceholder.last().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        return fieldsWithPlaceholder.all().stream().map(locator -> {
            String placeholder = locator.getAttribute("placeholder");
            return placeholder != null ? placeholder : locator.innerText();
        }).toList();
    }

    public List<String> getPlaceholdersFromInputFields() {
        allInputFields.last().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        return allInputFields.all().stream().map(locator -> locator.getAttribute("placeholder")).toList();
    }

    @Step("Clear all form input fields")
    public BaseDialog clearInputFields() {
        allInputFields.last().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        allInputFields.all().forEach(locator -> {
            locator.clear();
            banner.click();
        });
        return this;
    }
}
