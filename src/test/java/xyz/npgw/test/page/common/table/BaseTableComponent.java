package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.page.base.BaseComponent;
import xyz.npgw.test.page.base.HeaderPage;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

@Log4j2
@Getter
public abstract class BaseTableComponent<CurrentPageT extends HeaderPage<?>> extends BaseComponent {

    private final Locator columnHeader = getByRole(AriaRole.COLUMNHEADER);
    private final Locator headersRow = getByRole(AriaRole.ROW).filter(new Locator.FilterOptions().setHas(columnHeader));
    private final Locator rows = getByRole(AriaRole.ROW).filter(new Locator.FilterOptions().setHasNot(columnHeader));

    private final Locator rowsPerPage = getByRole(AriaRole.BUTTON, "Rows Per Page");
    private final Locator rowsPerPageDropdown = locator("div[data-slot='listbox']");
    private final Locator paginationItems = getPage().getByLabel("pagination item");
    private final Locator nextPageButton = getByRole(AriaRole.BUTTON, "next page button");


    public BaseTableComponent(Page page) {
        super(page);
    }

    protected abstract CurrentPageT getCurrentPage();

    protected int getColumnHeaderIndex(String name) {
        columnHeader.last().waitFor();

        return ((Number) getColumnHeader(name).evaluate("el => el.cellIndex")).intValue();
    }

    public Locator getColumnHeader(String name) {
        return columnHeader.getByText(name);
    }

    public String columnSelector(String columnHeader) {
        return "td:nth-child(" + (getColumnHeaderIndex(columnHeader) + 1) + ")";
    }

    public List<String> getColumnValues(String name) {
        return rows.locator(columnSelector(name)).allInnerTexts();
    }

    public List<String> getColumnHeadersText() {
        return columnHeader.allInnerTexts();
    }

    public Locator getRow(String rowHeader) {
        do {
            Locator header = getByRole(AriaRole.ROWHEADER, rowHeader);

            try {
                header.waitFor(new Locator.WaitForOptions().setTimeout(1000));
                return rows.filter(new Locator.FilterOptions().setHas(header));
            } catch (PlaywrightException ignored) {
                log.info("Row header not found on this page, trying next page.");
            }
        } while (goToNextPage());

        throw new NoSuchElementException("Row with header '" + rowHeader + "' not found on any page.");
    }

    public Locator getCell(String columnHeader, String rowHeader) {
        return rows
                .filter(new Locator.FilterOptions().setHasText(rowHeader))
                .locator(columnSelector(columnHeader));
    }

    public List<Locator> getCells(String columnHeader) {
        return rows.locator(columnSelector(columnHeader)).all();
    }

    @Step("@Step(Click sort icon in '{columnName}' column)")
    public CurrentPageT clickSortIcon(String columnName) {
        getColumnHeader(columnName).locator("svg").click();
        getPage().waitForTimeout(500);

        return getCurrentPage();
    }

    @Step("Click the 'Rows Per Page' dropdown Chevron")
    public CurrentPageT clickRowsPerPageChevron() {
        rowsPerPage.locator("svg").click();

        return getCurrentPage();
    }

    public Locator getRowsPerPageOptions() {
        return rowsPerPageDropdown.locator("li");
    }

    @Step("Select Rows Per Page '{option}'")
    public CurrentPageT selectRowsPerPageOption(String option) {
        clickRowsPerPageChevron();
        rowsPerPageDropdown.getByText(option, new Locator.GetByTextOptions().setExact(true)).click();
        rows.last().waitFor();

        return getCurrentPage();
    }

    public Locator getActivePage() {
        return getPage().getByLabel(Pattern.compile("pagination item.*active.*", Pattern.CASE_INSENSITIVE));
    }

    @Step("Click next page")
    public CurrentPageT clickNextPageButton() {
        nextPageButton.click();

        return getCurrentPage();
    }

    public boolean hasNextPage() {
        return nextPageButton.isEnabled();
    }

    public Locator getFirstRowCell(String columnHeader) {
        return getCells(columnHeader).get(0);
    }

    public boolean hasRow(String name) {
        return getRow(name).count() > 0;
    }

    public boolean goToNextPage() {
        if (!hasNextPage()) {
            return false;
        }
        clickNextPageButton();

        return true;
    }

    public int countAllRows(List<Integer> rowCountsPerPage) {

        return rowCountsPerPage.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public List<Integer> getRowCountsPerPage() {
        List<Integer> rowsPerPage = new ArrayList<>();
        do {
            rowsPerPage.add(getRows().count());
        } while (goToNextPage());

        return rowsPerPage;
    }

    public void forEachPage(String rowsPerPageOption, PageCallback callback) {
        selectRowsPerPageOption(rowsPerPageOption);
        do {
            callback.accept(getActivePage().innerText());
        } while (goToNextPage());
    }

    public interface PageCallback {
        void accept(String pageNumber);
    }
}
