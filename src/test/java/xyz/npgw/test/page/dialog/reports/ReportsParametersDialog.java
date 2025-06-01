package xyz.npgw.test.page.dialog.reports;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.ReportsPage;
import xyz.npgw.test.page.common.trait.SelectBusinessUnitTrait;
import xyz.npgw.test.page.common.trait.SelectDateRangeTrait;
import xyz.npgw.test.page.dialog.BaseDialog;

import java.util.List;

public class ReportsParametersDialog extends BaseDialog<ReportsPage, ReportsParametersDialog>
        implements SelectDateRangeTrait<ReportsParametersDialog>, SelectBusinessUnitTrait<ReportsParametersDialog> {

    @Getter
    private final Locator generateButton = getByRole(AriaRole.BUTTON, "Generate");
    private final Locator radioButtonCsv = getByRole(AriaRole.RADIO, "CSV");
    private final Locator radioButtonExcel = getByRole(AriaRole.RADIO, "EXCEL");
    private final Locator radioButtonPdf = getByRole(AriaRole.RADIO, "PDF");
    private final Locator checkboxes = locator("label:has(input[type='checkbox'])");

    public ReportsParametersDialog(Page page) {
        super(page);
    }

    @Override
    protected ReportsPage getReturnPage() {

        return new ReportsPage(getPage());
    }

    public List<String> getReportColumns() {

        return checkboxes
                .all()
                .stream()
                .skip(1)
                .map(l -> l.locator("span").last().innerText())
                .toList();
    }

    @Step("Click on the checkbox 'Report columns'")
    public ReportsParametersDialog clickReportColumnsCheckbox() {
        checkboxes.first().click();

        return this;
    }

    public boolean isAllColumnsChecked() {
        checkboxes.last().waitFor();

        int totalColumns = checkboxes.count() - 1;
        int checked = 0;
        for (int i = 1; i <= totalColumns; i++) {
            if (checkboxes.nth(i).getAttribute("data-selected") != null) {
                checked++;
            }
        }
        return checked == totalColumns;
    }

    public boolean isAllColumnsUnchecked() {
        checkboxes.last().waitFor();

        int totalColumns = checkboxes.count() - 1;
        int checked = 0;
        for (int i = 1; i <= totalColumns; i++) {
            if (checkboxes.nth(i).getAttribute("data-selected") != null) {
                checked++;
            }
        }
        return checked == 0;
    }

    @Step("Hover on the checkbox 'Report columns'")
    public void hoverOnReportColumnsCheckbox() {
        checkboxes.first().hover();
    }

    public boolean isTextVisible(String text) {

        return getByTextExact(text).isVisible();
    }

    @Step("Click on the all column checkboxes one by one")
    public void clickAllColumnCheckboxesOneByOne() {
        checkboxes.last().waitFor();

        int totalColumns = checkboxes.count() - 1;
        for (int i = 1; i <= totalColumns; i++) {
            checkboxes.nth(i).dispatchEvent("click");
        }
    }
}
