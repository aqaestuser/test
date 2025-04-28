package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.util.ResponseUtils;
import xyz.npgw.test.page.common.TableTrait;
import xyz.npgw.test.page.dialog.user.AddUserDialog;
import xyz.npgw.test.page.dialog.user.ChangeUserActivityDialog;
import xyz.npgw.test.page.dialog.user.EditUserDialog;

import java.util.NoSuchElementException;

@Log4j2
public class TeamPage extends BaseSystemPage<TeamPage> implements TableTrait {

    private final Locator dropdownOptionList = getPage().getByRole(AriaRole.OPTION);
    @Getter
    private final Locator selectCompanyField = placeholder("Search...");
    private final Locator applyFilterButton = getByTestId("ApplyFilterButtonTeamPage");
    private final Locator addUserButton = getByTestId("AddUserButtonTeamPage");

    public TeamPage(Page page) {
        super(page);
    }

    public Locator getCompanyNameInDropdownOption(String companyName) {
        return dropdownOptionList.filter(new Locator.FilterOptions().setHas(textExact(companyName)));
    }

    @Step("Select '{companyName}' company using filter")
    public TeamPage selectCompany(String companyName) {
        String lastName = "";

        selectCompanyField.fill(companyName);

        if (dropdownOptionList.all().isEmpty()) {
            throw new NoSuchElementException("Company '" + companyName + "' not found in dropdown list.");
        }

        while (getCompanyNameInDropdownOption(companyName).all().isEmpty()) {
            if (dropdownOptionList.last().innerText().equals(lastName)) {
                throw new NoSuchElementException("Company '" + companyName + "' not found in dropdown list.");
            }
            dropdownOptionList.last().scrollIntoViewIfNeeded();

            lastName = dropdownOptionList.last().innerText();
        }

//        .first() - из-за того, что компания "super" отображается в отфильтрованном списке два раза,
//        это баг(!!), правильно - один раз (или ноль).
//        На суть теста .first() не влияет и позволяет "не заметить" баг.
//
        getCompanyNameInDropdownOption(companyName).first().click();

        return this;
    }

    @Step("Click 'Add user' button")
    public AddUserDialog clickAddUserButton() {
        ResponseUtils.clickAndWaitForResponse(getPage(), addUserButton, Constants.MERCHANT_ENDPOINT);

        return new AddUserDialog(getPage());
    }

    @Step("Click 'Edit user'")
    public EditUserDialog clickEditUser(String email) {
        Locator editButton = getPage().getByRole(
                AriaRole.ROW, new Page.GetByRoleOptions().setName(email)).getByTestId("EditUserButton");
        editButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        editButton.click();

        return new EditUserDialog(getPage());
    }

    public Locator getUsernameByEmail(String email) {
        Locator row = getPage()
                .getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName(email));

        return row.locator("td").first();
    }

    public Locator getUserRoleByEmail(String email) {
        Locator row = getPage()
                .getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName(email));

        return row.locator("td").nth(1);
    }

    public Locator getUserStatusByEmail(String email) {
        Locator row = getPage()
                .getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName(email));

        return row.locator("td").nth(2);
    }

    @Step("Click 'Apply filter")
    public TeamPage clickApplyFilter() {
        applyFilterButton.click();

        return this;
    }

    public Locator getChangeUserActivityButton(String email) {
        return getPage()
                .getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName(email))
                .getByTestId("ChangeUserActivityButton")
                .locator("svg");
    }

    @Step("Click user activation button")
    public ChangeUserActivityDialog clickChangeUserActivityButton(String email) {
        Locator button = getChangeUserActivityButton(email);
        button.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        button.click();

        return new ChangeUserActivityDialog(getPage());
    }
}
