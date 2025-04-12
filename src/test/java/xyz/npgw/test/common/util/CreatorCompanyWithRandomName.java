package xyz.npgw.test.common.util;

import lombok.Getter;
import net.datafaker.Faker;

@Getter
public class CreatorCompanyWithRandomName {

    private final String name;
    private final String type;

    private static final Faker FAKER = new Faker();

    public CreatorCompanyWithRandomName(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public static CreatorCompanyWithRandomName random() {

        return new CreatorCompanyWithRandomName(
                FAKER.company().name() + " " + FAKER.number().digits(5),
                FAKER.company().industry()
        );
    }
}
