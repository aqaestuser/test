package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.page.system.TeamPage;

@Log4j2
public class EditUserDialog extends UserDialog<EditUserDialog> {

    public EditUserDialog(Page page) {
        super(page);
    }

    @Step("Click 'Save changes' button")
    public TeamPage clickSaveChangesButton() {
        getPage().route("**/*/list/*", route -> {
            log.info("current route {}", route.request().url());
            if (route.request().url().contains("list")) {
                log.info("list on hold for 1000");
                getPage().waitForTimeout(1000);
            }
            log.info("current route fallback");
            route.fallback();
        });

        getPage().waitForResponse(
                response -> {
                    log.info("response for {} - {}", response.request().url(), response.statusText());
                    if (response.request().url().contains("/portal-v1/user") && response.ok()) {
                        log.info("create done");
                        return true;
                    }
                    return false;
                },
                getPage().getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save changes"))::click
        );
        getPage().unroute("**/*/list/*");

        return new TeamPage(getPage());
    }
}
