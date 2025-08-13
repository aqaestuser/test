package xyz.npgw.test.common;

public class Constants {

    public static final String LOGIN_PAGE_URL = ProjectProperties.getBaseUrl() + "/";
    public static final String DASHBOARD_PAGE_URL = ProjectProperties.getBaseUrl() + "/dashboard";
    public static final String TRANSACTIONS_PAGE_URL = ProjectProperties.getBaseUrl() + "/transactions";
    public static final String REPORTS_PAGE_URL = ProjectProperties.getBaseUrl() + "/reports";
    public static final String SYSTEM_PAGE_URL = ProjectProperties.getBaseUrl() + "/system";

    public static final String LOGIN_URL_TITLE = "NPGW";
    public static final String DASHBOARD_URL_TITLE = "NPGW";
    public static final String TRANSACTIONS_URL_TITLE = "NPGW";
    public static final String REPORTS_URL_TITLE = "NPGW";
    public static final String SYSTEM_URL_TITLE = "NPGW";

    public static final String COMPANY_NAME_FOR_TEST_RUN = "CompanyForTestRunOnly Inc.";
    public static final String BUSINESS_UNIT_FOR_TEST_RUN = "MerchantCompanyForTestRunOnly Inc.";
    public static final String MERCHANT_ID_FOR_TEST_RUN = "merchant-server-ngenius";

    public static final String ONE_DATE_FOR_TABLE = "12/08/2025";

    public static final String[] TRANSACTION_STATUSES = {
            "ALL",
            "INITIATED",
            "PENDING",
            "SUCCESS",
            "AUTHORISED",
            "PARTIAL_CAPTURE",
            "FAILED",
            "CANCELLED",
            "EXPIRED",
            "PARTIAL_REFUND",
            "REFUND"
    };
}
