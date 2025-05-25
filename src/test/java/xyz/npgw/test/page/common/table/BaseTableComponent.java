package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.base.BaseComponent;
import xyz.npgw.test.page.base.HeaderPage;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Getter
public abstract class BaseTableComponent<CurrentPageT extends HeaderPage> extends BaseComponent {

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
        Locator header = getPage().getByRole(AriaRole.ROWHEADER, new Page.GetByRoleOptions().setName(rowHeader));

        return rows.filter(new Locator.FilterOptions().setHas(header));
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
        rowsPerPageDropdown.getByText(option, new Locator.GetByTextOptions().setExact(true)).click();
        rows.last().waitFor();

        return getCurrentPage();
    }

    public Locator getActivePage() {
        return getPage().getByLabel(Pattern.compile("pagination item.*active.*", Pattern.CASE_INSENSITIVE));
    }

    @Step("Click on page '{pageNumber}'")
    public CurrentPageT clickOnPaginationPage(String pageNumber) {
        getPage().getByLabel("pagination item " + pageNumber).click();

        return getCurrentPage();
    }

    @Step("Click next page")
    public CurrentPageT clickNextPage() {
        nextPageButton.click();

        return getCurrentPage();
    }

    public  Locator getActivePaginationPage(String number) {
        return getByRole(AriaRole.BUTTON, "pagination item " + number + " active");
    }

    public boolean isNotLastPage() {
        return !Objects.equals(nextPageButton.getAttribute("tabindex"), "-1");
    }

    public Locator getFirstRowCell(String columnHeader) {
        return getCells(columnHeader).get(0);
    }

}
