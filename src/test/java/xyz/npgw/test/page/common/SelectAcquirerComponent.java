package xyz.npgw.test.page.common;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.page.base.BaseComponent;

import java.time.LocalTime;
import java.util.NoSuchElementException;

@Log4j2
public class SelectAcquirerComponent<CurrentPageT> extends BaseComponent {

    @Getter
    private final Locator selectAcquirerField = getByLabelExact("Select acquirer");
    private final Locator dropdownOptionList = getByRole(AriaRole.OPTION);
    private final Locator selectAcquirerContainer = locator("div[data-slot='input-wrapper']");
    private final Locator selectAcquirerDropdownChevron = selectAcquirerContainer
            .locator("button[aria-label='Show suggestions']:last-child");
    private final Locator selectAcquirerClearIcon = selectAcquirerContainer
            .locator("button[aria-label='Show suggestions']:first-child");

    private final CurrentPageT page;

    public SelectAcquirerComponent(Page page, CurrentPageT currentPage) {
        super(page);
        this.page = currentPage;
    }

    @Step("Click 'Select acquirer' field")
    public CurrentPageT clickSelectAcquirerField() {
        selectAcquirerField.click();

        return page;
    }

    @Step("Enter '{acquirerName}' into select acquirer field")
    public CurrentPageT typeName(String acquirerName) {
        selectAcquirerField.fill(acquirerName);

        return page;
    }


    @Step("Click '{acquirerName}' in dropdown")
    public CurrentPageT clickAcquirerInDropdown(String acquirerName) {
        dropdownOptionList.getByText(acquirerName, new Locator.GetByTextOptions().setExact(true)).click();

        return page;
    }

    public Locator getSelectAcquirersDropdownItems() {
        dropdownOptionList.last().waitFor();

        return dropdownOptionList;
    }

    public Locator getAcquirerInDropdownOption(String acquirerName) {
        return dropdownOptionList.filter(new Locator.FilterOptions().setHas(getByTextExact(acquirerName)));
    }

    @Step("Select '{acquirerName}' acquirer using filter")
    public CurrentPageT selectAcquirer(String acquirerName) {
        getPage().waitForCondition(() -> LocalTime.now().isAfter(THREAD_LAST_ACTIVITY.get()));

        String lastName = "";
        selectAcquirerField.fill(acquirerName);

        if (dropdownOptionList.all().isEmpty()) {
            throw new NoSuchElementException("Business unit '" + acquirerName + "' not found in dropdown.");
        }

        while (getAcquirerInDropdownOption(acquirerName).all().isEmpty()) {
            if (dropdownOptionList.last().innerText().equals(lastName)) {
                throw new NoSuchElementException("Business unit '" + acquirerName + "' not found in dropdown.");
            }
            dropdownOptionList.last().scrollIntoViewIfNeeded();

            lastName = dropdownOptionList.last().innerText();
        }

        getAcquirerInDropdownOption(acquirerName).click();

        return page;
    }

    @Step("Click acquirer dropdown toggle arrow '˅˄'")
    public CurrentPageT clickAcquirerDropdownChevron() {
        selectAcquirerDropdownChevron.click();

        return page;
    }

    @Step("Click select Acquirer unit clear icon")
    public CurrentPageT clickSelectAcquirerClearIcon() {
        selectAcquirerClearIcon.dispatchEvent("click");

        return page;
    }

    public boolean isAcquirerAbsent(String acquirerName) {
        getPage().waitForCondition(() -> selectAcquirerField.inputValue().isEmpty());
        try {
            selectAcquirer(acquirerName);
            return false;
        } catch (NoSuchElementException e) {
            return true;
        }
    }
}
