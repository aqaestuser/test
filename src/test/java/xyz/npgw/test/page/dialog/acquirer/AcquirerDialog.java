package xyz.npgw.test.page.dialog.acquirer;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.common.entity.Acquirer;
import xyz.npgw.test.page.common.AlertTrait;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.AcquirersPage;

import java.util.Arrays;
import java.util.Map;

@Getter
@SuppressWarnings("unchecked")
public abstract class AcquirerDialog<CurrentDialogT extends AcquirerDialog<CurrentDialogT>>
        extends BaseDialog<AcquirersPage, CurrentDialogT>
        implements AlertTrait<CurrentDialogT> {

    private final Locator acquirerNamePlaceholder = getByPlaceholder("Enter acquirer name");
    private final Locator statusSwitch = locator("div[role='radiogroup']");
    private final Locator allowedCurrenciesCheckboxes = locator("div[role='group']");

    public AcquirerDialog(Page page) {
        super(page);
    }

    @Override
    protected AcquirersPage getReturnPage() {

        return new AcquirersPage(getPage());
    }

    @Step("Click on the '{option}' radiobutton")
    public CurrentDialogT clickStatusRadiobutton(String option) {
        getByLabelExact(option).click();

        return (CurrentDialogT) this;
    }

    public Locator getStatusRadiobutton(String value) {
        return statusSwitch.locator("label:has(input[value='" + value.toUpperCase() + "'])");
    }

    @Step("Enter acquirer name '{name}'")
    public CurrentDialogT fillAcquirerName(String name) {
        getDialogHeader().waitFor();
        getByPlaceholder("Enter acquirer name").fill(name);

        return (CurrentDialogT) this;
    }

    @Step("Enter challenge URL '{url}'")
    public CurrentDialogT fillChallengeUrl(String url) {
        getByPlaceholder("Enter challenge URL").fill(url);

        return (CurrentDialogT) this;
    }

    @Step("Enter fingerprint URL '{url}'")
    public CurrentDialogT fillFingerprintUrl(String url) {
        getByPlaceholder("Enter fingerprint URL").fill(url);

        return (CurrentDialogT) this;
    }

    @Step("Enter resource URL '{url}'")
    public CurrentDialogT fillResourceUrl(String url) {
        getByPlaceholder("Enter resource URL").fill(url);

        return (CurrentDialogT) this;
    }

    @Step("Enter notification queue '{queue}'")
    public CurrentDialogT fillNotificationQueue(String queue) {
        getByPlaceholder("Enter notification queue").fill(queue);

        return (CurrentDialogT) this;
    }

    @Step("Click currency '{currency}'")
    public CurrentDialogT clickCheckboxCurrency(String currency) {
        getPage().getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName(currency)).check();

        return (CurrentDialogT) this;
    }

    @Step("Click acquirer config '{acquirerConfig}'")
    public CurrentDialogT fillAcquirerConfig(String acquirerConfig) {
        getByPlaceholder("Enter acquirer config").fill(acquirerConfig);

        return (CurrentDialogT) this;
    }

    @Step("Fill acquirer form")
    public CurrentDialogT fillAcquirerForm(Acquirer acquirer) {
        fillChallengeUrl(acquirer.systemConfig().challengeUrl())
                .fillFingerprintUrl(acquirer.systemConfig().fingerprintUrl())
                .fillResourceUrl(acquirer.systemConfig().resourceUrl())
                .fillNotificationQueue(acquirer.systemConfig().notificationQueue())
                .fillAcquirerConfig(acquirer.acquirerConfig());

        clickStatusRadiobutton(acquirer.isActive() ? "Active" : "Inactive");

        for (String currency : acquirer.currencyList()) {
            clickCheckboxCurrency(currency);
        }

        return (CurrentDialogT) this;
    }
}
