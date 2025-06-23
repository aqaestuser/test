package xyz.npgw.test.page.dialog;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.TransactionsPage;

@Getter
public class TransactionDetailsDialog extends BaseDialog<TransactionsPage, TransactionDetailsDialog> {

//    private final Locator transactionDialogHeader = getDialogHeader().locator("div:first-child");

    private final Locator sectionNames = getByRole(AriaRole.DIALOG).locator(".text-primary");

    private final Locator amount = getDialog().getByText("Amount");
    private final Locator updatedOn = getDialog().getByText("Updated on");
    private final Locator npgwReference = getDialog().getByText("NPGW reference");
    private final Locator merchantReference = getDialog().getByText("Merchant reference");

    private final Locator paymentLifecycleRegion = getByRole(AriaRole.REGION, "Payment lifecycle");

    private final Locator cardDetailsRegion = getByRole(AriaRole.REGION, "Card details");
    private final Locator cardDetailsLabels = cardDetailsRegion.locator(".labelText");

    private final Locator customerDetailsRegion = getByRole(AriaRole.REGION, "Customer details");
    private final Locator customerDetailsLabels = customerDetailsRegion.locator(".labelText");

    private final Locator paymentMethodValue = parameter("Payment method", "Card details");
    private final Locator cardHolderValue = parameter("Card holder", "Card details");
    private final Locator cardNumberValue = parameter("Card number", "Card details");
    private final Locator expiryDateValue = parameter("Expiry date", "Card details");

    private final Locator nameValue = parameter("Name", "Customer details");
    private final Locator dateOfBirthValue = parameter("Date of birth", "Customer details");
    private final Locator emailValue = parameter("E-Mail", "Customer details");
    private final Locator phoneValue = parameter("Phone", "Customer details");
    private final Locator countryValue = parameter("Country", "Customer details");
    private final Locator stateValue = parameter("State", "Customer details");
    private final Locator cityValue = parameter("City", "Customer details");
    private final Locator zipValue = parameter("ZIP", "Customer details");
    private final Locator addressValue = parameter("Address", "Customer details");

    private final Locator statusValue = getDialogHeader().locator("div > div");
    private final Locator amountValue = amount.locator("+div");
    private final Locator updatedOnValue = updatedOn.locator("+div");
    private final Locator npgwReferenceValue = npgwReference.locator("+div");
    private final Locator merchantReferenceValue = merchantReference.locator("+div");

    public TransactionDetailsDialog(Page page) {
        super(page);
    }

    @Override
    protected TransactionsPage getReturnPage() {
        return new TransactionsPage(getPage());
    }

    @Step("Click on chevron in Card details section")
    public TransactionDetailsDialog clickSection(String sectionName) {
        getByRole(AriaRole.BUTTON, sectionName).click();

        return new TransactionDetailsDialog(getPage());
    }

    public Locator getCardTypeValue() {
        parameter("Card type", "Card details").getByRole(AriaRole.IMG).hover();

        return locator("[data-slot='content'].text-small");
    }

    private Locator parameter(String parameter, String sectionName) {
        Locator section = locator("div[aria-label='" + sectionName + "']");

        return section.locator("//div[.='" + parameter + "']/following-sibling::div[1]");
    }
}
