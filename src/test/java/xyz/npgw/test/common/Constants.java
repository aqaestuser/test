package xyz.npgw.test.common;

public class Constants {

    public static final String USER_EMAIL = ProjectProperties.getUserEmail();
    public static final String USER_PASSWORD = ProjectProperties.getUserPassword();

    public static final String BASE_URL = ProjectProperties.getBaseUrl();
    public static final String LOGIN_PAGE_URL = BASE_URL + "/";
    public static final String LOGIN_PAGE_URL_AFTER_TOKEN_EXPIRATION = BASE_URL + "/login";
    public static final String DASHBOARD_PAGE_URL = BASE_URL + "/dashboard";
    public static final String TRANSACTIONS_PAGE_URL = BASE_URL + "/transactions";
    public static final String REPORTS_PAGE_URL = BASE_URL + "/reports";
    public static final String SA_PAGE_URL = BASE_URL + "/system";

    public static final String LOGIN_URL_TITLE = "NPGW";
    public static final String DASHBOARD_URL_TITLE = "NPGW";
    public static final String TRANSACTIONS_URL_TITLE = "NPGW";
    public static final String REPORTS_URL_TITLE = "NPGW";
    public static final String SA_URL_TITLE = "NPGW";
}
