package xyz.npgw.test.page.dialog.reports;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.Getter;
import xyz.npgw.test.page.ReportsPage;
import xyz.npgw.test.page.common.DateRangePickerTrait;
import xyz.npgw.test.page.common.SelectBusinessUnitTrait;
import xyz.npgw.test.page.dialog.BaseDialog;

import java.util.List;

public class ReportsParametersDialog extends BaseDialog<ReportsPage, ReportsParametersDialog>
        implements DateRangePickerTrait<ReportsParametersDialog>, SelectBusinessUnitTrait<ReportsParametersDialog> {

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
}
