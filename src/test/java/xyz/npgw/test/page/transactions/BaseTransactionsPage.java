package xyz.npgw.test.page.transactions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import xyz.npgw.test.common.entity.CardType;
import xyz.npgw.test.common.entity.Currency;
import xyz.npgw.test.common.entity.Status;
import xyz.npgw.test.common.entity.Transaction;
import xyz.npgw.test.common.entity.Type;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.base.HeaderPage;
import xyz.npgw.test.page.component.select.SelectBusinessUnitTrait;
import xyz.npgw.test.page.component.select.SelectCurrencyTrait;
import xyz.npgw.test.page.component.select.SelectDateRangeTrait;
import xyz.npgw.test.page.component.select.SelectStatusTrait;
import xyz.npgw.test.page.component.table.TransactionsTableTrait;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public abstract class BaseTransactionsPage<CurrentPageT extends BaseTransactionsPage<CurrentPageT>>
        extends HeaderPage<CurrentPageT>
        implements SelectBusinessUnitTrait<CurrentPageT>,
        SelectDateRangeTrait<CurrentPageT>,
        SelectCurrencyTrait<CurrentPageT>,
        SelectStatusTrait<CurrentPageT>,
        TransactionsTableTrait<CurrentPageT> {

    private final Locator businessUnitSelector = getByTextExact("Business unit").locator("../../..");
    private final Locator cardTypeSelector = getByLabelExact("Card type");
    private final Locator cardTypeValue = getByRole(AriaRole.BUTTON, "Card type");

    private final Locator searchTrxIdsButton = getByRole(AriaRole.BUTTON, "Trx IDs");
    private final Locator trxIdAppliedButton =
            locator("button").filter(new Locator.FilterOptions().setHasText("Trx Id"));
    private final Locator trxIdPencil = getByRole(AriaRole.BUTTON, "Trx Id").locator("svg[data-icon='pencil']");
    private final Locator trxIdClearIcon = getByRoleExact(AriaRole.BUTTON, "close chip");

    private final Locator npgwReferenceField = getByLabelExact("NPGW reference");
    private final Locator npgwReferenceAcceptButton = npgwReferenceField.locator("..")
            .locator("svg[data-icon='circle-check']");
    private final Locator npgwReferenceFieldClearIcon = npgwReferenceField.locator("..")
            .locator("svg[data-icon='circle-xmark']");
    private final Locator businessUnitReference = getByLabelExact("Business unit reference");
    private final Locator businessUnitReferenceClear = getByRole(AriaRole.BUTTON, "clear input").last();

    private final Locator resetFilterButton = getByTestId("ResetFilterButtonTransactionsPage");
    private final Locator refreshDataButton = locator("[data-icon='arrows-rotate']");
    private final Locator settingsButton = getByTestId("SettingsButtonTransactionsPage");
    private final Locator downloadButton = getByTestId("ExportToFileuttonTransactionsPage"); //TODO missing B in utton
    private final Locator amountButton = getByRole(AriaRole.BUTTON, "Amount");
    private final Locator amountFromField = getByLabelExact("From").locator("..");
    private final Locator amountToField = getByLabelExact("To").locator("..");
    private final Locator clearAmountFromButton = amountFromField.locator("//button[@aria-label='clear input']");
    private final Locator clearAmountToButton = amountToField.locator("//button[@aria-label='clear input']");
    private final Locator amountFromInputField = amountFromField.locator("//input[@type='text']");
    private final Locator amountToInputField = amountToField.locator("//input[@type='text']");
    private final Locator amountFromIncreaseArrow = amountFromField.locator("//button[@aria-label='Increase From']");
    private final Locator amountFromDecreaseArrow = amountFromField.locator("//button[@aria-label='Decrease From']");
    private final Locator amountToIncreaseArrow = amountToField.locator("//button[@aria-label='Increase To']");
    private final Locator amountToDecreaseArrow = amountToField.locator("//button[@aria-label='Decrease To']");
    private final Locator amountApplyButton = getByRole(AriaRole.BUTTON, "Apply");
    private final Locator amountClearButton = getByTextExact("Clear");
    private final Locator amountAppliedClearButton = getByLabelExact("close chip");
    private final Locator amountErrorMessage = locator("[data-slot='error-message']");
    private final Locator cardTypeOptions = locator("ul[data-slot='listbox']").getByRole(AriaRole.OPTION);
    private final Locator settingsVisibleColumnCheckbox = getByRole(AriaRole.CHECKBOX);
    private final Locator settingsArrowsUpDown = locator("svg[data-icon='arrows-up-down']");
    private final Locator amountEditButton = locator("svg[data-icon='pencil']");
    private final Locator downloadCsvOption = getByRole(AriaRole.MENUITEM, "CSV");
    private final Locator downloadExcelOption = getByRole(AriaRole.MENUITEM, "EXCEL");
    private final Locator downloadPdfOption = getByRole(AriaRole.MENUITEM, "PDF");
    private final Locator dialog = locator("[role='dialog']");
    private final Locator dropdownMenuContent = locator("[data-slot='content'][data-open='true']");
    private final Locator amountApplied = locator("//*[@data-icon='pencil']/..");
    private final Locator columns = getByRole(AriaRole.MENUITEM).getByRole(AriaRole.BUTTON);

    public BaseTransactionsPage(Page page) {
        super(page);
    }

    @Step("Click 'Refresh Data' button")
    public CurrentPageT clickRefreshDataButton() {
        refreshDataButton.click();

        return self();
    }

    @Step("Click 'Amount' button")
    public CurrentPageT clickAmountButton() {
        amountButton.click();

        return self();
    }

    @Step("Fill 'From' amount value")
    public CurrentPageT fillAmountFromField(String value) {
        amountFromInputField.fill(value);
        amountFromField.press("Tab"); //TODO remove this after bug fix

        return self();
    }

    @Step("Fill 'To' amount value")
    public CurrentPageT fillAmountToField(String value) {
        amountToInputField.fill(value);
        amountToField.press("Tab"); //TODO remove this after bug fix

        return self();
    }

    @Step("Clear 'From' amount input field ")
    public CurrentPageT clickClearAmountFromButton() {
        clearAmountFromButton.click(new Locator.ClickOptions().setForce(true));

        return self();
    }

    @Step("Clear 'To' amount input field ")
    public CurrentPageT clickClearAmountToButton() {
        clearAmountToButton.click(new Locator.ClickOptions().setForce(true));

        return self();
    }

    @Step("Click 'From' amount increase arrow ")
    public CurrentPageT clickAmountFromIncreaseArrow() {
        amountFromIncreaseArrow.click();

        return self();
    }

    @Step("Click 'From' amount decrease arrow ")
    public CurrentPageT clickAmountFromDecreaseArrow() {
        amountFromDecreaseArrow.click();

        return self();
    }

    @Step("Click 'To' amount increase arrow ")
    public CurrentPageT clickAmountToIncreaseArrow() {
        amountToIncreaseArrow.click();

        return self();
    }

    @Step("Click 'To' amount decrease arrow ")
    public CurrentPageT clickAmountToDecreaseArrow() {
        amountToDecreaseArrow.click();

        return self();
    }

    @Step("Click 'Apply' amount button")
    public CurrentPageT clickAmountApplyButton() {
        amountApplyButton.click();
        dropdownMenuContent.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));

        return self();
    }

    @Step("Click 'Clear' amount button")
    public CurrentPageT clickAmountClearButton() {
        amountClearButton.click();

        return self();
    }

    @Step("Click clear applied amount button")
    public CurrentPageT clickAmountAppliedClearButton() {
        amountAppliedClearButton.click();

        return self();
    }

    @Step("Click 'Card type' selector")
    public CurrentPageT clickCardTypeSelector() {
        cardTypeSelector.click();

        return self();
    }

    @Step("Click on the Settings button")
    public CurrentPageT clickSettingsButton() {
        settingsButton.click();
        getByTextExact("Visible columns").waitFor();

        return self();
    }

    @Step("Uncheck all 'Visible columns' in Settings")
    public CurrentPageT uncheckAllCheckboxInSettings() {
        settingsVisibleColumnCheckbox
                .all()
                .forEach(this::uncheckIfSelected);

        return self();
    }

    @Step("Uncheck Visible column '{name}' in Settings")
    public CurrentPageT uncheckVisibleColumn(String name) {
        settingsVisibleColumnCheckbox
                .all()
                .stream()
                .filter(l -> name.equals(l.getAttribute("aria-label")))
                .findFirst()
                .ifPresent(this::uncheckIfSelected);

        return self();
    }

    @Step("Check all 'Visible columns' in Settings")
    public CurrentPageT checkAllCheckboxInSettings() {
        settingsVisibleColumnCheckbox
                .all()
                .forEach(this::checkIfNotSelected);

        return self();
    }

    @Step("Check visible column '{name}' in Settings")
    public CurrentPageT checkVisibleColumn(String name) {
        settingsVisibleColumnCheckbox
                .all()
                .stream()
                .filter(l -> name.equals(l.getAttribute("aria-label")))
                .findFirst()
                .ifPresent(this::checkIfNotSelected);

        return self();
    }

    @Step("Click amount 'Edit' button")
    public CurrentPageT clickAmountEditButton() {
        amountEditButton.click();

        return self();
    }

    @Step("Click 'Download' button")
    public CurrentPageT clickDownloadButton() {
        downloadButton.click();

        return self();
    }

    @Step("Click 'Reset filter' button")
    public CurrentPageT clickResetFilterButton() {
        resetFilterButton.click();

        return self();
    }

    @Step("Click Search 'Trx IDs' button")
    public CurrentPageT clickSearchTrxIdsButton() {
        searchTrxIdsButton.click();

        return self();
    }

    @Step("Fill '{value}' into 'NPGW reference' field")
    public CurrentPageT fillNpgwReference(String value) {
        npgwReferenceField.fill(value);
        return self();
    }

    @Step("Click NPGW reference acceptation button")
    public CurrentPageT clickNpgwReferenceAcceptButton() {
        npgwReferenceAcceptButton.click();

        return self();
    }

    @Step("Click TrxId Clear Icon")
    public CurrentPageT clickTrxIdClearIcon() {
        trxIdClearIcon.click();

        return self();
    }

    @Step("Click TrxId Pencil Icon")
    public CurrentPageT clickTrxIdPencilIcon() {
        trxIdPencil.click();

        return self();
    }

    @Step("Click 'Npgw reference' Clear Icon")
    public CurrentPageT clickNpgwReferenceClearIcon() {
        npgwReferenceFieldClearIcon.click();

        return self();
    }

    @Step("Select Card type '{value}' from dropdown menu")
    public CurrentPageT selectCardType(String value) {
        cardTypeSelector.click();
        getByRole(AriaRole.OPTION, value).click();

        return self();
    }

    public String getRequestData() {
        AtomicReference<String> data = new AtomicReference<>("");
        getPage().waitForResponse(response -> {
            if (response.url().contains("/transaction/status")) {
                data.set(response.request().postData());
                return true;
            }
            return false;
        }, refreshDataButton::click);
        return data.get();
    }

    @Step("Click 'Export table data to file' button")
    public CurrentPageT clickExportTableDataToFileButton() {
        downloadButton.click();
        getPage().waitForTimeout(3000); //TODO replace with adequate wait for 10000 transactions for export

        return self();
    }

    @Step("Select 'CSV' option")
    public CurrentPageT selectCsv() {
        downloadCsvOption.click();

        return self();
    }

    @Step("Select 'PDF' option")
    public CurrentPageT selectPdf() {
        downloadPdfOption.click();

        return self();
    }

    @Step("Select 'Excel' option")
    public CurrentPageT selectExcel() {
        downloadExcelOption.click();

        return self();
    }

    @Step("Read and parse transactions from CSV file: {csvFilePath}")
    public List<Transaction> parseTransactionsFromCsvFile(Path csvFilePath) throws IOException {
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(csvFilePath)) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                List<String> cells = Arrays.stream(line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"))
                        .map(s -> s.replaceAll("^\"|\"$", "").trim())
                        .toList();

                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                transactions.add(TestUtils.mapToTransaction(cells));
            }
        }

        return transactions;
    }

    @Step("Read and parse transactions from PDF text")
    public List<Transaction> parseTransactionsFromPdfText(String text) {
        List<Transaction> transactions = new ArrayList<>();

        String[] lines = text.split("\\R");
        Pattern dateTypePattern = Pattern.compile("(\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}:\\d{2})\\s+(AUTH|SALE)");
        Pattern amountLinePattern = Pattern.compile("^(\\S+) ([,0-9]*\\.\\d{2}) (\\w+) (\\w+) (\\w+)$");

        String creationDate = null;
        String type = null;
        List<String> middleLines = new ArrayList<>();

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            Matcher dtMatcher = dateTypePattern.matcher(line);
            if (dtMatcher.find()) {
                creationDate = dtMatcher.group(1);
                type = dtMatcher.group(2);
                middleLines.clear();
                continue;
            }

            Matcher amountMatcher = amountLinePattern.matcher(line);
            if (amountMatcher.find() && creationDate != null) {
                String fullMiddleText = String.join("", middleLines);
                String npgwReference;
                String businessUnitReference = "";

                if (fullMiddleText.length() > 207) {
                    npgwReference = fullMiddleText.substring(0, 207);
                    businessUnitReference = fullMiddleText.substring(207);
                } else {
                    npgwReference = fullMiddleText;
                }

                String buCode = amountMatcher.group(1);
                double amount = Double.parseDouble(amountMatcher.group(2).replaceAll(",", ""));
                String currency = amountMatcher.group(3);
                String cardType = amountMatcher.group(4);
                String status = switch (amountMatcher.group(5)) {
                    case "PARTIAL_CAPT" -> "PARTIAL_CAPTURE";
                    case "PARTIAL_REFU" -> "PARTIAL_REFUND";
                    default -> amountMatcher.group(5);
                };

                businessUnitReference = businessUnitReference + buCode;

                Transaction transaction = new Transaction(
                        creationDate,
                        Type.valueOf(type),
                        npgwReference,
                        businessUnitReference,
                        amount,
                        Currency.valueOf(currency),
                        CardType.valueOf(cardType),
                        Status.valueOf(status)
                );
                transactions.add(transaction);

                creationDate = null;
                type = null;
                middleLines.clear();
                continue;
            }

            if (creationDate != null) {
                middleLines.add(line);
            }
        }

        return transactions;
    }

    @Step("Read and parse transactions from Excel file: {filePath}")
    public List<Transaction> parseTransactionsFromExcelFile(Path filePath) throws IOException {
        List<Transaction> transactionList = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (
                FileInputStream fis = new FileInputStream(String.valueOf(filePath));
                Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                List<String> cells = new ArrayList<>();
                for (int i = 0; i <= 7; i++) {
                    cells.add(formatter.formatCellValue(row.getCell(i)));
                }

                transactionList.add(TestUtils.mapToTransaction(cells));
            }
        }

        return transactionList;
    }

    public CurrentPageT dragArrows(String from, String to) {
        dragAndDrop(getArrowsUpDown(from), getSettingsVisibleColumnCheckbox(to));

        return self();
    }

    public CurrentPageT dragArrowsToFirstPosition(String from) {
        dragAndDrop(getArrowsUpDown(from), settingsVisibleColumnCheckbox.first());

        return self();
    }

    public CurrentPageT dragArrowsToLastPosition(String from) {
        dragAndDrop(getArrowsUpDown(from), settingsVisibleColumnCheckbox.last());

        return self();
    }

    public CurrentPageT pressEscapeKey() {
        getPage().keyboard().press("Escape");

        return self();
    }

    private void uncheckIfSelected(Locator checkbox) {
        if ((boolean) checkbox.evaluate("el => el.checked")) {
            checkbox.dispatchEvent("click");
        }
    }

    private void checkIfNotSelected(Locator checkbox) {
        if (!(boolean) checkbox.evaluate("el => el.checked")) {
            checkbox.dispatchEvent("click");
        }
    }

    private void dragAndDrop(Locator source, Locator target) {
        source.dragTo(target);
    }

    private Locator getArrowsUpDown(String name) {
        return getByRole(AriaRole.BUTTON, name).locator(settingsArrowsUpDown);
    }

    private Locator getSettingsVisibleColumnCheckbox(String name) {
        return getByRole(AriaRole.BUTTON, name).locator(settingsVisibleColumnCheckbox);
    }
}
