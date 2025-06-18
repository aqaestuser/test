package xyz.npgw.test.page.dialog;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.TransactionsPage;

@Getter
public class TransactionDetailsDialog extends BaseDialog<TransactionsPage, TransactionDetailsDialog> {

    private final Locator statusField = getDialog().getByText("Status");
    private final Locator amountField = getDialog().getByText("Amount");
    private final Locator merchantReferenceField = getDialog().getByText("Merchant reference");
    private final Locator cardDetailsSection = locator("div[aria-label='Card details']");
    private final Locator paymentMethodParameter = parameter("Payment method", "Card details");
    private final Locator cardTypeParameter = parameter("Card type", "Card details");
    private final Locator cardHolderParameter = parameter("Card holder", "Card details");
    private final Locator cardNumberParameter = parameter("Card number", "Card details");
    private final Locator expiryDateParameter = parameter("Expiry date", "Card details");
    private final Locator statusValue = statusField.locator("+div");
    private final Locator amountValue = amountField.locator("+div");
    private final Locator merchantReferenceValue = merchantReferenceField.locator("+div");

    public TransactionDetailsDialog(Page page) {
        super(page);
    }

    @Override
    protected TransactionsPage getReturnPage() {
        return new TransactionsPage(getPage());
    }

    @Step("Click on chevron in Card details section")
    public TransactionDetailsDialog clickChevronInSection(String sectionName) {
        Locator section = locator("div[aria-label='" + sectionName + "']");
        Locator chevron = section.locator("svg[role='presentation']");
        chevron.click();
        return new TransactionDetailsDialog(getPage());
    }

    private Locator parameter(String parameter, String sectionName) {
        Locator section = locator("div[aria-label='" + sectionName + "']");

        return section.locator("//div[.='" + parameter + "']/following-sibling::div[1]");
    }
}
