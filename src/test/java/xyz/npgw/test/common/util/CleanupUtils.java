package xyz.npgw.test.common.util;

import com.microsoft.playwright.APIRequestContext;
import xyz.npgw.test.common.entity.Acquirer;
import xyz.npgw.test.common.entity.Company;
import xyz.npgw.test.common.entity.User;

import java.util.Arrays;
import java.util.List;

public class CleanupUtils {

    private static final List<String> COMPANY = List.of(
            "Amazon", "CompanyForTestRunOnly Inc.", "super", "Luke Company");
    private static final List<String> USER = List.of("test@email.com", "supertest@email.com");
    private static final List<String> ACQUIRER = List.of("Luke EUR MID 1");

    public static void clean(APIRequestContext request) {
        deleteCompanies(request);
        deleteAcquirers(request);
        deleteUsersFromSuper(request);
    }

    public static void deleteCompanies(APIRequestContext request) {
        Arrays.stream(Company.getAll(request))
                .filter(c -> !COMPANY.contains(c.companyName()))
                .filter(c -> c.companyName().matches("^\\d{4}\\.\\d{6}.*$"))
                .filter(c -> TestUtils.isOneHourOld(c.companyName()))
                .forEach(item -> TestUtils.deleteCompany(request, item.companyName()));
    }

    public static void deleteAcquirers(APIRequestContext request) {
        Arrays.stream(Acquirer.getAll(request))
                .filter(acquirer -> !ACQUIRER.contains(acquirer.getAcquirerName()))
                .filter(acquirer -> acquirer.getAcquirerName().matches("^\\d{4}\\.\\d{6}.*$"))
                .filter(acquirer -> !acquirer.getAcquirerName().startsWith("acquirerName "))
                .filter(acquirer -> TestUtils.isOneHourOld(acquirer.getAcquirerName()))
                .forEach(item -> Acquirer.delete(request, item.getAcquirerName()));
    }

    private static void deleteUsersFromSuper(APIRequestContext request) {
        Arrays.stream(User.getAll(request, "super"))
                .filter(user -> !USER.contains(user.email()))
                .filter(user -> user.email().matches("^\\d{4}\\.\\d{6}.*$"))
                .filter(user -> TestUtils.isOneHourOld(user.email()))
                .forEach(user -> User.delete(request, user.email()));
    }
}
