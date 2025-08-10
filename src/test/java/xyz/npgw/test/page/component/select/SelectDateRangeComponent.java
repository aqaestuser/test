package xyz.npgw.test.page.component.select;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.base.BaseComponent;

import java.util.Arrays;
import java.util.List;

@Getter
public class SelectDateRangeComponent<CurrentPageT> extends BaseComponent {

    private final Locator errorMessage = locator("[data-slot='error-message']");
    private final Locator dateRangeField = getByRole(AriaRole.GROUP, "DateRange");
    private final Locator startDate = locator("//div[@data-slot='start-input']");
    private final Locator endDate = locator("//div[@data-slot='end-input']");
    private final CurrentPageT currentPage;

    public SelectDateRangeComponent(Page page, CurrentPageT currentPage) {
        super(page);
        this.currentPage = currentPage;
    }

    @Step("Set date range: {dateRange}")
    public CurrentPageT setDateRangeFields(String dateRange) {
        String[] dates = dateRange.split("-");
        List<String> startDate = Arrays.stream(dates[0].split("/")).map(String::trim).toList();
        List<String> endDate = Arrays.stream(dates.length == 1 ? dates[0].split("/") : dates[1].split("/"))
                .map(String::trim)
                .toList();

        getByRole(AriaRole.SPINBUTTON).nth(0).fill(startDate.get(0));
        getByRole(AriaRole.SPINBUTTON).nth(1).fill(startDate.get(1));
        getByRole(AriaRole.SPINBUTTON).nth(2).fill(startDate.get(2));


        getByRole(AriaRole.SPINBUTTON).nth(3).fill(endDate.get(0));
        getByRole(AriaRole.SPINBUTTON).nth(4).fill(endDate.get(1));
        getByRole(AriaRole.SPINBUTTON).nth(5).fill(endDate.get(2));

        return currentPage;
    }
}
