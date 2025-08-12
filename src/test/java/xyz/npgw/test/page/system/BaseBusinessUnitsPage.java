package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.page.base.HeaderPage;

@Log4j2
@Getter
public abstract class BaseBusinessUnitsPage<CurrentPageT extends HeaderPage<CurrentPageT>>
        extends HeaderPage<CurrentPageT> {

    private final Locator settings = getByTestId("SettingsButtonMerchantsPage");
    private final Locator viewDocumentation = getByTestId("ViewDocumentationButtonMerchantsPage");

    private final Locator pageContent = locator("div.contentBlock");

    private final Locator companyInfoBlock = locator("//div[text()='Company info']/..");
    private final Locator name = getByLabelExact("Name");
    private final Locator type = getByLabelExact("Type");
    private final Locator description = getByLabelExact("Description");
    private final Locator website = getByLabelExact("Website");
    private final Locator primaryContact = getByLabelExact("Primary contact");
    private final Locator email = getByLabelExact("Email");
    private final Locator apiActive = getByLabelExact("API active");
    private final Locator portalActive = getByLabelExact("Portal active");
    private final Locator phone = getByLabelExact("Phone");
    private final Locator mobile = getByLabelExact("Mobile");
    private final Locator fax = getByLabelExact("Fax");
    private final Locator country = getByLabelExact("Country");
    private final Locator state = getByLabelExact("State");
    private final Locator zip = getByLabelExact("ZIP");
    private final Locator city = getByLabelExact("City");

    private final Locator addCompanyDialog = getByRole(AriaRole.DIALOG);
    private final Locator editBusinessUnitDialog = getByRole(AriaRole.DIALOG).getByTitle("Edit business unit");
    private final Locator merchantsTable = getByLabelExact("merchants table");
    private final Locator showRadiobutton = locator("[value='show']");
    private final Locator hideRadiobutton = locator("[value='hide']");

    public BaseBusinessUnitsPage(Page page) {
        super(page);
    }

    @Step("Click 'Settings'")
    public CurrentPageT clickSettings() {
        settings.click();

        return self();
    }

    @Step("Check 'Show' Company info option")
    public CurrentPageT checkShowCompanyInfo() {
        showRadiobutton.check();

        return self();
    }

    @Step("Check 'Hide' Company info option")
    public CurrentPageT checkHideCompanyInfo() {
        hideRadiobutton.check();

        return self();
    }
}
