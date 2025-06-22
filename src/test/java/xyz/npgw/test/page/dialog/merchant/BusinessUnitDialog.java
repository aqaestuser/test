package xyz.npgw.test.page.dialog.merchant;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;

@Getter
@SuppressWarnings("unchecked")
public abstract class BusinessUnitDialog<CurrentDialogT extends BusinessUnitDialog<CurrentDialogT>>
        extends BaseDialog<CompaniesAndBusinessUnitsPage, CurrentDialogT> {

    private final Locator companyNameField = getByLabelExact("Company name");
    private final Locator businessUnitNameField = getByPlaceholder("Enter business unit name");

    public BusinessUnitDialog(Page page) {
        super(page);
    }

    @Override
    protected CompaniesAndBusinessUnitsPage getReturnPage() {
        return new CompaniesAndBusinessUnitsPage(getPage());
    }

    @Step("Fill in merchant name: {merchantName}")
    public CurrentDialogT fillBusinessUnitNameField(String merchantName) {
        businessUnitNameField.fill(merchantName);

        return (CurrentDialogT) this;
    }
}
