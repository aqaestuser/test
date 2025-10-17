package xyz.npgw.test.page.component.select;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;

public class SelectAcquirerMidComponent<CurrentPageT> extends SelectComponent<CurrentPageT> {

    @Getter
    private final Locator selectAcquirerMidField = getByLabelExact("Select acquirer MID");
    private final Locator dialogSelectAcquirerMidField = getByRole(AriaRole.DIALOG)
            .getByLabel("Select acquirer MID", new Locator.GetByLabelOptions().setExact(true))
            .locator("../input");
    private final Locator selectAcquirerDialogField = locator("input[aria-label='Select acquirer']");
    @Getter
    private final Locator dropdownOptionList = getByRole(AriaRole.OPTION);
    private final Locator selectAcquirerContainer = locator("div[data-slot='input-wrapper']");
    private final Locator selectAcquirerDropdownChevron = selectAcquirerContainer
            .locator("button[aria-label='Show suggestions']:last-child");
    private final Locator selectAcquirerClearIcon = selectAcquirerContainer
            .locator("button:first-child");

    public SelectAcquirerMidComponent(Page page, CurrentPageT currentPage) {
        super(page, currentPage);
    }

    @Step("Click 'Select acquirer' field")
    public CurrentPageT clickSelectAcquirerMidField() {
        selectAcquirerMidField.click();

        return currentPage;
    }

    @Step("Type '{acquirerName}' into 'Select acquirer' field")
    public CurrentPageT typeName(String acquirerName) {
        selectAcquirerMidField.pressSequentially(acquirerName, new Locator.PressSequentiallyOptions().setDelay(1));

        return currentPage;
    }


    @Step("Click '{acquirerName}' in dropdown")
    public CurrentPageT clickAcquirerInDropdown(String acquirerName) {
        dropdownOptionList.getByText(acquirerName, new Locator.GetByTextOptions().setExact(true)).click();

        return currentPage;
    }

    public Locator getSelectAcquirersDropdownItems() {
        dropdownOptionList.last().waitFor();

        return dropdownOptionList;
    }

    @Step("Select '{acquirerName}' acquirer using filter")
    public CurrentPageT selectAcquirerMid(String acquirerName) {
        select(selectAcquirerMidField, acquirerName);

        return currentPage;
    }

    @Step("Select '{acquirerName}' acquirer within dialog")
    public CurrentPageT selectAcquirerInDialog(String acquirerName) {
        select(selectAcquirerDialogField, acquirerName);

        return currentPage;
    }

    @Step("Select '{acquirerName}' acquirer MID within dialog")
    public CurrentPageT selectAcquirerMidInDialog(String acquirerName) {
        select(dialogSelectAcquirerMidField, acquirerName);

        return currentPage;
    }

    @Step("Click acquirer dropdown toggle arrow '˅˄'")
    public CurrentPageT clickAcquirerDropdownChevron() {
        selectAcquirerDropdownChevron.click();

        return currentPage;
    }

    @Step("Click select Acquirer unit clear icon")
    public CurrentPageT clickSelectAcquirerClearIcon() {
        selectAcquirerClearIcon.dispatchEvent("click");

        return currentPage;
    }

    public boolean isAcquirerPresent(String acquirerName) {
        return getAllOptions(selectAcquirerMidField, acquirerName).contains(acquirerName);
    }
}
