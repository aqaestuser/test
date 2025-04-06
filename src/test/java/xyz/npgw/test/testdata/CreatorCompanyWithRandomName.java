package xyz.npgw.test.testdata;

import lombok.Getter;
import net.datafaker.Faker;

@Getter
public class CreatorCompanyWithRandomName {

    private final String name;
    private final String type;

    private static final Faker faker = new Faker();

    public CreatorCompanyWithRandomName(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public static CreatorCompanyWithRandomName random() {
        return new CreatorCompanyWithRandomName(
                faker.company().name() + " " + faker.number().digits(5),
                faker.company().industry()
        );
    }
}
