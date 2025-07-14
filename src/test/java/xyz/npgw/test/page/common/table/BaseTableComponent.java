package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.page.base.BaseComponent;
import xyz.npgw.test.page.base.HeaderPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@Log4j2
@Getter
public abstract class BaseTableComponent<CurrentPageT extends HeaderPage<?>> extends BaseComponent {

    private final Locator columnHeader = getByRole(AriaRole.COLUMNHEADER);
    private final Locator headersRow = getByRole(AriaRole.ROW).filter(new Locator.FilterOptions().setHas(columnHeader));
    private final Locator rows = getByRole(AriaRole.ROW).filter(new Locator.FilterOptions()
            .setHasNot(columnHeader)
            .setHasNotText("No rows to display."));
    private final Locator firstRow = locator("tr[data-first='true']");

    private final Locator rowsPerPage = getByRole(AriaRole.BUTTON, "Rows Per Page");
    private final Locator rowsPerPageDropdown = locator("div[data-slot='listbox']");
    private final Locator paginationItems = getPage().getByLabel("pagination item");
    private final Locator nextPageButton = getByRole(AriaRole.BUTTON, "next page button");
    private final Locator previousPageButton = getByRole(AriaRole.BUTTON, "previous page button");
    private final Locator noRowsToDisplayMessage = getByTextExact("No rows to display.");

    public BaseTableComponent(Page page) {
        super(page);
        getByRole(AriaRole.GRIDCELL, "No rows to display.")
                .or(firstRow)
                .first()
                .waitFor();
    }

    protected abstract CurrentPageT getCurrentPage();

    public Locator getColumnHeader(String name) {
        return columnHeader.getByText(name, new Locator.GetByTextOptions().setExact(true));
    }

    public List<String> getColumnValues(String name) {
        return rows.locator(columnSelector(name)).allInnerTexts();
    }

    public List<String> getColumnHeadersText() {
        return columnHeader.allInnerTexts();
    }

    public Locator getRow(String rowHeader) {
        do {
            try {
                Locator row = locator("tr[data-key]").filter(new Locator.FilterOptions()
                        .setHasText(rowHeader));
                row.waitFor(new Locator.WaitForOptions().setTimeout(3000).setState(WaitForSelectorState.ATTACHED));
                log.info(row.allInnerTexts());
                return row;
            } catch (PlaywrightException ignored) {
                if (hasNoPagination()) {
                    throw new NoSuchElementException("No rows with data-key '" + rowHeader + "! Table is empty");
                } else {
                    log.info("Row not found on this page, trying next page.");
                }
            }
        } while (goToNextPage());

        throw new NoSuchElementException("Row with data-key '" + rowHeader + "' not found on any page.");
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
        getByLabelExact("transactions table").locator("tr[data-last='true']").waitFor();

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

    public Locator getFirstRowCell(String columnHeader) {
        return firstRow.locator(columnSelector(columnHeader));
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

        if (hasNoPagination()) {
            return rowsPerPage;
        }

        goToFirstPageIfNeeded();

        do {
            rowsPerPage.add(getRows().count());
        } while (goToNextPage());

        goToFirstPageIfNeeded();

        return rowsPerPage;
    }

    @Step("Count rows with {columnHeader} = {values}")
    public int countValues(String columnHeader, String... values) {
        long count = 0;
        Set<String> valueSet = Set.of(values);

        if (goToFirstPageIfNeeded()) {
            do {
                count += getColumnCells(columnHeader).stream()
                        .map(locator -> locator.innerText().trim())
                        .filter(valueSet::contains)
                        .count();
            } while (goToNextPage());
        }
        goToFirstPageIfNeeded();

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
        if (isNotCurrentPage("1")) {
            clickPaginationPageButton("1");
        }

        return true;
    }

    public boolean goToLastPage() {
        if (hasNoPagination()) {
            return false;
        }
        clickPaginationPageButton(getLastPageNumber());

        return true;
    }

    public String getLastPageNumber() {
        return paginationItems.last().innerText();
    }

    private boolean isNotCurrentPage(String number) {
        return !getActivePageButton().innerText().equals(number);
    }

    public boolean goToNextPage() {
        if (nextPageButton.isDisabled()) {
            return false;
        }
        clickNextPageButton();

        return true;
    }

    private int getColumnHeaderIndex(String name) {
        columnHeader.last().waitFor();

        return ((Number) getColumnHeader(name).evaluate("el => el.cellIndex")).intValue();
    }

    protected String columnSelector(String columnHeader) {
        return "td:nth-child(" + (getColumnHeaderIndex(columnHeader) + 1) + ")";
    }

    private boolean hasNextPage() {
        return nextPageButton.isEnabled();
    }

    public boolean hasNoPagination() {
        return paginationItems.first().isHidden();
    }

    public List<String> getColumnValuesFromAllPages(String columnName) {
        return collectAllPages(() -> getColumnValues(columnName).stream().map(String::trim).toList());
    }

    public <T> List<T> getColumnValuesFromAllPages(String columnName, Function<String, T> parser) {
        return collectAllPages(() ->
                getColumnValues(columnName).stream()
                        .map(String::trim)
                        .map(parser)
                        .toList()
        );
    }

    protected <T> List<T> collectAllPages(Supplier<List<T>> currentPageExtractor) {
//        getPage().waitForCondition(() -> LocalTime.now().isAfter(THREAD_LAST_ACTIVITY.get()));
        if (hasNoPagination()) {
            return Collections.emptyList();
        }

        selectRowsPerPageOption("100");
//        getPage().waitForCondition(() -> LocalTime.now().isAfter(THREAD_LAST_ACTIVITY.get()));
        goToFirstPageIfNeeded();

        List<T> allValues = new ArrayList<>();
        do {
            allValues.addAll(currentPageExtractor.get());
        } while (goToNextPage());

        return allValues;
    }

    public Locator getRowByText(String text) {
        firstRow.waitFor();
//        getPage().waitForCondition(() -> LocalTime.now().isAfter(THREAD_LAST_ACTIVITY.get()));

        do {
            Locator row = getRows().filter(new Locator.FilterOptions().setHasText(text));
            if (row.count() > 0) {
                return row;
            }
        } while (goToNextPage());

        throw new NoSuchElementException("Row with text '" + text + "' not found on any page.");
    }

    public interface PageCallback {
        void accept(String pageNumber);
    }
}
