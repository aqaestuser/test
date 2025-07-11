package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.TransactionsPage;
import xyz.npgw.test.page.dialog.transactions.TransactionDetailsDialog;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static xyz.npgw.test.common.Constants.BUSINESS_UNIT_FOR_TEST_RUN;
import static xyz.npgw.test.common.Constants.COMPANY_NAME_FOR_TEST_RUN;

public class TransactionDetailsTest extends BaseTest {

    @Test
    @TmsLink("638")
    @Epic("Transactions")
    @Feature("Transaction details")
    @Description("Check that after click on transactions in column NPGW reference user see transaction details")
    public void testCheckTransactionDetails() {
        TransactionDetailsDialog transactionDetailsDialog = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN)
                .getTable().clickOnFirstTransaction();

        Allure.step("Verify: The dialog box header has text 'Transaction details'");
        assertThat(transactionDetailsDialog.getDialogHeader()).containsText("Transaction details");

        Allure.step("Verify: The dialog box section names");
        assertThat(transactionDetailsDialog.getSectionNames())
                .hasText(new String[]{"Amount", "Updated on (GMT)", "NPGW reference", "Business unit reference",
                        "Payment lifecycle", "Card details", "Customer details", "3D Secure"});

        Allure.step("Verify: The Card details labels");
        assertThat(transactionDetailsDialog.getCardDetailsLabels())
                .hasText(new String[]{"Payment method", "Card type", "Card holder", "Card number", "Expiry date"});

        Allure.step("Verify: The Customer details labels");
        assertThat(transactionDetailsDialog.getCustomerDetailsLabels())
                .hasText(new String[]{"Name", "Date of birth", "E-Mail", "Phone",
                        "Country", "State", "City", "ZIP", "Address"});
    }


    @Test
    @TmsLink("661")
    @Epic("Transactions")
    @Feature("Transaction details")
    @Description("Check the hiding of parameters by pressing the chevron in Card details section")
    public void testCheckTheHidingOfParameters() {
        TransactionDetailsDialog transactionDetailsDialog = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN)
                .getTable().clickOnFirstTransaction()
                .clickSection("Card details");

        Allure.step("Verify: Parameter 'Payment method' is hidden after click on chevron in Card details field ");
        assertThat(transactionDetailsDialog.getPaymentMethodValue()).isHidden();

        Allure.step("Verify: Parameter 'Card holder' is hidden after click on chevron in Card details field ");
        assertThat(transactionDetailsDialog.getCardHolderValue()).isHidden();

        Allure.step("Verify: Parameter 'Card number' is hidden after click on chevron in Card details field ");
        assertThat(transactionDetailsDialog.getCardNumberValue()).isHidden();

        Allure.step("Verify: Parameter 'Expiry date' is hidden after click on chevron in Card details field ");
        assertThat(transactionDetailsDialog.getExpiryDateValue()).isHidden();

        transactionDetailsDialog
                .clickSection("Customer details");

        Allure.step("Verify: Parameter 'Name' is hidden after click on chevron in Customer details field ");
        assertThat(transactionDetailsDialog.getNameValue()).isHidden();

        Allure.step("Verify: Parameter 'Date of birth' is hidden after click on chevron in Customer details field ");
        assertThat(transactionDetailsDialog.getDateOfBirthValue()).isHidden();

        Allure.step("Verify: Parameter 'E-Mail' is hidden after click on chevron in Customer details field ");
        assertThat(transactionDetailsDialog.getEmailValue()).isHidden();

        Allure.step("Verify: Parameter 'Phone' is hidden after click on chevron in Customer details field ");
        assertThat(transactionDetailsDialog.getPhoneValue()).isHidden();

        Allure.step("Verify: Parameter 'Country' is hidden after click on chevron in Customer details field ");
        assertThat(transactionDetailsDialog.getCountryValue()).isHidden();

        Allure.step("Verify: Parameter 'State' is hidden after click on chevron in Customer details field ");
        assertThat(transactionDetailsDialog.getStateValue()).isHidden();

        Allure.step("Verify: Parameter 'City' is hidden after click on chevron in Customer details field ");
        assertThat(transactionDetailsDialog.getCityValue()).isHidden();

        Allure.step("Verify: Parameter 'ZIP' is hidden after click on chevron in Customer details field ");
        assertThat(transactionDetailsDialog.getZipValue()).isHidden();

        Allure.step("Verify: Parameter 'Address' is hidden after click on chevron in Customer details field ");
        assertThat(transactionDetailsDialog.getAddressValue()).isHidden();
    }

    @Test
    @TmsLink("738")
    @Epic("Transactions")
    @Feature("Transaction details")
    @Description("Closes the transaction details dialog using both the button and the icon.")
    public void testCloseTransactionDetailsDialog() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN)
                .getTable().clickOnFirstTransaction()
                .clickCloseButton();

        Allure.step("Verify: Transaction details dialog is closed ");
        assertThat(transactionsPage.getDialog()).not().isAttached();

        transactionsPage
                .getTable().clickOnFirstTransaction()
                .clickCloseIcon();

        Allure.step("Verify: Transaction details dialog is closed ");
        assertThat(transactionsPage.getDialog()).not().isAttached();
    }

    @Test
    @TmsLink("749")
    @Epic("Transactions")
    @Feature("Transaction details")
    @Description("Verify, that the data in Transaction Details Dialog corresponds to the data in Transactions table")
    public void testDataMatching() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN);

        String currency = transactionsPage
                .getTable().getFirstRowCell("Currency").textContent();

        String status = transactionsPage
                .getTable().getFirstRowCell("Status").textContent();

        String amount = transactionsPage
                .getTable().getFirstRowCell("Amount").textContent();

        String businessUnitReference = transactionsPage
                .getTable().getFirstRowCell("Business unit reference").textContent();

        String cardType = transactionsPage
                .getTable().getFirstRowCardType();

        TransactionDetailsDialog transactionDetails = transactionsPage
                .getTable().clickOnFirstTransaction();

        Allure.step("Verify: 'Status' value is the same as in the table");
        assertThat(transactionDetails.getStatusValue()).hasText(status);

        Allure.step("Verify: Business unit reference is the same as in the table");
        assertThat(transactionDetails.getBusinessUnitReferenceValue()).hasText(businessUnitReference);

        Allure.step("Verify: 'Amount' value and Currency are the same as in the table");
        assertThat(transactionDetails.getAmountValue()).hasText(currency + " " + amount);

        Allure.step("Verify: 'Card type' is the same as in table");
        assertThat(transactionDetails.getCardTypeValue()).hasText(cardType);
    }

    @Test
    @TmsLink("828")
    @Epic("Transactions")
    @Feature("Transaction details")
    @Description("Check that the 'Pending' occurs at most once in the Payment lifecycle section")
    public void testPendingOccursAtMostOnceInLifecycle() {
        new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(TestUtils.lastBuildDate(getApiRequestContext()))
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN)
                .getTable().selectRowsPerPageOption("10")
                .getTable().goToLastPage();

        TransactionsPage transactionsPage = new TransactionsPage(getPage());
        int numberOfTransactions = transactionsPage
                .getTable().getRows()
                .count();

        for (int i = 0; i < numberOfTransactions; i++) {
            TransactionDetailsDialog transactionDetails = transactionsPage
                    .getTable().clickOnTransaction(i);

            String statusInHeader = transactionDetails.getStatusValue().innerText();
            String lastTypeInLifecycle = transactionDetails.getLastPaymentLifecycleType().innerText();

            Allure.step("Verify: Statuses in the dialog header and lifecycle are the same");
            assertEquals(statusInHeader, lastTypeInLifecycle, "Statuses should match!");

            Allure.step("Verify: The Payment lifecycle begins with 'INITIATED'");
            assertThat(transactionDetails.getFirstPaymentLifecycleType()).hasText("INITIATED");

            Allure.step("Verify: The 'INITIATED' occurs exactly once in the lifecycle");
            assertThat(transactionDetails.getPaymentLifecycleType("INITIATED")).hasCount(1);

            Allure.step("Verify: the 'PENDING' occurs at most once");
            assertTrue(transactionDetails.countPaymentLifecycleType("PENDING") <= 1, String.format(
                    "The 'PENDING' occurs several times in the transaction details (#%d on the page)", i + 1));

            transactionDetails.clickCloseIcon();
        }
    }
}
