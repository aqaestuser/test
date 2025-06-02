package xyz.npgw.test.common.util;

import com.microsoft.playwright.APIRequestContext;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.common.entity.Acquirer;
import xyz.npgw.test.common.entity.BusinessUnit;
import xyz.npgw.test.common.entity.Company;
import xyz.npgw.test.common.entity.User;
import xyz.npgw.test.common.entity.UserRole;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class CleanupUtils {

    private static final List<String> COMPANY = List.of("Luke Payments", "CompanyForTestRunOnly Inc.", "super");
    private static final List<String> USER = List.of(
            "test@email.com", "supertest@email.com", "admintest@email.com", "usertest@email.com");
    private static final List<String> ACQUIRER = List.of("Luke EUR MID 1");



    public static void deleteCompanies(APIRequestContext request) {
        Arrays.stream(Company.getAll(request))
                .filter(c -> !COMPANY.contains(c.companyName()))
                .forEach(item -> TestUtils.deleteCompany(request, item.companyName()));
    }

    public static void clean(APIRequestContext request) {
        deleteUnprotectedAcquirers(request);

        deleteMerchantsForCompaniesWithoutUsersAndWithDefaultValues(request);
        deleteUnprotectedUsers(request);
        deleteCompaniesWithoutUsersAndMerchants(request);
    }

    public static void deleteCompaniesWithoutUsersAndMerchants(APIRequestContext request) {
        Arrays.stream(Company.getAll(request))
                .filter(c -> !COMPANY.contains(c.companyName()))
                .forEach(item -> {
                    BusinessUnit[] businessUnits = BusinessUnit.getAll(request, item.companyName());
                    User[] users = User.getAll(request, item.companyName());

                    if (users.length == 0 && businessUnits.length == 0) {
                        log.info("---will delete company---> |{}|", item.companyName());
                        Company.delete(request, item.companyName());
                    }
                });
    }

    public static void deleteMerchantsForCompaniesWithoutUsersAndWithDefaultValues(APIRequestContext request) {
        Arrays.stream(Company.getAll(request))
                .filter(c -> !COMPANY.contains(c.companyName()))
                .forEach(item -> {
                    User[] users = User.getAll(request, item.companyName());
                    if (users.length == 0) {
                        log.info("delete all merchants from |{}| without users", item.companyName());
                        BusinessUnit[] businessUnits = BusinessUnit.getAll(request, item.companyName());
                        for (BusinessUnit businessUnit : businessUnits) {
                            log.info("---will delete merchant ---> |{}|", businessUnit.merchantId());
                            BusinessUnit.delete(request, item.companyName(), businessUnit);
                        }
                    }
                });
    }

    public static void deleteUnprotectedUsers(APIRequestContext request) {
        Arrays.stream(Company.getAll(request))
                .filter(c -> !COMPANY.contains(c.companyName()))
                .forEach(item -> {
                    log.info("delete users from |{}|", item.companyName());
                    User[] users = User.getAll(request, item.companyName());
                    for (User user : users) {

                        //any user role is linked to invalid merchantId
                        boolean deleteUser = false;
                        for (String merchantId : user.merchantIds()) {
                            if (merchantId.isBlank()
                                    || !merchantId.startsWith("id.merchant.")
                                    || merchantId.matches("^id\\.merchant\\.[0-9a-f]{32}$")) {

                                log.info("---will delete invalid merchant ---> |{}|", merchantId);
                                BusinessUnit.delete(request,
                                        item.companyName(),
                                        new BusinessUnit(merchantId, ""));
                                deleteUser = true;
                            }
                        }
                        if (deleteUser) {
                            log.info("any user role is linked to invalid merchantId");
                            log.info("--- will delete user with invalid merchantId ---> |{}|", user.email());
                            User.delete(request, user);
                        }

                        if (user.userRole() == UserRole.ADMIN && user.merchantIds().length > 0) {
                            log.info("company admins with merchant");
                            for (String merchantId : user.merchantIds()) {
                                log.info("---will delete admin merchant ---> |{}|", merchantId);
                                BusinessUnit.delete(request,
                                        item.companyName(),
                                        new BusinessUnit(merchantId, ""));
                            }
                        }

                        if (user.userRole() == UserRole.ADMIN) {
                            log.info("all company admins will be removed");
                            log.info("---will delete admin ---> |{}|", user.email());
                            User.delete(request, user);
                        }


                        if (user.userRole() == UserRole.USER && user.merchantIds().length == 0) {
                            log.info("company analyst should have some merchantId");
                            log.info("---will delete analyst ---> |{}|", user.email());
                            User.delete(request, user);
                        }

                        if (user.userRole() == UserRole.SUPER && !"super".equals(user.companyName())) {
                            log.info("SUPER user outside SUPER company");
                            log.info("--- will delete outside SUPER ---> |{}|", user.email());
                            User.delete(request, user);
                        }

                        if (user.userRole() == UserRole.SUPER && user.merchantIds().length > 0) {
                            log.info("super admins with merchant");
                            for (String merchantId : user.merchantIds()) {
                                log.info("---will delete super merchant ---> |{}|", merchantId);
                                BusinessUnit.delete(request,
                                        item.companyName(),
                                        new BusinessUnit(merchantId, ""));
                            }
                        }

                        if (user.userRole() == UserRole.SUPER && !USER.contains(user.email())) {
                            log.info("unprotected SUPER user");
                            log.info("--- will delete unprotected SUPER ---> |{}|", user.email());
                            User.delete(request, user);
                        }
                    }
                });
    }

    public static void deleteUnprotectedAcquirers(APIRequestContext request) {
        Arrays.stream(Acquirer.getAll(request))
                .filter(acquirer -> !ACQUIRER.contains(acquirer.acquirerName()))
                .filter(acquirer -> !acquirer.acquirerName().startsWith("acquirerName "))
                .forEach(item -> {
                    log.info("---will delete acquirer ---> |{}|", item.acquirerName());
                    Acquirer.delete(request, item.acquirerName());
                });
    }
}
