package xyz.npgw.test.page;

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
import xyz.npgw.test.common.entity.CardType;
import xyz.npgw.test.common.entity.Currency;
import xyz.npgw.test.common.entity.Status;
import xyz.npgw.test.common.entity.Transaction;
import xyz.npgw.test.page.base.HeaderPage;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.common.trait.SelectBusinessUnitTrait;
import xyz.npgw.test.page.common.trait.SelectCompanyTrait;
import xyz.npgw.test.page.common.trait.SelectDateRangeTrait;
import xyz.npgw.test.page.common.trait.SelectStatusTrait;
import xyz.npgw.test.page.common.trait.TransactionsTableTrait;

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
public class TransactionsPage extends HeaderPage<TransactionsPage> implements TransactionsTableTrait,
        SelectDateRangeTrait<TransactionsPage>,
        SelectCompanyTrait<TransactionsPage>,
        SelectBusinessUnitTrait<TransactionsPage>,
        SelectStatusTrait<TransactionsPage>,
        AlertTrait<TransactionsPage> {

    private final Locator businessUnitSelector = getByTextExact("Business unit").locator("../../..");
    private final Locator currencySelector = getByLabelExact("Currency");
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

    public TransactionsPage(Page page) {
        super(page);
    }

    public Locator amountApplied(String amount) {
        return getByTextExact(amount);
    }

    @Step("Click Currency Selector")
    public TransactionsPage clickCurrencySelector() {
        currencySelector.click();

        return this;
    }

    @Step("Select currency {value} from dropdown menu")
    public TransactionsPage selectCurrency(String value) {
        Locator option = getByRole(AriaRole.OPTION, value);
        option.waitFor();
        option.click();
        dropdownMenuContent.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
//        getPage().waitForCondition(() -> LocalTime.now().isAfter(THREAD_LAST_ACTIVITY.get()));

        return this;
    }

    @Step("Click 'Refresh Data' button")
    public TransactionsPage clickRefreshDataButton() {
        refreshDataButton.click();

        return this;
    }

    @Step("Click 'Amount' button")
    public TransactionsPage clickAmountButton() {
        amountButton.click();

        return this;
    }

    @Step("Fill 'From' amount value")
    public TransactionsPage fillAmountFromField(String value) {
        amountFromInputField.click();
        amountFromInputField.clear();
        amountFromInputField.fill(value);
        amountFromField.press("Enter");

        return this;
    }

    @Step("Fill 'To' amount value")
    public TransactionsPage fillAmountToField(String value) {
        amountToInputField.click();
        amountToInputField.clear();
        amountToInputField.fill(value);
        amountToField.press("Enter");

        return this;
    }

    @Step("Clear 'From' amount input field ")
    public TransactionsPage clickClearAmountFromButton() {
        clearAmountFromButton.click();

        return this;
    }

    @Step("Clear 'To' amount input field ")
    public TransactionsPage clickClearAmountToButton() {
        clearAmountToButton.click();

        return this;
    }

    @Step("Click 'From' amount increase arrow ")
    public TransactionsPage clickAmountFromIncreaseArrow() {
        amountFromIncreaseArrow.click();

        return this;
    }

    @Step("Click 'From' amount decrease arrow ")
    public TransactionsPage clickAmountFromDecreaseArrow() {
        amountFromDecreaseArrow.click();

        return this;
    }

    @Step("Click 'To' amount increase arrow ")
    public TransactionsPage clickAmountToIncreaseArrow() {
        amountToIncreaseArrow.click();

        return this;
    }

    @Step("Click 'To' amount decrease arrow ")
    public TransactionsPage clickAmountToDecreaseArrow() {
        amountToDecreaseArrow.click();

        return this;
    }

    @Step("Click 'Apply' amount button")
    public TransactionsPage clickAmountApplyButton() {
        amountApplyButton.click();
        dropdownMenuContent.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));

        return this;
    }

    @Step("Click 'Clear' amount button")
    public TransactionsPage clickAmountClearButton() {
        amountClearButton.click();

        return this;
    }

    @Step("Click clear applied amount button")
    public TransactionsPage clickAmountAppliedClearButton() {
        amountAppliedClearButton.click();

        return this;
    }

    @Step("Click 'Card type' selector")
    public TransactionsPage clickCardTypeSelector() {
        cardTypeSelector.click();

        return this;
    }

    public List<String> getCardTypeOptions() {
        return cardTypeOptions.all().stream().map(Locator::innerText).toList();
    }

    @Step("Click on the Settings button")
    public TransactionsPage clickSettingsButton() {
        settingsButton.click();
        getByTextExact("Visible columns").waitFor();

        return this;
    }


    public Locator getColumns() {
        return getByRole(AriaRole.MENUITEM).getByRole(AriaRole.BUTTON);
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

    @Step("Uncheck all 'Visible columns' in Settings")
    public TransactionsPage uncheckAllCheckboxInSettings() {
        settingsVisibleColumnCheckbox
                .all()
                .forEach(this::uncheckIfSelected);

        return this;
    }

    @Step("Uncheck Visible column '{name}' in Settings")
    public TransactionsPage uncheckVisibleColumn(String name) {
        settingsVisibleColumnCheckbox
                .all()
                .stream()
                .filter(l -> name.equals(l.getAttribute("aria-label")))
                .findFirst()
                .ifPresent(this::uncheckIfSelected);

        return this;
    }

    @Step("Check all 'Visible columns' in Settings")
    public TransactionsPage checkAllCheckboxInSettings() {
        settingsVisibleColumnCheckbox
                .all()
                .forEach(this::checkIfNotSelected);

        return this;
    }

    @Step("Check visible column '{name}' in Settings")
    public TransactionsPage checkVisibleColumn(String name) {
        settingsVisibleColumnCheckbox
                .all()
                .stream()
                .filter(l -> name.equals(l.getAttribute("aria-label")))
                .findFirst()
                .ifPresent(this::checkIfNotSelected);

        return this;
    }

    @Step("Click amount 'Edit' button")
    public TransactionsPage clickAmountEditButton() {
        amountEditButton.click();

        return this;
    }

    @Step("Click 'Download' button")
    public TransactionsPage clickDownloadButton() {
        downloadButton.click();

        return this;
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
    public TransactionsPage clickResetFilterButton() {
        resetFilterButton.click();
//        getPage().waitForCondition(() -> LocalTime.now().isAfter(THREAD_LAST_ACTIVITY.get()));

        return this;
    }

    @Step("Click Search 'Trx IDs' button")
    public TransactionsPage clickSearchTrxIdsButton() {
        searchTrxIdsButton.click();

        return this;
    }

    @Step("Fill '{value}' into 'NPGW reference' field")
    public TransactionsPage fillNpgwReference(String value) {
        npgwReferenceField.fill(value);
        return this;
    }

    @Step("Click 'Trx Id' button")
    public TransactionsPage clickTrxIdAppliedButton() {
        trxIdAppliedButton.click();

        return this;
    }

    @Step("Click TrxId Clear Icon")
    public TransactionsPage clickTrxIdClearIcon() {
        trxIdClearIcon.click();

        return this;
    }

    @Step("Click TrxId Pencil Icon")
    public TransactionsPage clickTrxIdPencilIcon() {
        trxIdPencil.click();

        return this;
    }

    @Step("Click 'Npgw reference' Clear Icon")
    public TransactionsPage clickNpgwReferenceClearIcon() {
        npgwReferenceFieldClearIcon.click();

        return this;
    }

    @Step("Select Card type '{value}' from dropdown menu")
    public TransactionsPage selectCardType(String value) {
        cardTypeSelector.click();
        getByRole(AriaRole.OPTION, value).click();

        return this;
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
    public TransactionsPage clickExportTableDataToFileButton() {
        downloadButton.click();

        return this;
    }

    @Step("Select 'CSV' option")
    public TransactionsPage selectCsv() {
        downloadCsvOption.click();

        return this;
    }

    @Step("Select 'PDF' option")
    public TransactionsPage selectPdf() {
        downloadPdfOption.click();

        return this;
    }

    @Step("Select 'Excel' option")
    public TransactionsPage selectExcel() {
        downloadExcelOption.click();

        return this;
    }

    @Step("Read and parse CSV from path: {csvFilePath}")
    public List<List<String>> readCsv(Path csvFilePath) throws IOException {
        List<List<String>> rows = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(csvFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> cells = Arrays.stream(line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"))
                        .map(s -> s.replaceAll("^\"|\"$", "").trim())
                        .toList();
                rows.add(cells);
            }
        }

        return rows;
    }

    @Step("Read and parse transactions from PDF text")
    public List<Transaction> readPdf(String text) {
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

            try {
                Currency currency = Currency.valueOf(tokens[1]);
                CardType cardType = CardType.valueOf(tokens[2]);
                Status status = Status.valueOf(tokens[3]);

                Transaction transaction = new Transaction(
                        creationDate,
                        npgwReference,
                        businessUnitReference,
                        amount,
                        currency,
                        cardType,
                        status
                );

                transactions.add(transaction);
            } catch (IllegalArgumentException e) {
                // intentionally ignored - invalid enum value
            }
        }

        return transactions;
    }

    @Step("Read and parse transactions from Excel file: {filePath}")
    public List<Transaction> readExcel(String filePath) throws IOException {
        List<Transaction> transactionList = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (
                FileInputStream fis = new FileInputStream(filePath);
                Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                String creationDate = formatter.formatCellValue(row.getCell(0));
                String npqwReference = formatter.formatCellValue(row.getCell(1));
                String businessUnit = formatter.formatCellValue(row.getCell(2));
                String amountStr = formatter.formatCellValue(row.getCell(3));
                double amount = Double.parseDouble(amountStr);
                String currency = formatter.formatCellValue(row.getCell(4));
                String cardType = formatter.formatCellValue(row.getCell(5));
                String status = formatter.formatCellValue(row.getCell(6));

                transactionList.add(
                        new Transaction(
                                creationDate,
                                npqwReference,
                                businessUnit,
                                amount,
                                Currency.valueOf(currency),
                                CardType.valueOf(cardType),
                                Status.valueOf(status)
                        ));
            }
        }

        return transactionList;
    }


    public TransactionsPage dragArrows(String from, String to) {
        dragAndDrop(getArrowsUpDown(from), getSettingsVisibleColumnCheckbox(to));

        return this;
    }

    public TransactionsPage dragArrowsToFirstPosition(String from) {
        dragAndDrop(getArrowsUpDown(from), settingsVisibleColumnCheckbox.first());

        return this;
    }

    public TransactionsPage dragArrowsToLastPosition(String from) {
        dragAndDrop(getArrowsUpDown(from), settingsVisibleColumnCheckbox.last());

        return this;
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

    public TransactionsPage pressEscapeKey() {
        getPage().keyboard().press("Escape");

        return this;
    }
}
