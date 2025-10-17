package xyz.npgw.test.run;

import com.microsoft.playwright.APIRequestContext;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTestForLogout;
import xyz.npgw.test.common.client.Operation;
import xyz.npgw.test.common.client.TransactionResponse;
import xyz.npgw.test.common.entity.Acquirer;
import xyz.npgw.test.common.entity.AddMerchantAcquirerItem;
import xyz.npgw.test.common.entity.BusinessUnit;
import xyz.npgw.test.common.entity.Currency;
import xyz.npgw.test.common.entity.User;
import xyz.npgw.test.common.entity.UserRole;
import xyz.npgw.test.common.util.AuthTransactionUtils;
import xyz.npgw.test.common.util.SaleTransactionUtils;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.dashboard.SuperDashboardPage;
import xyz.npgw.test.page.dialog.transactions.TransactionDetailsDialog;
import xyz.npgw.test.page.transactions.SuperTransactionsPage;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;

public class TransactionFlowTest extends BaseTestForLogout {

    private final String company = "A transaction flow";
    private final String merchant = "The merchant";
    private APIRequestContext apiRequestContext;
    private BusinessUnit businessUnit;

    @Override
    @BeforeClass
    protected void beforeClass() {
        super.beforeClass();
        final String acquirerName = "The acquirer";
        final Acquirer acquirer = Acquirer.builder()
                .acquirerName(acquirerName)
                .acquirerDisplayName(acquirerName)
                .currencyList(new Currency[]{Currency.EUR})
                .build();
        final User admin = User.builder()
                .companyName(company)
                .userRole(UserRole.ADMIN)
                .email("the.admin@email.com")
                .build();

        TestUtils.createCompany(getApiRequestContext(), company);

        User.create(getApiRequestContext(), admin);
        User.passChallenge(getApiRequestContext(), admin.getEmail(), admin.getPassword());

        businessUnit = TestUtils.createBusinessUnit(getApiRequestContext(), company, merchant);
        String apiKey = BusinessUnit.getNewApikey(
                getApiRequestContext(getPlaywright(), admin.getCredentials()),
                company,
                businessUnit);

        apiRequestContext = getApiRequestContext(getPlaywright(), apiKey);

        TestUtils.createAcquirer(getApiRequestContext(), acquirer);
        AddMerchantAcquirerItem.create(getApiRequestContext(), businessUnit, acquirer);
    }

    @Test
    @TmsLink("1280")
    @Epic("Transactions")
    @Feature("Actions")
    @Description("Cancel an authorized transaction and verify the updated status in table and details dialog")
    public void testCancelAuthorizedTransaction() {
        String transactionId = AuthTransactionUtils
                .createAuthorisedTransaction(
                        getPlaywright(), apiRequestContext, 2000, businessUnit, "AUTHORISED1234")
                .transactionId();

        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectCompany().selectCompany(company)
                .getSelectBusinessUnit().selectBusinessUnit(merchant);

        Allure.step("Verify: transaction status in the table");
        assertThat(transactionsPage.getTable().getCellByTransactionId(transactionId, "Status"))
                .hasText("AUTHORISED");

        transactionsPage
                .getTable().clickCancelTransactionButton(transactionId)
                .clickCancelButton();

        Allure.step("Verify: Success message is shown");
        assertThat(transactionsPage.getAlert().getSuccessMessage())
                .containsText("SUCCESSTransaction was cancelled successfully");

        Allure.step("Verify: transaction status in the table is changed");
        assertThat(transactionsPage.getTable().getCellByTransactionId(transactionId, "Status"))
                .hasText("CANCELLED");

        TransactionDetailsDialog transactionDetailsDialog = transactionsPage
                .getTable().clickTransactionId(transactionId);

        Allure.step("Verify: 'Status' value is the same as in the table");
        assertThat(transactionDetailsDialog.getStatusValue()).hasText("CANCELLED");

        Allure.step("Verify: final 'Status' value is the same in lifecycle as in the table");
        assertThat(transactionDetailsDialog.getLastLifecycleStatus()).hasText("CANCELLED");
    }

    @Test
    @TmsLink("xxx")
    @Epic("Transactions")
    @Feature("Actions")
    @Description("Create a success auth type transaction by performing one capture of all authorised amount")
    public void testRefundAuthTypeTransaction() {
        TransactionResponse transactionResponse = AuthTransactionUtils
                .createSuccessByFullCaptureTransaction(
                        getPlaywright(), apiRequestContext, 3001, businessUnit, "SUCCESS");

        String transactionId = transactionResponse.transactionId();
        String transactionCurrency = String.valueOf(transactionResponse.currency());
        String transactionAmount = String.format("%.2f", transactionResponse.amount() / 100.0);
        List<Operation> operations = List.of(transactionResponse.operationList());
        Operation firstOperation = operations.get(0);
        String operationId = firstOperation.operationId();

        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectCompany().selectCompany(company)
                .getSelectBusinessUnit().selectBusinessUnit(merchant);

        Allure.step("Verify: transaction status in the table");
        assertThat(transactionsPage.getTable().getCellByTransactionId(transactionId, "Status"))
                .hasText("SUCCESS");

        TransactionDetailsDialog transactionDetailsDialog = transactionsPage
                .getTable().clickTransactionId(transactionId);

        Allure.step("Verify: 'Status' value is the same as in the table");
        assertThat(transactionDetailsDialog.getStatusValue()).hasText("SUCCESS");

        Allure.step("Verify: amount matches expected value");
        assertThat(transactionDetailsDialog.getAmountValue())
                .hasText(transactionCurrency + " " + transactionAmount);

        Allure.step("Verify: Verify latest operation is Captured");
        assertThat(transactionDetailsDialog.getLatestOperation()).hasText("Captured");

        //TODO BUG - captured value is 0.00 always
//        Allure.step("Verify: latest operation value matches expected");
//        assertEquals(transactionDetailsDialog.getLatestOperationValue(),
//                transactionCurrency + " " + transactionAmount);

        transactionDetailsDialog
                .clickRefundOperation(operationId)
                .clickRefundButton();

        Allure.step("Verify: alert message shows successful refund");
        assertThat(transactionDetailsDialog.getAlert().getMessage())
                .hasText("SUCCESSThe refund request has been sent successfully");
    }

    @Test
    @TmsLink("1307")
    @Epic("Transactions")
    @Feature("Sale type transaction with SUCCESS status")
    @Description("Created SALE type transaction with SUCCESS status is displayed correctly in the Transactions table.")
    public void testCreateSaleTypeSuccessTransaction() {
        TransactionResponse transactionResponse = SaleTransactionUtils
                .createSuccessTransaction(
                        getPlaywright(), apiRequestContext, 5001, businessUnit, "SALESUCCESS");

        String creationDate = transactionResponse.createdOn();
        OffsetDateTime dateTime = OffsetDateTime.parse(creationDate);
        String formattedCreatedDate = dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH));

        String type = String.valueOf(transactionResponse.type());
        String npgwReference = transactionResponse.transactionId();
        String businessUnitReference = transactionResponse.externalTransactionId();
        String amount = String.format("%.2f", transactionResponse.amount() / 100.0);
        String currency = String.valueOf(transactionResponse.currency());
        String cardType = String.valueOf(transactionResponse.paymentDetails().cardType());

        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectCompany().selectCompany(company)
                .getSelectBusinessUnit().selectBusinessUnit(merchant);

        Allure.step("Verify: Creation Date (GMT) matches the API response");
        assertThat(transactionsPage.getTable().getCellByTransactionId(npgwReference, "Creation Date (GMT)"))
                .hasText(formattedCreatedDate);

        Allure.step("Verify: Type matches the API response");
        assertThat(transactionsPage.getTable().getCellByTransactionId(npgwReference, "Type"))
                .hasText(type);

        Allure.step("Verify: Business unit reference matches the API response");
        assertThat(transactionsPage.getTable().getCellByTransactionId(npgwReference, "Business unit reference"))
                .hasText(businessUnitReference);

        Allure.step("Verify: Amount matches the API response");
        assertThat(transactionsPage.getTable().getCellByTransactionId(npgwReference, "Amount"))
                .hasText(amount);

        Allure.step("Verify: Currency matches the API response");
        assertThat(transactionsPage.getTable().getCellByTransactionId(npgwReference, "Currency"))
                .hasText(currency);

        Allure.step("Verify:  Card Type matches the API response");
        assertEquals(transactionsPage.getTable().getCardTypeByTransactionId(npgwReference), cardType);

        Allure.step("Verify: Status is SUCCESS");
        assertThat(transactionsPage.getTable().getCellByTransactionId(npgwReference, "Status"))
                .hasText("SUCCESS");

        Allure.step("Verify: Refund button is visible in Actions column");
        assertThat(transactionsPage.getTable()
                .getRefundButtonByTransactionId(npgwReference)).isVisible();
    }

    //TODO Add await for acquirer answer
    @Test
    @TmsLink("1308")
    @Epic("Transactions")
    @Feature("Sale type transaction with SUCCESS status")
    @Description("SALE type transaction with SUCCESS status can be refunded successfully")
    public void testRefundSaleTypeTransactionWithSuccessStatus() {
        String transactionId = SaleTransactionUtils
                .createSuccessTransaction(
                        getPlaywright(), apiRequestContext, 4001, businessUnit, "SALESUCCESS")
                .transactionId();

        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectCompany().selectCompany(company)
                .getSelectBusinessUnit().selectBusinessUnit(merchant)
                .getTable().clickRefundTransaction(transactionId)
                .clickRefundButton();

        Allure.step("Verify: Verify that transaction shows success message after refund");
        assertThat(transactionsPage.getAlert().getSuccessMessage())
                .hasText("SUCCESSRefund request for transaction was sent successfully");
    }
}
