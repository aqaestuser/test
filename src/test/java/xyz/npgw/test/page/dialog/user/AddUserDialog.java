package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import xyz.npgw.test.common.entity.User;
import xyz.npgw.test.common.entity.UserRole;
import xyz.npgw.test.page.system.TeamPage;

import static io.qameta.allure.model.Parameter.Mode.MASKED;

public class AddUserDialog extends UserDialog<AddUserDialog> {

    private final Locator emailField = getByPlaceholder("Enter user email");
    private final Locator passwordField = getByPlaceholder("Enter user password");
    private final Locator createButton = getByRole(AriaRole.BUTTON, "Create");

    public AddUserDialog(Page page) {
        super(page);
    }

    @Step("Enter user email")
    public AddUserDialog fillEmailField(String email) {
        emailField.fill(email);

        return this;
    }

    @Step("Enter user password")
    public AddUserDialog fillPasswordField(@Param(name = "Password", mode = MASKED) String password) {
        passwordField.fill(password);

        return this;
    }

    @Step("Click 'Create' button")
    public TeamPage clickCreateButton() {
        createButton.click();

        return new TeamPage(getPage());
    }

    public TeamPage createUser(User user) {
        return fillEmailField(user.email())
                .fillPasswordField(user.password())
                .setStatusRadiobutton(user.enabled())
                .setUserRoleRadiobutton(user.userRole())
                .setAllowedBusinessUnits(user.merchantIds())
                .clickCreateButton();
    }

    public TeamPage createCompanyAdmin(String email, String password) {

        return createUser(new User(
                "",
                true,
                UserRole.ADMIN,
                new String[]{},
                email,
                password));
    }
}
