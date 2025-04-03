package xyz.npgw.test.page.base;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.component.HeaderComponent;

public abstract class BasePageWithHeader<T extends BasePageWithHeader<T>> extends BasePage<T> {

    private HeaderComponent<T> header;

    public BasePageWithHeader(Page page) {
        super(page);
    }

    @SuppressWarnings("unchecked")
    public HeaderComponent<T> getHeader() {
        if (header == null) {
            header = new HeaderComponent<>(getPage(), (T) this);
        }
        return header;
    }
}
