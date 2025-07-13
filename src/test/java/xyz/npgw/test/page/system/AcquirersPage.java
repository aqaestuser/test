package xyz.npgw.test.page.system;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.entity.Acquirer;
import xyz.npgw.test.common.entity.User;
import xyz.npgw.test.page.common.trait.AcquirersTableTrait;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.common.trait.SelectAcquirerTrait;
import xyz.npgw.test.page.common.trait.SelectStatusTrait;
import xyz.npgw.test.page.dialog.acquirer.AddAcquirerDialog;
import xyz.npgw.test.page.dialog.acquirer.DeleteAcquirerDialog;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Log4j2
@Getter
public class AcquirersPage extends BaseSystemPage<AcquirersPage> implements AcquirersTableTrait,
        SelectAcquirerTrait<AcquirersPage>,
        AlertTrait<AcquirersPage>,
        SelectStatusTrait<AcquirersPage> {

    private final Locator addAcquirerButton = getByTestId("AddAcquirerButton");
    private final Locator addAcquirerDialog = getByRole(AriaRole.DIALOG);
    private final Locator editAcquirerDialog = getByRole(AriaRole.DIALOG);
    private final Locator resetFilterButton = getByTestId("ResetFilterButtonAcquirersPage");
    private final Locator refreshDataButton = getByTestId("ApplyFilterButtonAcquirersPage");
    private final Locator deleteAcquirerButton = getByTestId("DeleteAcquirerButton");

    public AcquirersPage(Page page) {
        super(page);
    }

    @Step("Click Add Acquirer")
    public AddAcquirerDialog clickAddAcquirer() {
        addAcquirerButton.click();

        return new AddAcquirerDialog(getPage());
    }

    @Step("Click 'Reset filter' button")
    public AcquirersPage clickResetFilterButton() {
        resetFilterButton.click();

        return this;
    }

    @Step("Click 'Delete acquirer' button")
    public DeleteAcquirerDialog clickDeleteAcquirer() {
        deleteAcquirerButton.click();

        return new DeleteAcquirerDialog(getPage());
    }

    @SneakyThrows
    public AcquirersPage waitForAcquirerPresence(APIRequestContext request, String acquirerName) {
        double timeout = ProjectProperties.getDefaultTimeout();
        while (Arrays.stream(Acquirer.getAll(request)).noneMatch(item -> item.getAcquirerName().equals(acquirerName))) {
            TimeUnit.MILLISECONDS.sleep(300);
            timeout -= 300;
            if (timeout <= 0) {
                throw new TimeoutError("Waiting for acquirer '%s' presence".formatted(acquirerName));
            }
        }
        log.info("Acquirer presence wait took {}ms", ProjectProperties.getDefaultTimeout() - timeout);
        refreshDataButton.click();

        return this;
    }

    @SneakyThrows
    public AcquirersPage waitForAcquirerAbsence(APIRequestContext request, String acquirerName) {
        double timeout = ProjectProperties.getDefaultTimeout();
        while (Arrays.stream(Acquirer.getAll(request)).anyMatch(item -> item.getAcquirerName().equals(acquirerName))) {
            TimeUnit.MILLISECONDS.sleep(300);
            timeout -= 300;
            if (timeout <= 0) {
                throw new TimeoutError("Waiting for acquirer '%s' absence".formatted(acquirerName));
            }
        }
        log.info("Acquirer absence wait took {}ms", ProjectProperties.getDefaultTimeout() - timeout);
        refreshDataButton.click();

        return this;
    }
}
