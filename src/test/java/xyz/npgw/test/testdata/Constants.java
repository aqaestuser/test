package xyz.npgw.test.testdata;


import xyz.npgw.test.common.ProjectUtils;

public class Constants {

    private static final String BASE_URL = ProjectUtils.getBaseUrl();

    public static final String LOGIN_PAGE_URL = BASE_URL + "/";
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
