package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.HeaderPage;
import xyz.npgw.test.page.common.header.AdminHeaderMenuTrait;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public abstract class AdminSystemPage<CurrentPageT extends AdminSystemPage<CurrentPageT>>
        extends HeaderPage<CurrentPageT>
        implements AdminHeaderMenuTrait<CurrentPageT> {

    private final Locator teamTab = getByRole(AriaRole.TAB, "Team");
    private final Locator businessUnitsTab = getByRole(AriaRole.TAB, "Business units");

    public AdminSystemPage(Page page) {
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
