package xyz.npgw.test.page.component;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import xyz.npgw.test.page.base.BaseComponent;

import java.util.NoSuchElementException;

public class TableComponent extends BaseComponent {

    private final Locator header = getPage().getByRole(AriaRole.COLUMNHEADER);
    private final Locator rows = getPage().getByRole(AriaRole.ROW);

    public TableComponent(Page page) {
        super(page);
    }

    public Locator getTableHeader() {
        return header;
    }

    public Locator getRowsWithoutHeader() {
        return rows.filter(new Locator.FilterOptions().setHasNot(header));
    }

    public Locator getColumnBySelector(String selector) {
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
