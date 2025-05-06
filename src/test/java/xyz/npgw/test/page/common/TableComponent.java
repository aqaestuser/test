package xyz.npgw.test.page.common;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.base.BaseComponent;
import xyz.npgw.test.page.dialog.user.ChangeUserActivityDialog;
import xyz.npgw.test.page.dialog.user.EditUserDialog;

import java.util.List;
import java.util.NoSuchElementException;

@Getter
public class TableComponent<CurrentPageT> extends BaseComponent {

    private final Locator tableHeader = getPage().getByRole(AriaRole.COLUMNHEADER);

    private final Locator tableRows = getPage()
            .getByRole(AriaRole.ROW).filter(new Locator.FilterOptions().setHasNot(tableHeader));

    private final CurrentPageT currentPage;

    public TableComponent(Page page, CurrentPageT currentPage) {
        super(page);
        this.currentPage = currentPage;
    }

    private int getColumnHeaderIndexByName(String columnHeaderName) {
        tableHeader.last().waitFor();

        for (int i = 0; i < tableHeader.count(); i++) {
            if (tableHeader.nth(i).innerText().equals(columnHeaderName)) {
                return i;
            }
        }
        throw new NoSuchElementException("Column with header '" + columnHeaderName + "' not found.");
    }

    public Locator getHeaderByName(String name) {

        return tableHeader.getByText(name);
    }

    @Step("Get list of values in column '{columnHeaderName}'")
    public List<String> getColumnValues(String columnHeaderName) {
        Locator header = getHeaderByName(columnHeaderName);
        int columnIndex = ((Number) header.evaluate("el => el.cellIndex")).intValue();
        Locator cells = getPage().locator("tr[role='row'] > td:nth-child(" + (columnIndex + 1) + ")");

        return cells.allInnerTexts();
    }

    public List<String> getColumnHeadersText() {

        return tableHeader
                .all()
                .stream()
                .map(Locator::innerText)
                .toList();
    }

    public Locator getTableRow(String email) {
        Locator rowHeader = getPage().getByRole(AriaRole.ROWHEADER, new Page.GetByRoleOptions().setName(email));

        return  getTableRows().filter(new Locator.FilterOptions().setHas(rowHeader));
    }

    public EditUserDialog clickEditUserButton(String email) {
        getTableRow(email).getByTestId("EditUserButton").click();

        return new EditUserDialog(getPage());
    }

    public ChangeUserActivityDialog clickDeactivateUserButton(String email) {
        getTableRow(email).getByTestId("ChangeUserActivityButton").click();

        return new ChangeUserActivityDialog(getPage());
    }

//    public EditUserDialog clickResetUserPasswordButton(String email) {
//        getTableRow(email).getByTestId("ResetUserPasswordButton").click();
//
//        return new EditUserDialog(getPage());
//    }

    public Locator getUserStatus(String email) {
        int columnIndex = getColumnHeaderIndexByName("Status") - 1;

        return getTableRow(email).getByRole(AriaRole.GRIDCELL).nth(columnIndex);
    }

    public Locator getUserActivityIcon(String email) {
        return getTableRow(email).getByTestId("ChangeUserActivityButton").locator("svg");
    }
}
