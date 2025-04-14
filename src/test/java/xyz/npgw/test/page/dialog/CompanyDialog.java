package xyz.npgw.test.page.dialog;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.Getter;

public abstract class CompanyDialog<T extends CompanyDialog<T>> extends BaseDialog {

    @Getter
    private final Locator companyNameField = placeholder("Enter company name");
    @Getter
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
    private final Locator apiActiveCheckbox = checkbox("API active");
    private final Locator portalActiveCheckbox = checkbox("Portal active");

    public CompanyDialog(Page page) {
        super(page);
    }

    @Step("Fill company name field")
    public T fillCompanyNameField(String companyName) {
        companyNameField.fill(companyName);

        return (T) this;
    }

    @Step("Fill company type field")
    public T fillCompanyTypeField(String companyType) {
        companyTypeField.fill(companyType);

        return (T) this;
    }

    @Step("Fill company description field")
    public T fillCompanyDescriptionField(String companyDescription) {
        companyDescriptionField.fill(companyDescription);

        return (T) this;
    }

    @Step("Fill company website field")
    public T fillCompanyWebsiteField(String companyWebsite) {
        companyWebsiteField.fill(companyWebsite);

        return (T) this;
    }

    @Step("Fill company primary contact field")
    public T fillCompanyPrimaryContactField(String companyPrimaryContact) {
        companyPrimaryContactField.fill(companyPrimaryContact);

        return (T) this;
    }

    @Step("Fill company email field")
    public T fillCompanyEmailField(String companyEmail) {
        companyEmailField.fill(companyEmail);

        return (T) this;
    }

    @Step("Fill company country field")
    public T fillCompanyCountryField(String companyCountry) {
        companyCountryField.fill(companyCountry);

        return (T) this;
    }

    @Step("Fill company state field")
    public T fillCompanyStateField(String companyState) {
        companyStateField.fill(companyState);

        return (T) this;
    }

    @Step("Fill company ZIP field")
    public T fillCompanyZipField(String companyZip) {
        companyZipField.fill(companyZip);

        return (T) this;
    }

    @Step("Fill company city field")
    public T fillCompanyCityField(String companyCity) {
        companyCityField.fill(companyCity);

        return (T) this;
    }

    @Step("Fill company phone field")
    public T fillCompanyPhoneField(String companyPhone) {
        companyPhoneField.fill(companyPhone);

        return (T) this;
    }

    @Step("Fill company mobile field")
    public T fillCompanyMobileField(String companyMobile) {
        companyMobileField.fill(companyMobile);

        return (T) this;
    }

    @Step("Fill company fax field")
    public T fillCompanyFaxField(String companyFax) {
        companyFaxField.fill(companyFax);

        return (T) this;
    }

    @Step("Set 'API active' checkbox checked state {isActive}")
    public T setApiActiveCheckbox(boolean isActive) {
        if (isActive) {
            apiActiveCheckbox.check();
        } else {
            apiActiveCheckbox.uncheck();
        }

        return (T) this;
    }

    @Step("Set 'Portal active' checkbox checked state {isActive}")
    public T setPortalActiveCheckbox(boolean isActive) {
        if (isActive) {
            portalActiveCheckbox.check();
        } else {
            portalActiveCheckbox.uncheck();
        }

        return (T) this;
    }
}
