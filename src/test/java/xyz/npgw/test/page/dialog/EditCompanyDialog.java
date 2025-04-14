package xyz.npgw.test.page.dialog;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;

public class EditCompanyDialog extends CompanyDialog {

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
