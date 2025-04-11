package xyz.npgw.test.common.dto;

import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class CompanyUtils {

    public static Company readCompanyInformationFromJson(String filePath) throws IOException {
        InputStream inputStream = CompanyUtils.class.getClassLoader().getResourceAsStream(filePath);

        return new ObjectMapper().readValue(inputStream, Company.class);
    }
}
