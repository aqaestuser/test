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
import java.util.function.Function;
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
    private final Locator previousPageButton = getByRole(AriaRole.BUTTON, "previous page button");

    public BaseTableComponent(Page page) {
        super(page);
    }

    protected abstract CurrentPageT getCurrentPage();

    public Locator getColumnHeader(String name) {
        return columnHeader.getByText(name);
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
                if (hasNoPagination()) {
                    throw new NoSuchElementException("Row with header '" + rowHeader + "' isn't found! Table is empty");
                } else {
                    log.info("Row header not found on this page, trying next page.");
                }
            }

        } while (goToNextPage());

        throw new NoSuchElementException("Row with header '" + rowHeader + "' not found on any page.");
    }

    public Locator getCell(String rowHeader, String columnHeader) {
        return getCell(getRow(rowHeader), columnHeader);
    }

    public Locator getCell(Locator row, String columnHeader) {
        return row.locator(columnSelector(columnHeader));
    }

    public List<Locator> getColumnCells(String columnHeader) {
        return rows.locator(columnSelector(columnHeader)).all();
    }

    @Step("Click sort icon in '{columnName}' column")
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

    @Step("Click pagination page button '{pageNumber}'")
    public CurrentPageT clickPaginationPageButton(String pageNumber) {
        getByLabelExact("pagination item " + pageNumber).click();

        return getCurrentPage();
    }

    public Locator getActivePageButton() {
        return getPage().getByLabel(Pattern.compile("pagination item.*active.*", Pattern.CASE_INSENSITIVE));
    }

    @Step("Click next page button")
    public CurrentPageT clickNextPageButton() {
        nextPageButton.click();

        return getCurrentPage();
    }

    @Step("Click previous page button")
    public CurrentPageT clickPreviousPageButton() {
        previousPageButton.click();

        return getCurrentPage();
    }

    public Locator getFirstRowCell(String columnHeader) {
        return getColumnCells(columnHeader).get(0);
    }

    public boolean hasRow(String rowHeader) {
        return getRow(rowHeader).count() > 0;
    }

    public int countAllRows() {

        return getRowCountsPerPage().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public int countAllRows(List<Integer> rowCountsPerPage) {

        return rowCountsPerPage.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public List<Integer> getRowCountsPerPage() {
        List<Integer> rowsPerPage = new ArrayList<>();
        goToFirstPageIfNeeded();

        do {
            rowsPerPage.add(getRows().count());
        } while (goToNextPage());

        return rowsPerPage;
    }

    public int countValue(String columnHeader, String value) {
        long count = 0;
        if (goToFirstPageIfNeeded()) {
            do {
                count += getColumnCells(columnHeader).stream()
                        .filter(locator -> locator.innerText().equals(value))
                        .count();
            } while (goToNextPage());
        }
        return (int) count;
    }

    public void forEachPage(String rowsPerPageOption, PageCallback callback) {
        selectRowsPerPageOption(rowsPerPageOption);
        goToFirstPageIfNeeded();

        do {
            callback.accept(getActivePageButton().innerText());
        } while (goToNextPage());
    }

    public boolean goToFirstPageIfNeeded() {
        if (hasNoPagination()) {
            return false;
        }
        if (!isCurrentPage("1")) {
            clickPaginationPageButton("1");
        }

        return true;
    }

    private boolean isCurrentPage(String number) {
        return getActivePageButton().innerText().equals(number);
    }

    private boolean goToNextPage() {
        if (!hasNextPage()) {
            return false;
        }
        clickNextPageButton();

        return true;
    }

    private int getColumnHeaderIndex(String name) {
        columnHeader.last().waitFor();

        return ((Number) getColumnHeader(name).evaluate("el => el.cellIndex")).intValue();
    }

    private String columnSelector(String columnHeader) {
        return "td:nth-child(" + (getColumnHeaderIndex(columnHeader) + 1) + ")";
    }

    private boolean hasNextPage() {
        return nextPageButton.isEnabled();
    }

    private boolean hasNoPagination() {
        return !paginationItems.first().isVisible();
    }

    public interface PageCallback {
        void accept(String pageNumber);
    }

    public <T> List<T> getColumnValuesFromAllPages(String columnName, Function<String, T> parser) {
        selectRowsPerPageOption("100");
        goToFirstPageIfNeeded();

        List<T> allValues = new ArrayList<>();
        do {
            List<T> pageValues = getColumnValues(columnName).stream()
                    .map(String::trim)
                    .map(parser)
                    .toList();
            allValues.addAll(pageValues);
        } while (goToNextPage());

        return allValues;
    }
}
