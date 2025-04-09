package xyz.npgw.test.common;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

public enum BrowserFactory {

    CHROMIUM {
        @Override
        public Browser createInstance(Playwright playwright) {
            return playwright.chromium().launch(getBrowserOptions());
        }
    },

    FIREFOX {
        @Override
        public Browser createInstance(Playwright playwright) {
            return playwright.firefox().launch(getBrowserOptions());
        }
    },

    WEBKIT {
        @Override
        public Browser createInstance(Playwright playwright) {
            return playwright.webkit().launch(getBrowserOptions());
        }
    };

    public static Browser getBrowser(Playwright playwright) {
        try {
            return BrowserFactory.valueOf(ProjectProperties.getBrowserType()).createInstance(playwright);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported browser: %s".formatted(ProjectProperties.getBrowserType()));
        }
    }

    private static BrowserType.LaunchOptions getBrowserOptions() {
        return new BrowserType
                .LaunchOptions()
                .setHeadless(ProjectProperties.isHeadlessMode())
                .setSlowMo(ProjectProperties.getSlowMoMode());
    }

    public abstract Browser createInstance(Playwright playwright);
}
