package xyz.npgw.test.page.dialog.transactions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.transactions.SuperTransactionsPage;

@Getter
public class TransactionDetailsDialog extends BaseDialog<SuperTransactionsPage, TransactionDetailsDialog> {

    private static final String CARD_DETAILS_SECTION = "Card details";
    private static final String CUSTOMER_DETAILS_SECTION = "Customer details";

    private final Locator sectionNames = getDialog().locator(".text-primary");

    private final Locator amount = getDialog().getByText("Amount");
    private final Locator updatedOn = getDialog().getByText("Updated on");
    private final Locator npgwReference = getDialog().getByText("NPGW reference");
    private final Locator businessUnitReference = getDialog().getByText("Business unit reference");

    private final Locator paymentLifecycleTable = getByLabelExact("payment lifecycle table");
    private final Locator paymentLifecycleType = paymentLifecycleTable.getByRole(AriaRole.ROWHEADER);

    private final Locator cardDetailsRegion = getByRole(AriaRole.REGION, "Card details");
    private final Locator cardDetailsLabels = cardDetailsRegion.locator(".labelText");

    private final Locator customerDetailsRegion = getByRole(AriaRole.REGION, "Customer details");
    private final Locator customerDetailsLabels = customerDetailsRegion.locator(".labelText");

    private final Locator paymentMethodValue = parameter("Payment method", CARD_DETAILS_SECTION);
    private final Locator cardHolderValue = parameter("Card holder", CARD_DETAILS_SECTION);
    private final Locator cardNumberValue = parameter("Card number", CARD_DETAILS_SECTION);
    private final Locator expiryDateValue = parameter("Expiry date", CARD_DETAILS_SECTION);

    private final Locator nameValue = parameter("Name", CUSTOMER_DETAILS_SECTION);
    private final Locator dateOfBirthValue = parameter("Date of birth", CUSTOMER_DETAILS_SECTION);
    private final Locator emailValue = parameter("E-Mail", CUSTOMER_DETAILS_SECTION);
    private final Locator phoneValue = parameter("Phone", CUSTOMER_DETAILS_SECTION);
    private final Locator countryValue = parameter("Country", CUSTOMER_DETAILS_SECTION);
    private final Locator stateValue = parameter("State", CUSTOMER_DETAILS_SECTION);
    private final Locator cityValue = parameter("City", CUSTOMER_DETAILS_SECTION);
    private final Locator zipValue = parameter("ZIP", CUSTOMER_DETAILS_SECTION);
    private final Locator addressValue = parameter("Address", CUSTOMER_DETAILS_SECTION);

    private final Locator statusValue = getDialogHeader().locator("div > div");
    private final Locator amountValue = amount.locator("+div");
    private final Locator updatedOnValue = updatedOn.locator("+div");
    private final Locator npgwReferenceValue = npgwReference.locator("+div");
    private final Locator businessUnitReferenceValue = businessUnitReference.locator("+div");

    public TransactionDetailsDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperTransactionsPage getReturnPage() {
        return new SuperTransactionsPage(getPage());
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

    public int countPaymentLifecycleType(String text) {
        paymentLifecycleType.last().waitFor();

        return paymentLifecycleType.getByText(text).count();
    }

    public Locator getFirstPaymentLifecycleType() {
        paymentLifecycleType.last().waitFor();

        return paymentLifecycleType.first();
    }

    public Locator getLastPaymentLifecycleType() {
        paymentLifecycleType.last().waitFor();

        return paymentLifecycleType.last();
    }

    public Locator getPaymentLifecycleType(String name) {
        paymentLifecycleType.last().waitFor();

        return paymentLifecycleType.getByText(name);
    }
}
