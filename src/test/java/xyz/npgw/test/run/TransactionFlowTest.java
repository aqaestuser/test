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
import xyz.npgw.test.common.entity.Acquirer;
import xyz.npgw.test.common.entity.AddMerchantAcquirerItem;
import xyz.npgw.test.common.entity.BusinessUnit;
import xyz.npgw.test.common.entity.Currency;
import xyz.npgw.test.common.entity.User;
import xyz.npgw.test.common.entity.UserRole;
import xyz.npgw.test.common.util.AuthTransactionUtils;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.dashboard.SuperDashboardPage;
import xyz.npgw.test.page.dialog.transactions.TransactionDetailsDialog;
import xyz.npgw.test.page.transactions.SuperTransactionsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

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
                .email("the.user@email.com")
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

    //TODO rewrite this example to a ATC or remove it
    @Test
    @TmsLink("xxx")
    @Epic("Transactions")
    @Feature("Actions")
    @Description("Create a success auth type transaction by performing one capture of all authorised amount")
    public void testRefundAuthTypeTransaction() {
        String transactionId = AuthTransactionUtils
                .createSuccessByFullCaptureTransaction(
                        getPlaywright(), apiRequestContext, 3001, businessUnit, "")
                .transactionId();

        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectCompany().selectCompany(company)
                .getSelectBusinessUnit().selectBusinessUnit(merchant);

        Allure.step("Verify: transaction status in the table");
        assertThat(transactionsPage.getTable().getCellByTransactionId(transactionId, "Status"))
                .hasText("SUCCESS");
    }
}
