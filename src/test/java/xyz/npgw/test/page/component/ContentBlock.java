package xyz.npgw.test.page.component;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.NoSuchElementException;

public class ContentBlock extends Element {
    private final Locator header = getPage().getByRole(AriaRole.COLUMNHEADER);
    private final Locator rows = getPage().getByRole(AriaRole.ROW);

    public ContentBlock(Page page) {
        super(page, "table[aria-label='transactions table']");
    }

    Locator getRowsWithoutHeader() {
        return getPage().getByRole(AriaRole.ROW).filter(new Locator.FilterOptions().setHasNot(header));
    }

    Locator getColumnBySelector(String selector) {
        int index = -1;
        for (int i = 0; i < header.count(); i++) {
            if (header.nth(i).innerText().equals(selector)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new NoSuchElementException("Column with selector '" + selector + "' not found.");
        }

        return header.nth(index);
    }
}
