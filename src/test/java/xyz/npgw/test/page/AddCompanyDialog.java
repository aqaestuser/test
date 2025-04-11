package xyz.npgw.test.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.base.BasePage;
import xyz.npgw.test.page.systemadministration.CompaniesAndBusinessUnitsPage;

import java.util.List;

public class AddCompanyDialog extends BasePage {

    @Getter
    private final Locator addCompanyDialogHeader = locator("section header");
    private final Locator companyNameField = placeholder("Enter company name");
    private final Locator companyTypeField = placeholder("Enter type");
    @Getter
    private final Locator createButton = buttonByName("Create");
    private final Locator alertMessage = locator("[role='alert']");
    private final Locator allFieldPlaceholders = locator("[data-slot='input']:not([placeholder='Search...'])");
    private final Locator closeButton = textExact("Close");
    private final Locator companyDescriptionField = placeholder("Enter company description");
    private final Locator companyWebsiteField = placeholder("Enter company website");
    private final Locator companyPrimaryContactField = placeholder("Enter company primary contact");
    private final Locator companyEmailField = placeholder("Enter company email");
    private final Locator companyCountryField = placeholder("Enter country");
    private final Locator companyStateField = placeholder("Enter state");
    private final Locator companyZipField = placeholder("Enter ZIP");
    private final Locator companyCityField = placeholder("Enter city");
    private final Locator companyPhoneField = placeholder("Enter phone");
    private final Locator companyMobileField = placeholder("Enter mobile");
    private final Locator companyFaxField = placeholder("Enter fax");
    private final Locator apiActiveCheckbox = checkbox("API active");
    private final Locator portalActiveCheckbox = checkbox("Portal active");

    public AddCompanyDialog(Page page) {
        super(page);
    }

    @Step("Fill company name field with the name: {companyName}")
    public AddCompanyDialog fillCompanyNameField(String companyName) {
        companyNameField.fill(companyName);

        return this;
    }

    @Step("Fill company type field with the type: {companyType}")
    public AddCompanyDialog fillCompanyTypeField(String companyType) {
        companyTypeField.fill(companyType);

        return this;
    }

    @Step("Fill company description field")
    public AddCompanyDialog fillCompanyDescriptionField(String companyDescription) {
        companyDescriptionField.fill(companyDescription);

        return this;
    }

    @Step("Fill company website field")
    public AddCompanyDialog fillCompanyWebsiteField(String companyWebsite) {
        companyWebsiteField.fill(companyWebsite);

        return this;
    }

    @Step("Fill company primary contact field")
    public AddCompanyDialog fillCompanyPrimaryContactField(String companyPrimaryContact) {
        companyPrimaryContactField.fill(companyPrimaryContact);

        return this;
    }

    @Step("Fill company email field")
    public AddCompanyDialog fillCompanyEmailField(String companyEmail) {
        companyEmailField.fill(companyEmail);

        return this;
    }

    @Step("Fill company country field")
    public AddCompanyDialog fillCompanyCountryField(String companyCountry) {
        companyCountryField.fill(companyCountry);

        return this;
    }

    @Step("Fill company state field")
    public AddCompanyDialog fillCompanyStateField(String companyState) {
        companyStateField.fill(companyState);

        return this;
    }

    @Step("Fill company ZIP field")
    public AddCompanyDialog fillCompanyZipField(String companyZip) {
        companyZipField.fill(companyZip);

        return this;
    }

    @Step("Fill company city field")
    public AddCompanyDialog fillCompanyCityField(String companyCity) {
        companyCityField.fill(companyCity);

        return this;
    }

    @Step("Fill company phone field")
    public AddCompanyDialog fillCompanyPhoneField(String companyPhone) {
        companyPhoneField.fill(companyPhone);

        return this;
    }

    @Step("Fill company mobile field")
    public AddCompanyDialog fillCompanyMobileField(String companyMobile) {
        companyMobileField.fill(companyMobile);

        return this;
    }

    @Step("Fill company fax field")
    public AddCompanyDialog fillCompanyFaxField(String companyFax) {
        companyFaxField.fill(companyFax);

        return this;
    }

    @Step("Click on the 'Create' button and trigger an error")
    public AddCompanyDialog clickCreateButtonAndTriggerError() {
        createButton.click();

        return this;
    }

    public Locator getAlertMessage() {
        alertMessage.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        return alertMessage;
    }

    public List<String> getAllFieldPlaceholders() {
        allFieldPlaceholders.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        return allFieldPlaceholders.all().stream().map(l -> l.getAttribute("placeholder")).toList();
    }

    @Step("Click 'Close' button")
    public CompaniesAndBusinessUnitsPage clickCloseButton() {
        closeButton.click();

        return new CompaniesAndBusinessUnitsPage(getPage());
    }

    @Step("Click on the 'Create' button")
    public CompaniesAndBusinessUnitsPage clickCreateButton() {
        createButton.click();

        return new CompaniesAndBusinessUnitsPage(getPage());
    }

    public AddCompanyDialog setApiActiveCheckbox(boolean isActive) {
        if (isActive) {
            apiActiveCheckbox.check();
        } else {
            apiActiveCheckbox.uncheck();
        }

        return this;
    }

    public AddCompanyDialog setPortalActiveCheckbox(boolean isActive) {
        if (isActive) {
            portalActiveCheckbox.check();
        } else {
            portalActiveCheckbox.uncheck();
        }

        return this;
    }
}
