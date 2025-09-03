package xyz.npgw.test.page.component.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.BaseComponent;
import xyz.npgw.test.page.system.AdminBusinessUnitsPage;
import xyz.npgw.test.page.system.AdminTeamPage;

import java.util.Objects;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AdminSystemMenuComponent extends BaseComponent {
    private final Locator teamTab = getByRole(AriaRole.TAB, "Team");
    private final Locator businessUnitsTab = getByRole(AriaRole.TAB, "Business units");

    public AdminSystemMenuComponent(Page page) {
        super(page);
    }

    @Step("Click 'Team' tab")
    public AdminTeamPage clickTeamTab() {
        clickTabIfNeededAndCheckIt(teamTab);

        return new AdminTeamPage(getPage());
    }

    @Step("Click 'Business units' tab")
    public AdminBusinessUnitsPage clickBusinessUnitsTab() {
        clickTabIfNeededAndCheckIt(businessUnitsTab);

        return new AdminBusinessUnitsPage(getPage());
    }


    protected void clickTabIfNeededAndCheckIt(Locator tab) {
        tab.waitFor();
        if (Objects.equals(tab.getAttribute("aria-selected"), "false")) {
            tab.click();
            assertThat(tab).hasAttribute("data-selected", "true");
        }
        getPage().waitForLoadState(LoadState.NETWORKIDLE);
    }
}
