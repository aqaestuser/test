package xyz.npgw.test.page.dialog;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;

public abstract class CompanyDialog extends BaseDialog {

    @Getter
    protected final Locator companyNameField = placeholder("Enter company name");
    @Getter
    protected final Locator companyTypeField = placeholder("Enter type");
    protected final Locator companyDescriptionField = placeholder("Enter company description");
    protected final Locator companyWebsiteField = placeholder("Enter company website");
    protected final Locator companyPrimaryContactField = placeholder("Enter company primary contact");
    protected final Locator companyEmailField = placeholder("Enter company email");
    protected final Locator companyCountryField = placeholder("Enter country");
    protected final Locator companyStateField = placeholder("Enter state");
    protected final Locator companyZipField = placeholder("Enter ZIP");
    protected final Locator companyCityField = placeholder("Enter city");
    protected final Locator companyPhoneField = placeholder("Enter phone");
    protected final Locator companyMobileField = placeholder("Enter mobile");
    protected final Locator companyFaxField = placeholder("Enter fax");
    protected final Locator apiActiveCheckbox = checkbox("API active");
    protected final Locator portalActiveCheckbox = checkbox("Portal active");

    public CompanyDialog(Page page) {
        super(page);
    }
}
