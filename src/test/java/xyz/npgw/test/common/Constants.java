package xyz.npgw.test.common;

import java.util.Map;

public class Constants {

    public static final String BASE_URL = ProjectProperties.getBaseURL();
    public static final String LOGIN_PAGE_URL = BASE_URL + "/";
    public static final String DASHBOARD_PAGE_URL = BASE_URL + "/dashboard";
    public static final String TRANSACTIONS_PAGE_URL = BASE_URL + "/transactions";
    public static final String REPORTS_PAGE_URL = BASE_URL + "/reports";
    public static final String SYSTEM_PAGE_URL = BASE_URL + "/system";

    public static final String LOGIN_URL_TITLE = "NPGW";
    public static final String DASHBOARD_URL_TITLE = "NPGW";
    public static final String TRANSACTIONS_URL_TITLE = "NPGW";
    public static final String REPORTS_URL_TITLE = "NPGW";
    public static final String SYSTEM_URL_TITLE = "NPGW";

    public static final String COMPANY_NAME_FOR_TEST_RUN = "CompanyForTestRunOnly Inc.";
    public static final String BUSINESS_UNIT_FOR_TEST_RUN = "MerchantCompanyForTestRunOnly Inc.";
    public static final String MERCHANT_ID_FOR_TEST_RUN = "merchant-server-ngenius";

    public static final String ONE_DATE_FOR_TABLE = "12/08/2025";
    public static final String AUGUST_FOR_TABLE = "01/08/2025 - 31/08/2025";

    public static final String[] CURRENCY_OPTIONS = {"ALL", "EUR", "USD", "GBP"};
    public static final String[] CARD_OPTIONS = {"ALL", "VISA", "MASTERCARD"};
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
    public static final Map<String, String> TOOLTIPSCONTENT = Map.ofEntries(
            Map.entry("circle-plus", "Add user"),
            Map.entry("xmark", "Reset filter"),
            Map.entry("arrows-rotate", "Refresh data"),
            Map.entry("gear", "Settings"),
            Map.entry("pencil", "Edit user"),
            Map.entry("ban", "Deactivate user"),
            Map.entry("circle-exclamation", "Reset user password"),
            Map.entry("trash", "Delete user"),
            Map.entry("check", "Activate user"),
            Map.entry("AddCompanyButton", "Add company"),
            Map.entry("ResetButtonTeamPage", "Reset filter"),
            Map.entry("SettingsButtonMerchantsPage", "Settings"),
            Map.entry("ViewDocumentationButtonMerchantsPage", "View documentation"),
            Map.entry("EditCompanyButton", "Edit selected company"),
            Map.entry("DeleteCompanyButton", "Delete selected company"),
            Map.entry("ButtonAddMerchant", "Add business unit"),
            Map.entry("ApplyFilterButtonsMerchantsPage", "Refresh data"),
            Map.entry("CopyBusinessUnitIDToClipboardButton", "Copy Business unit ID to clipboard"),
            Map.entry("EditBusinessUnitButton", "Edit business unit"),
            Map.entry("DeleteBusinessUnitButton", "Delete business unit")
    );
}
