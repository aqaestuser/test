package xyz.npgw.test.page.transactions;

import com.microsoft.playwright.Download;
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
import org.testng.Assert;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.entity.Transaction;
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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
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
    private final Locator npgwReferenceFieldClearIcon = getByRole(AriaRole.BUTTON, "clear input").first();

    private final Locator businessUnitReference = getByLabelExact("Business unit reference");
    private final Locator businessUnitReferenceClear = getByRole(AriaRole.BUTTON, "clear input").last();

    private final Locator resetFilterButton = getByTestId("ResetFilterButtonTransactionsPage");
    private final Locator refreshDataButton = locator("[data-icon='arrows-rotate']");
    private final Locator settingsButton = getByTestId("SettingsButtonTransactionsPage");
    private final Locator downloadButton = getByTestId("ExportToFileuttonTransactionsPage");
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

    public BaseTransactionsPage(Page page) {
        super(page);
    }

    public Locator amountApplied(String amount) {
        return getByTextExact(amount);
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
        amountFromInputField.click();
        amountFromInputField.clear();
        amountFromInputField.fill(value);
        amountFromField.press("Enter");

        return self();
    }

    @Step("Fill 'To' amount value")
    public CurrentPageT fillAmountToField(String value) {
        amountToInputField.click();
        amountToInputField.clear();
        amountToInputField.fill(value);
        amountToField.press("Enter");

        return self();
    }

    @Step("Clear 'From' amount input field ")
    public CurrentPageT clickClearAmountFromButton() {
        clearAmountFromButton.click();

        return self();
    }

    @Step("Clear 'To' amount input field ")
    public CurrentPageT clickClearAmountToButton() {
        clearAmountToButton.click();

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

    public List<String> getCardTypeOptions() {
        return cardTypeOptions.all().stream().map(Locator::innerText).toList();
    }

    @Step("Click on the Settings button")
    public CurrentPageT clickSettingsButton() {
        settingsButton.click();
        getByTextExact("Visible columns").waitFor();

        return self();
    }


    public Locator getColumns() {
        return getByRole(AriaRole.MENUITEM).getByRole(AriaRole.BUTTON);
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

    public boolean isFileAvailableAndNotEmpty(String fileType) {
        Download download = getPage().waitForDownload(
                new Page.WaitForDownloadOptions().setTimeout(ProjectProperties.getDefaultTimeout() * 6),
                () -> getByRole(AriaRole.MENUITEM, fileType).click());

        int length = 0;
        try (InputStream inputStream = download.createReadStream()) {
            length = inputStream.readAllBytes().length;
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
        return length > 0;
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

    @Step("Click 'Trx Id' button")
    public CurrentPageT clickTrxIdAppliedButton() {
        trxIdAppliedButton.click();

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

        return self();
    }

    @Step("Select 'CSV' option")
    public CurrentPageT selectCsv() {
        downloadCsvOption.click();

        return self();
    }

    @Step("Select 'PDF' option")
    public CurrentPageT selectPdf() {
        getPage().waitForTimeout(3000); //TODO replace with adequate wait for 10000 transactions for export
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
        int i = 0;

        Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
        Pattern amountLinePattern = Pattern.compile(
                ".*\\d+\\.\\d{2} (EUR|USD|GBT) (MASTERCARD|VISA) "
                        + "(INITIATED|PENDING|SUCCESS|FAILED|CANCELLED|EXPIRED|PARTIAL_REFUND|REFUNDED)");

        while (i < lines.length && (lines[i].trim().isEmpty() || lines[i].contains("Creation Date (GMT)"))) {
            i++;
        }

        while (i < lines.length) {
            String creationDate = lines[i].trim();
            if (!datePattern.matcher(creationDate).matches()) {
                i++;
                continue;
            }
            i++;

            StringBuilder npgwReferenceBuilder = new StringBuilder();
            while (i < lines.length) {
                String line = lines[i].trim();
                if (line.isEmpty()) {
                    break;
                }
                npgwReferenceBuilder.append(line);
                i++;
                if (npgwReferenceBuilder.length() >= 101) {
                    break;
                }
            }
            String npgwReference = npgwReferenceBuilder.toString().trim();

            StringBuilder businessUnitReferenceBuilder = new StringBuilder();
            String amountLine = null;

            while (i < lines.length) {
                String line = lines[i].trim();
                if (line.isEmpty() || datePattern.matcher(line).matches()) {
                    break;
                }

                if (amountLinePattern.matcher(line).matches()) {
                    String[] parts = line.split("\\s+", 2);
                    businessUnitReferenceBuilder.append(parts[0]);
                    amountLine = parts.length > 1 ? parts[1].trim() : "";
                    i++;
                    break;
                } else {
                    businessUnitReferenceBuilder.append(line);
                    i++;
                    if (businessUnitReferenceBuilder.length() >= 50) {
                        break;
                    }
                }
            }

            String businessUnitReference = businessUnitReferenceBuilder.toString().trim();
            if (amountLine == null && i < lines.length) {
                amountLine = lines[i].trim();
                i++;
            }

            if (amountLine == null || !amountLinePattern.matcher(amountLine).matches()) {
                continue;
            }

            String[] tokens = amountLine.split("\\s+");
            if (tokens.length < 4) {
                continue;
            }

            double amount;
            try {
                amount = Double.parseDouble(tokens[0]);
            } catch (NumberFormatException e) {
                continue;
            }

            List<String> fields = List.of(
                    creationDate,
                    npgwReference,
                    businessUnitReference,
                    String.valueOf(amount),
                    tokens[1], // currency
                    tokens[2], // cardType
                    tokens[3]  // status
            );

            try {
                transactions.add(TestUtils.mapToTransaction(fields));
            } catch (IllegalArgumentException e) {
                // intentionally ignored - invalid enum value
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
                for (int i = 0; i <= 6; i++) {
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
