package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.common.AlertTrait;
import xyz.npgw.test.page.common.TableTrait;
import xyz.npgw.test.page.dialog.acquirer.AddAcquirerDialog;
import xyz.npgw.test.page.dialog.acquirer.EditAcquirerDialog;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Getter
public class AcquirersPage extends BaseSystemPage<AcquirersPage>
        implements SelectAcquirerTrait<AcquirersPage>, TableTrait, AlertTrait<AcquirersPage> {

    private final Locator addAcquirerButton = getByTestId("AddAcquirerButton");
    private final Locator addAcquirerDialog = getByRole(AriaRole.DIALOG);
    private final Locator resetFilterButton = getByTestId("ResetFilterButtonAcquirersPage");
    private final Locator refreshDataButton = getByTestId("ApplyFilterButtonAcquirersPage");
    private final Locator acquirerNameHeader = getByTextExact("Acquirer name");
    private final Locator acquirersList = locator("div[data-slot='base'] li");
    private final Locator acquirersStatus = locator("span.flex-1.text-inherit");
    private final Locator rowsPerPage = locator("button[aria-label='Rows Per Page']");
    private final Locator rowsPerPageDropdown = locator("div[data-slot='listbox']");
    private final Locator paginationItems = getPage().getByLabel("pagination item");
    private final Locator paginationNext = getByLabelExact("next page button");

    private final Locator statusLabel = getByLabelExact("Status");
    private final Locator acquirerStatusValue = locator("div[data-slot='innerWrapper'] span").first();
    private final Locator acquirerStatusDropdown = locator("div[data-slot='listbox']");
    private final Locator acquirerStatusOptions = acquirerStatusDropdown.getByRole(AriaRole.OPTION);

    public AcquirersPage(Page page) {
        super(page);
    }

    public List<Locator> getAcquirersStatus() {

        return acquirersStatus.all();
    }

    @Step("Click Status placeholder")
    public AcquirersPage clickAcquirerStatusPlaceholder() {
        acquirerStatusValue.click();

        return this;
    }

    @Step("Select Acquirer Status '{status}'")
    public AcquirersPage selectAcquirerStatus(String status) {
        acquirerStatusOptions
                .getByText(status, new Locator.GetByTextOptions().setExact(true))
                .click();
        acquirerStatusDropdown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));

        return this;
    }

    @Step("Click Add Acquirer")
    public AddAcquirerDialog clickAddAcquirer() {
        addAcquirerButton.click();

        return new AddAcquirerDialog(getPage());
    }

    @Step("Click 'Edit' button to edit acquirer")
    public EditAcquirerDialog clickEditAcquirerButton(Locator row) {
        row.locator("button[data-testid='EditAcquirerButton']").click();

        return new EditAcquirerDialog(getPage());
    }

    public Locator getEditAcquirerButton(Locator row) {

        return row.locator("button[data-testid='EditAcquirerButton']");
    }

    public Locator getChangeAcquirerActivityButton(Locator row) {

        return row.locator("button[data-testid='ChangeBusinessUnitActivityButton']");
    }

    @Step("Click the 'Rows Per Page' dropdown Chevron")
    public AcquirersPage clickRowsPerPageChevron() {
        rowsPerPage.locator("svg").click();

        return this;
    }

    public Locator getRowsPerPageOptions() {
        return rowsPerPageDropdown.locator("li");
    }

    @Step("Select Rows Per Page '{option}'")
    public AcquirersPage selectRowsPerPageOption(String option) {
        rowsPerPageDropdown.getByText(option, new Locator.GetByTextOptions().setExact(true)).click();
        getTable().getTableRows().last().waitFor();

        return this;
    }

    public Locator getActivePage() {
        return getPage().getByLabel(Pattern.compile("pagination item.*active.*", Pattern.CASE_INSENSITIVE));
    }

    @Step("Click on page '{pageNumber}'")
    public AcquirersPage clickOnPaginationPage(String pageNumber) {
        getPage().getByLabel("pagination item " + pageNumber).click();

        return this;
    }

    @Step("Click next page")
    public AcquirersPage clickNextPage() {
        paginationNext.click();

        return this;
    }

    public boolean isLastPage() {

        return Objects.equals(paginationNext.getAttribute("tabindex"), "-1");
    }
}
