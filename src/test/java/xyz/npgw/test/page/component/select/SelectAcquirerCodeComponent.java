package xyz.npgw.test.page.component.select;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.Getter;

@Getter
public class SelectAcquirerCodeComponent<CurrentPageT> extends SelectComponent<CurrentPageT> {

    private final Locator selectAcquirerCodeField = getByLabelExact("Select acquirer code")
            .locator("../input");

    public SelectAcquirerCodeComponent(Page page, CurrentPageT currentPage) {
        super(page, currentPage);
    }

    @Step("Select '{acquirerName}' acquirer using filter")
    public CurrentPageT selectAcquirerCode(String acquirerName) {
        select(selectAcquirerCodeField, acquirerName);

        return currentPage;
    }
}
