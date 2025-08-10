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
import xyz.npgw.test.page.base.HeaderPage;
import xyz.npgw.test.page.component.header.SuperHeaderMenuTrait;
import xyz.npgw.test.page.component.select.SelectAcquirerTrait;
import xyz.npgw.test.page.component.select.SelectStatusTrait;
import xyz.npgw.test.page.component.system.SuperSystemMenuTrait;
import xyz.npgw.test.page.component.table.AcquirersTableTrait;
import xyz.npgw.test.page.dialog.acquirer.DeleteAcquirerDialog;
import xyz.npgw.test.page.dialog.acquirer.SetupAcquirerMidDialog;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Log4j2
@Getter
public class SuperAcquirersPage extends HeaderPage<SuperAcquirersPage>
        implements SuperHeaderMenuTrait<SuperAcquirersPage>,
                   SuperSystemMenuTrait,
                   SelectAcquirerTrait<SuperAcquirersPage>,
                   SelectStatusTrait<SuperAcquirersPage>,
                   AcquirersTableTrait {

    private final Locator setupAcquirerMidButton = getByTestId("AddAcquirerButton");
    private final Locator setupAcquirerMidDialog = getByRole(AriaRole.DIALOG);
    private final Locator editAcquirerMidDialog = getByRole(AriaRole.DIALOG);
    private final Locator resetFilterButton = getByTestId("ResetFilterButtonAcquirersPage");
    private final Locator refreshDataButton = getByTestId("ApplyFilterButtonAcquirersPage");
    private final Locator deleteAcquirerMidButton = getByTestId("DeleteAcquirerButton");

    public SuperAcquirersPage(Page page) {
        super(page);
    }

    @Step("Click 'Setup acquirer MID' button")
    public SetupAcquirerMidDialog clickSetupAcquirerMidButton() {
        setupAcquirerMidButton.click();

        return new SetupAcquirerMidDialog(getPage());
    }

    @Step("Click 'Reset filter' button")
    public SuperAcquirersPage clickResetFilterButton() {
        resetFilterButton.click();

        return this;
    }

    @Step("Click 'Delete acquirer MID' button")
    public DeleteAcquirerDialog clickDeleteAcquirerMidButton() {
        deleteAcquirerMidButton.click();

        return new DeleteAcquirerDialog(getPage());
    }

    @SneakyThrows
    public SuperAcquirersPage waitForAcquirerPresence(APIRequestContext request, String acquirerName) {
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
    public SuperAcquirersPage waitForAcquirerAbsence(APIRequestContext request, String acquirerName) {
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
