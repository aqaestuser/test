package xyz.npgw.test.page.component.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.page.base.BaseComponent;

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
public abstract class BaseTableComponent<CurrentPageT> extends BaseComponent {

    private final Locator root;
    private final Locator columnHeaders;
    private final Locator rows;
    private final Locator headerRow;
    private final Locator firstRow;
    private final Locator lastRow;
    private final Locator rowsPerPage;
    private final Locator rowsPerPageDropdown;
    private final Locator paginationItems;
    private final Locator nextPageButton;
    private final Locator previousPageButton;
    private final Locator leftDotsButton;
    private final Locator firstPageButton;
    private final Locator noRowsToDisplayMessage;
    protected CurrentPageT currentPage;

    public BaseTableComponent(Page page, CurrentPageT currentPage) {
        this(page, currentPage, page.locator("body"));
    }

    public BaseTableComponent(Page page, CurrentPageT currentPage, Locator root) {
        super(page);
        this.currentPage = currentPage;
        this.root = root;

        this.columnHeaders = root.getByRole(AriaRole.COLUMNHEADER);
        this.headerRow = root.locator("[role='row']:has([role='columnheader']):not(:has([role='cell']))");
        this.rows = root.locator("tr[data-key]");
        this.firstRow = root.locator("tr[data-first='true']");
        this.lastRow = root.locator("tr[data-last='true']");

        this.rowsPerPage = root.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Rows Per Page"));
        this.rowsPerPageDropdown = locator("div[data-slot='listbox']");
        this.paginationItems = root.getByLabel("pagination item");
        this.nextPageButton = root.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions()
                .setName("next page button"));
        this.previousPageButton = root.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions()
                .setName("previous page button"));
        this.leftDotsButton = root.locator("li[aria-label='previous page button'] + li[aria-label='dots element']");
        this.firstPageButton = root.getByLabel("pagination item 1", new Locator.GetByLabelOptions().setExact(true));

        this.noRowsToDisplayMessage = root.getByText("No rows to display.",
                new Locator.GetByTextOptions().setExact(true));

        root.getByRole(AriaRole.GRIDCELL, new Locator.GetByRoleOptions().setName("No rows to display."))
                .or(lastRow)
                .waitFor();
    }

    public Locator getColumnHeader(String name) {
        return columnHeaders.getByText(name, new Locator.GetByTextOptions().setExact(true));
    }

    public List<String> getColumnValues(String name) {
        return rows.locator(columnSelector(name)).allInnerTexts();
    }

    public Locator getRow(String content) {
        do {
            try {
                Locator row = rows.filter(new Locator.FilterOptions().setHasText(content));
                row.waitFor(new Locator.WaitForOptions().setTimeout(3000).setState(WaitForSelectorState.ATTACHED));

                return row;
            } catch (PlaywrightException ignored) {
                if (hasNoPagination()) {
                    throw new NoSuchElementException("No rows with '" + content + "'! Table is empty.");
                } else {
                    log.info("Row not found on this page, trying next page.");
                }
            }
        } while (goToNextPage());

        throw new NoSuchElementException("Row with '" + content + "' not found on any page.");
    }

    public Locator getRowByDataKey(String rowDataKey) {
        do {
            if (locator("tr[data-key]").all().stream()
                    .anyMatch(x -> x.getAttribute("data-key").equals(rowDataKey))) {
                return locator("tr[data-key='%s']".formatted(rowDataKey));
            }
        } while (goToNextPage());

        throw new NoSuchElementException("Row with data-key '" + rowDataKey + "' not found on any page.");
    }

    public Locator getCell(int priority, String columnHeader) {
        return getCell(getRowByDataKey(String.valueOf(priority)), columnHeader);
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

    @Step("Click '{name}' column header")
    public CurrentPageT clickColumnHeader(String name) {
        getRoot().getByRole(AriaRole.COLUMNHEADER, new Locator.GetByRoleOptions().setName(name).setExact(true)).click();
        lastRow.waitFor();

        return getCurrentPage();
    }

    @Step("Click sort icon in '{columnName}' column")
    public CurrentPageT clickSortIcon(String columnName) {
        getColumnHeader(columnName).locator("svg").click();
        lastRow.waitFor();

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
        root.getByLabel("pagination item " + pageNumber, new Locator.GetByLabelOptions().setExact(true)).click();

        return getCurrentPage();
    }

    public Locator getActivePageButton() {
        return root.getByLabel(Pattern.compile("pagination item.*active.*", Pattern.CASE_INSENSITIVE));
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

    @Step("Click pagination page button #1")
    public boolean goToFirstPageIfNeeded() {
        if (hasNoPagination()) {
            return false;
        }

        while (leftDotsButton.count() > 0 && leftDotsButton.isVisible()) {
            leftDotsButton.click();
        }

        if (isNotCurrentPage("1")) {
            firstPageButton.click();
        }

        return true;
    }

    public boolean goToLastPage() {
        if (hasNoPagination()) {
            return false;
        }
        clickPaginationPageButton(paginationItems.last().innerText());

        return true;
    }

    public boolean goToNextPage() {
        if (nextPageButton.isDisabled()) {
            return false;
        }
        clickNextPageButton();

        return true;
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

    protected CurrentPageT getCurrentPage() {
        return currentPage;
    }

    protected String columnSelector(String columnHeader) {
        int index = ((Number) getColumnHeader(columnHeader).evaluate("el => el.cellIndex")).intValue() + 1;

        return "td:nth-child(" + index + ")";
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

    private boolean isNotCurrentPage(String number) {
        return !getActivePageButton().innerText().equals(number);
    }

    public interface PageCallback {
        void accept(String pageNumber);
    }
}
