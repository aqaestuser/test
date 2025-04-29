package xyz.npgw.test.page.common;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.AccessLevel;
import lombok.Getter;
import xyz.npgw.test.page.base.BaseComponent;

@Getter
public class DateRangePickerComponent<CurrentPageT> extends BaseComponent {

    @Getter(AccessLevel.NONE)
    private final Locator dateRangeFields = spinButton();
    private final Locator dataRangePickerErrorMessage = locator("[data-slot='error-message']");
    private final Locator dateRangePickerField = group("DateRange");

    private final CurrentPageT currentPage;

    public DateRangePickerComponent(Page page, CurrentPageT currentPage) {
        super(page);
        this.currentPage = currentPage;
    }

    @Step("Set start and end date: {startDate} to {endDate}")
    public CurrentPageT setDateRangeFields(String startDate, String endDate) {
        String[] startParts = startDate.split("-");
        String[] endParts = endDate.split("-");

        dateRangeFields.nth(0).fill(startParts[0]);
        dateRangeFields.nth(1).fill(startParts[1]);
        dateRangeFields.nth(2).fill(startParts[2]);


        dateRangeFields.nth(3).fill(endParts[0]);
        dateRangeFields.nth(4).fill(endParts[1]);
        dateRangeFields.nth(5).fill(endParts[2]);

        return currentPage;
    }
}
