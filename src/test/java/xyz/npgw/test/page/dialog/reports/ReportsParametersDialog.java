package xyz.npgw.test.page.dialog.reports;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;
import xyz.npgw.test.page.ReportsPage;
import xyz.npgw.test.page.common.DateRangePickerTrait;
import xyz.npgw.test.page.common.SelectBusinessUnitTrait;
import xyz.npgw.test.page.dialog.BaseDialog;

import java.util.List;

public class ReportsParametersDialog extends BaseDialog<ReportsPage, ReportsParametersDialog>
        implements DateRangePickerTrait<ReportsParametersDialog>, SelectBusinessUnitTrait<ReportsParametersDialog> {

    @Getter
    private final Locator generateButton = buttonByName("Generate");
    private final Locator radioButtonCsv = radioButton("CSV");
    private final Locator radioButtonExcel = radioButton("EXCEL");
    private final Locator radioButtonPdf = radioButton("PDF");
    private final Locator checkboxes = getPage().locator("label:has(input[type='checkbox'])");

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
