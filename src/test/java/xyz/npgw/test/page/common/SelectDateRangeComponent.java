package xyz.npgw.test.page.common;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.base.BaseComponent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

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

    @Step("Set start and end date: {startDate} to {endDate}")
    public CurrentPageT setDateRangeFields(String startDate, String endDate) {
        String[] startParts = startDate.split("-");
        String[] endParts = endDate.split("-");

        getByRole(AriaRole.SPINBUTTON).nth(0).fill(startParts[0]);
        getByRole(AriaRole.SPINBUTTON).nth(1).fill(startParts[1]);
        getByRole(AriaRole.SPINBUTTON).nth(2).fill(startParts[2]);


        getByRole(AriaRole.SPINBUTTON).nth(3).fill(endParts[0]);
        getByRole(AriaRole.SPINBUTTON).nth(4).fill(endParts[1]);
        getByRole(AriaRole.SPINBUTTON).nth(5).fill(endParts[2]);

        getPage().waitForCondition(() -> LocalTime.now().isAfter(THREAD_LAST_ACTIVITY.get()));

        return currentPage;
    }

    public CurrentPageT setCurrentMonthRange() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return setDateRangeFields(
                now.with(TemporalAdjusters.firstDayOfMonth()).format(formatter),
                now.with(TemporalAdjusters.lastDayOfMonth()).format(formatter));
    }

    public CurrentPageT setOneWeekBeforeNowRange() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return setDateRangeFields(
                now.with(TemporalAdjusters.previous(now.getDayOfWeek())).format(formatter),
                now.format(formatter));
    }
}
