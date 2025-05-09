package xyz.npgw.test.page.dialog;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.base.BaseModel;
import xyz.npgw.test.page.system.BaseSystemPage;

import java.util.List;

@Getter
@SuppressWarnings("unchecked")
public abstract class BaseDialog<ReturnPageT extends BaseSystemPage, CurrentDialogT extends BaseDialog>
        extends BaseModel {

    private final Locator dialogHeader = locator("section header");
    private final Locator banner = dialog().getByRole(AriaRole.BANNER);
    private final Locator closeButton = dialog().getByText("Close");
    private final Locator closeIcon = dialog().getByLabel("Close");
    private final Locator requiredFields = dialog().locator("[required]");
    private final Locator allInputFields = dialog().getByRole(AriaRole.TEXTBOX);
    private final Locator fieldsWithPlaceholder = dialog()
            .locator("input[placeholder], textarea[placeholder], span[data-slot='value']");
    private final Locator allPlaceholdersWithoutSearch = locator("[data-slot='input']:not([placeholder='Search...'])");
    private final Locator alertMessage = locator("[role='alert']");

    public BaseDialog(Page page) {
        super(page);
    }

    public List<String> getPlaceholdersOrTextsFromFields() {
        fieldsWithPlaceholder.last().waitFor();

        return fieldsWithPlaceholder.all().stream().map(locator -> {
            String placeholder = locator.getAttribute("placeholder");
            return placeholder != null ? placeholder : locator.innerText();
        }).toList();
    }

    public List<String> getAllFieldPlaceholders() {
        allPlaceholdersWithoutSearch.first().waitFor();

        return allPlaceholdersWithoutSearch.all().stream().map(l -> l.getAttribute("placeholder")).toList();
    }

    @Step("Clear all form input fields")
    public CurrentDialogT clearInputFields() {
        allInputFields.last().waitFor();

        allInputFields.all().forEach(locator -> {
            locator.clear();
            banner.click();
        });
        return (CurrentDialogT) this;
    }

    protected abstract ReturnPageT getReturnPage();

    @Step("Click on the 'Close' button to close form")
    public ReturnPageT clickCloseButton() {
        closeButton.click();

        return getReturnPage();
    }

    @Step("Click on the 'Close' icon to close form")
    public ReturnPageT clickCloseIcon() {
        closeIcon.click();

        return getReturnPage();
    }

}
