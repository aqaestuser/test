package xyz.npgw.test.page.dialog.acquirer;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.common.entity.Acquirer;
import xyz.npgw.test.common.entity.Currency;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.AcquirersPage;

@Getter
@SuppressWarnings("unchecked")
public abstract class AcquirerDialog<CurrentDialogT extends AcquirerDialog<CurrentDialogT>>
        extends BaseDialog<AcquirersPage, CurrentDialogT>
        implements AlertTrait<CurrentDialogT> {

    private final Locator acquirerNameField = getByPlaceholder("Enter entity name");
    private final Locator statusSwitch = getByRole(AriaRole.RADIOGROUP, "Status");
    private final Locator allowedCurrenciesCheckboxes = getByRole(AriaRole.RADIOGROUP, "Allowed currency");
    private final Locator challengeURLField = getByPlaceholder("Enter challenge URL");
    private final Locator fingerprintUrlField = getByPlaceholder("Enter fingerprint URL");
    private final Locator resourceUrlField = getByPlaceholder("Enter resource URL");
    private final Locator notificationQueueField = getByPlaceholder("Enter notification queue");
    private final Locator acquirerConfigField = getByPlaceholder("Enter acquirer config");
    private final Locator acquirerCodeField = getByPlaceholder("Enter acquirer code");
    private final Locator acquirerDisplayNameField = getByPlaceholder("Enter display name");
    private final Locator acquirerMidField = getByPlaceholder("Enter MID");
    private final Locator acquirerMidMccField = getByPlaceholder("Enter MCC");

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

    public Locator getAllowedCurrencyRadio(String value) {
        return allowedCurrenciesCheckboxes.locator("label:has(input[value='" + value.toUpperCase() + "']) input");
    }

    @Step("Enter acquirer name '{name}'")
    public CurrentDialogT fillAcquirerNameField(String name) {
        getDialogHeader().waitFor();
        acquirerNameField.fill(name);

        return (CurrentDialogT) this;
    }

    @Step("Enter challenge URL '{url}'")
    public CurrentDialogT fillChallengeUrlField(String url) {
        challengeURLField.fill(url);

        return (CurrentDialogT) this;
    }

    @Step("Enter fingerprint URL '{url}'")
    public CurrentDialogT fillFingerprintUrlField(String url) {
        fingerprintUrlField.fill(url);

        return (CurrentDialogT) this;
    }

    @Step("Enter resource URL '{url}'")
    public CurrentDialogT fillResourceUrlField(String url) {
        resourceUrlField.fill(url);

        return (CurrentDialogT) this;
    }

    @Step("Enter notification queue '{queue}'")
    public CurrentDialogT fillNotificationQueueField(String queue) {
        notificationQueueField.fill(queue);

        return (CurrentDialogT) this;
    }

    @Step("Click currency '{currency}'")
    public CurrentDialogT clickCheckboxCurrency(String currency) {
        getByRole(AriaRole.RADIO, currency).check();

        return (CurrentDialogT) this;
    }

    @Step("Enter acquirer config '{acquirerConfig}'")
    public CurrentDialogT fillAcquirerConfigField(String acquirerConfig) {
        acquirerConfigField.fill(acquirerConfig);

        return (CurrentDialogT) this;
    }

    @Step("Fill acquirer form")
    public CurrentDialogT fillAcquirerForm(Acquirer acquirer) {
        fillChallengeUrlField(acquirer.getSystemConfig().challengeUrl())
                .fillFingerprintUrlField(acquirer.getSystemConfig().fingerprintUrl())
                .fillResourceUrlField(acquirer.getSystemConfig().resourceUrl())
                .fillNotificationQueueField(acquirer.getSystemConfig().notificationQueue())
                .fillAcquirerConfigField(acquirer.getAcquirerConfig());

        clickStatusRadiobutton(acquirer.isActive() ? "Active" : "Inactive");

        for (Currency currency : acquirer.getCurrencyList()) {
            clickCheckboxCurrency(currency.name());
        }

        return (CurrentDialogT) this;
    }

    @Step("Enter 'Acquirer display name'")
    public CurrentDialogT fillAcquirerDisplayNameField(String displayName) {
        acquirerDisplayNameField.fill(displayName);

        return (CurrentDialogT) this;
    }

    @Step("Enter 'Acquirer MID'")
    public CurrentDialogT fillAcquirerMidField(String acquirerMid) {
        acquirerMidField.fill(acquirerMid);

        return (CurrentDialogT) this;
    }

    @Step("Enter 'Acquirer MID MCC'")
    public CurrentDialogT fillAcquirerMidMccField(String acquirerMid) {
        acquirerMidMccField.fill(acquirerMid);

        return (CurrentDialogT) this;
    }
}
