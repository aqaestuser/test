package xyz.npgw.test.page.common.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.BaseComponent;
import xyz.npgw.test.page.system.AdminBusinessUnitsPage;
import xyz.npgw.test.page.system.AdminTeamPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AdminSystemMenuComponent extends BaseComponent {
    private final Locator teamTab = getByRole(AriaRole.TAB, "Team");
    private final Locator businessUnitsTab = getByRole(AriaRole.TAB, "Business units");

    public AdminSystemMenuComponent(Page page) {
        super(page);
    }

    @Step("Click 'Team' tab")
    public AdminTeamPage clickTeamTab() {
        clickAndCheckActive(teamTab);

        return new AdminTeamPage(getPage());
    }

    @Step("Click 'Business units' tab")
    public AdminBusinessUnitsPage clickBusinessUnitsTab() {
        clickAndCheckActive(businessUnitsTab);

        return new AdminBusinessUnitsPage(getPage());
    }

    private void clickAndCheckActive(Locator button) {
        button.click();
        assertThat(button).hasAttribute("data-selected", "true");
    }
}
