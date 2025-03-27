package xyz.npgw.test.tests;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.page.TransactionsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TransactionsPageTest extends BaseTest {

    @BeforeMethod
    public void beforeMethod() {
        new TransactionsPage(getPage())
                .signIn(Constants.USER_EMAIL, Constants.USER_PASSWORD, false)
                .getHeader()
                .clickTransactionsLink();;
    }

    @Test
    @TmsLink("108")
    @Epic("Transactions")
    @Feature("Navigation")
    @Description("User navigate to 'Transactions page' after clicking on 'Transactions' menu on the header")
    public void testNavigateToTransactionsPage() {
        TransactionsPage transaction = new TransactionsPage(getPage());

        Allure.step("Verify: Transactions Page URL");
        assertThat(transaction.getPage()).hasURL(Constants.TRANSACTIONS_PAGE_URL);

        Allure.step("Verify: Transactions Page Title");
        assertThat(transaction.getPage()).hasTitle(Constants.TRANSACTIONS_URL_TITLE);
    }
}
