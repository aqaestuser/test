package xyz.npgw.test.page.component.select;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.AccessLevel;
import lombok.Getter;
import xyz.npgw.test.page.base.BaseComponent;
import xyz.npgw.test.page.base.HeaderPage;

@Getter
public class SelectCurrencyComponent<CurrentPageT extends HeaderPage<?>> extends BaseComponent {

    @Getter(AccessLevel.NONE)
    private final CurrentPageT currentPage;
    private final Locator currencySelector = getByRole(AriaRole.BUTTON, "Currency")
            .locator("[data-slot='value']");
    private final Locator currencyDropdown = locator("[data-slot='content'][data-open='true']");
    private final Locator currencyOptions = currencyDropdown.getByRole(AriaRole.OPTION);

    public SelectCurrencyComponent(Page page, CurrentPageT currentPage) {
        super(page);
        this.currentPage = currentPage;
    }

    @Step("Click Currency Selector")
    public CurrentPageT clickCurrencySelector() {
        currencySelector.click();

        return currentPage;
    }

    @Step("Select currency {currency} from dropdown menu")
    public CurrentPageT select(String currency) {
        clickCurrencySelector();
        Locator option = getByRole(AriaRole.OPTION, currency);
        option.waitFor();
        option.click();
        currencyDropdown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
//        getPage().waitForCondition(() -> LocalTime.now().isAfter(THREAD_LAST_ACTIVITY.get()));

        return currentPage;
    }

}
