package xyz.npgw.test.page.dialog.company;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;

@SuppressWarnings("unchecked")
public abstract class CompanyDialog<CurrentDialogT extends CompanyDialog<CurrentDialogT>>
        extends BaseDialog<CompaniesAndBusinessUnitsPage, CurrentDialogT> {

    @Getter
    private final Locator companyNameField = getByPlaceholder("Enter name");
    private final Locator companyTitleField = getByPlaceholder("Enter title");
    @Getter
    private final Locator companyTypeField = getByPlaceholder("Enter type");
    private final Locator companyDescriptionField = getByPlaceholder("Enter company description");
    private final Locator companyWebsiteField = getByPlaceholder("Enter website");
    private final Locator companyPrimaryContactField = getByPlaceholder("Enter primary contact");
    private final Locator companyEmailField = getByPlaceholder("Enter email");
    private final Locator companyCountryField = getByPlaceholder("Enter country");
    private final Locator companyStateField = getByPlaceholder("Enter state");
    private final Locator companyZipField = getByPlaceholder("Enter ZIP");
    private final Locator companyCityField = getByPlaceholder("Enter city");
    private final Locator companyPhoneField = getByPlaceholder("Enter phone");
    private final Locator companyMobileField = getByPlaceholder("Enter mobile");
    private final Locator companyFaxField = getByPlaceholder("Enter fax");
    private final Locator apiActiveCheckbox = getByRole(AriaRole.CHECKBOX, "API active");
    private final Locator portalActiveCheckbox = getByRole(AriaRole.CHECKBOX, "Portal active");

    public CompanyDialog(Page page) {
        super(page);
    }

    @Override
    protected CompaniesAndBusinessUnitsPage getReturnPage() {

        return new CompaniesAndBusinessUnitsPage(getPage());
    }

    @Step("Fill company name field")
    public CurrentDialogT fillCompanyNameField(String companyName) {
        companyNameField.fill(companyName);

        return (CurrentDialogT) this;
    }

    @Step("Fill company title field")
    public CurrentDialogT fillCompanyTitleField(String companyTitle) {
        companyTitleField.fill(companyTitle);

        return (CurrentDialogT) this;
    }

    @Step("Fill company type field")
    public CurrentDialogT fillCompanyTypeField(String companyType) {
        companyTypeField.fill(companyType);

        return (CurrentDialogT) this;
    }

    @Step("Fill company description field")
    public CurrentDialogT fillCompanyDescriptionField(String companyDescription) {
        companyDescriptionField.fill(companyDescription);

        return (CurrentDialogT) this;
    }

    @Step("Fill company website field")
    public CurrentDialogT fillCompanyWebsiteField(String companyWebsite) {
        companyWebsiteField.fill(companyWebsite);

        return (CurrentDialogT) this;
    }

    @Step("Fill company primary contact field")
    public CurrentDialogT fillCompanyPrimaryContactField(String companyPrimaryContact) {
        companyPrimaryContactField.fill(companyPrimaryContact);

        return (CurrentDialogT) this;
    }

    @Step("Fill company email field")
    public CurrentDialogT fillCompanyEmailField(String companyEmail) {
        companyEmailField.fill(companyEmail);

        return (CurrentDialogT) this;
    }

    @Step("Fill company country field")
    public CurrentDialogT fillCompanyCountryField(String companyCountry) {
        companyCountryField.fill(companyCountry);

        return (CurrentDialogT) this;
    }

    @Step("Fill company state field")
    public CurrentDialogT fillCompanyStateField(String companyState) {
        companyStateField.fill(companyState);

        return (CurrentDialogT) this;
    }

    @Step("Fill company ZIP field")
    public CurrentDialogT fillCompanyZipField(String companyZip) {
        companyZipField.fill(companyZip);

        return (CurrentDialogT) this;
    }

    @Step("Fill company city field")
    public CurrentDialogT fillCompanyCityField(String companyCity) {
        companyCityField.fill(companyCity);

        return (CurrentDialogT) this;
    }

    @Step("Fill company phone field")
    public CurrentDialogT fillCompanyPhoneField(String companyPhone) {
        companyPhoneField.fill(companyPhone);

        return (CurrentDialogT) this;
    }

    @Step("Fill company mobile field")
    public CurrentDialogT fillCompanyMobileField(String companyMobile) {
        companyMobileField.fill(companyMobile);

        return (CurrentDialogT) this;
    }

    @Step("Fill company fax field")
    public CurrentDialogT fillCompanyFaxField(String companyFax) {
        companyFaxField.fill(companyFax);

        return (CurrentDialogT) this;
    }

    @Step("Set 'API active' checkbox checked state {isActive}")
    public CurrentDialogT setApiActiveCheckbox(boolean isActive) {
        if (isActive) {
            apiActiveCheckbox.check();
        } else {
            apiActiveCheckbox.uncheck();
        }

        return (CurrentDialogT) this;
    }

    @Step("Set 'Portal active' checkbox checked state {isActive}")
    public CurrentDialogT setPortalActiveCheckbox(boolean isActive) {
        if (isActive) {
            portalActiveCheckbox.check();
        } else {
            portalActiveCheckbox.uncheck();
        }

        return (CurrentDialogT) this;
    }
}
