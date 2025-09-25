package xyz.npgw.test.page.dialog;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.base.BaseModel;
import xyz.npgw.test.page.component.AlertTrait;

import java.util.List;
import java.util.Objects;

@Getter
@SuppressWarnings("unchecked")
public abstract class BaseDialog<
        ReturnPageT extends BaseModel,
        CurrentDialogT extends BaseDialog<ReturnPageT, CurrentDialogT>>
        extends BaseModel implements AlertTrait<CurrentDialogT> {

    private final Locator dialog = getByRole(AriaRole.DIALOG);
    private final Locator dialogHeader = locator("section header");
    private final Locator closeButton = dialog.getByText("Close");
    private final Locator closeIcon = dialog.getByLabel("Close");
    private final Locator inputFields = dialog.getByRole(AriaRole.TEXTBOX);
    private final Locator allPlaceholdersWithoutSearch = dialog.locator(
            "[data-slot='input']:not([placeholder='Search...'])");
    private final Locator fieldLabel = dialog.locator("label[data-slot='label']");
    private final Locator modalWindowsMainTextBody = locator("div.flex.flex-1.flex-col.gap-3.px-6.py-2");

    public BaseDialog(Page page) {
        super(page);
//        dialog.waitFor();
    }

    protected abstract ReturnPageT getReturnPage();

    public List<String> getAllPlaceholders() {
        allPlaceholdersWithoutSearch.first().waitFor();

        return allPlaceholdersWithoutSearch.all().stream()
                .map(l -> l.getAttribute("placeholder"))
                .filter(Objects::nonNull)
                .toList();
    }

    protected CurrentDialogT getCurrentDialog() {
        return (CurrentDialogT) this;
    }

    @Step("Clear '{label}' input field")
    public CurrentDialogT clearInput(String label) {
        inputFields.last().waitFor();
        inputFields.getByLabel(label).clear();

        return getCurrentDialog();
    }

    @Step("Click on the 'Close' button to close form")
    public ReturnPageT clickCloseButton() {
        closeButton.click();

        return getReturnPage();
    }

    @Step("Click on the 'Close' icon to close form")
    public ReturnPageT clickCloseIcon() {
        closeIcon.click();
        dialog.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));

        return getReturnPage();
    }

    @Step("Press 'Escape' to cancel")
    public ReturnPageT pressEscapeKey() {
        getPage().keyboard().press("Escape");

        return getReturnPage();
    }

    @Step("Get Modal window title")
    public Locator getModalWindowHeaderTitle() {
        dialogHeader.waitFor();

        return dialogHeader;
    }

    @Step("Get Modal window main body")
    public Locator getModalWindowMainBodyText() {
        modalWindowsMainTextBody.waitFor();

        return modalWindowsMainTextBody;
    }
}
