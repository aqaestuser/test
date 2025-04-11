package xyz.npgw.test.page.dialog;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.systemadministration.CompaniesAndBusinessUnitsPage;

public class EditCompanyDialog extends BaseDialog {

    private final Locator companyNameField = placeholder("Enter company name");
    private final Locator companyTypeField = placeholder("Enter type");
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
    private final Locator saveChangesButton = buttonByName("Save changes");

    public EditCompanyDialog(Page page) {
        super(page);
    }

    @Step("Fill company name field with the name: {companyName}")
    public EditCompanyDialog fillCompanyNameField(String companyName) {
        companyNameField.fill(companyName);

        return this;
    }

    @Step("Fill company type field with the type: {companyType}")
    public EditCompanyDialog fillCompanyTypeField(String companyType) {
        companyTypeField.fill(companyType);

        return this;
    }

    @Step("Fill company description field")
    public EditCompanyDialog fillCompanyDescriptionField(String companyDescription) {
        companyDescriptionField.fill(companyDescription);

        return this;
    }

    @Step("Fill company website field")
    public EditCompanyDialog fillCompanyWebsiteField(String companyWebsite) {
        companyWebsiteField.fill(companyWebsite);

        return this;
    }

    @Step("Fill company primary contact field")
    public EditCompanyDialog fillCompanyPrimaryContactField(String companyPrimaryContact) {
        companyPrimaryContactField.fill(companyPrimaryContact);

        return this;
    }

    @Step("Fill company email field")
    public EditCompanyDialog fillCompanyEmailField(String companyEmail) {
        companyEmailField.fill(companyEmail);

        return this;
    }

    @Step("Fill company country field")
    public EditCompanyDialog fillCompanyCountryField(String companyCountry) {
        companyCountryField.fill(companyCountry);

        return this;
    }

    @Step("Fill company state field")
    public EditCompanyDialog fillCompanyStateField(String companyState) {
        companyStateField.fill(companyState);

        return this;
    }

    @Step("Fill company ZIP field")
    public EditCompanyDialog fillCompanyZipField(String companyZip) {
        companyZipField.fill(companyZip);

        return this;
    }

    @Step("Fill company city field")
    public EditCompanyDialog fillCompanyCityField(String companyCity) {
        companyCityField.fill(companyCity);

        return this;
    }

    @Step("Fill company phone field")
    public EditCompanyDialog fillCompanyPhoneField(String companyPhone) {
        companyPhoneField.fill(companyPhone);

        return this;
    }

    @Step("Fill company mobile field")
    public EditCompanyDialog fillCompanyMobileField(String companyMobile) {
        companyMobileField.fill(companyMobile);

        return this;
    }

    @Step("Fill company fax field")
    public EditCompanyDialog fillCompanyFaxField(String companyFax) {
        companyFaxField.fill(companyFax);

        return this;
    }

    @Step("Click 'Save changes' button")
    public CompaniesAndBusinessUnitsPage clickSaveChangesButton() {
        saveChangesButton.click();

        return new CompaniesAndBusinessUnitsPage(getPage());
    }
}
