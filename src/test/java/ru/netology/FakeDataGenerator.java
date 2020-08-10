package ru.netology;

import com.github.javafaker.Faker;

import java.util.Locale;

public class FakeDataGenerator {
    private FakeDataGenerator() {
    }

//    public static String getCity(String locale){
//        Faker faker = new Faker(new Locale(locale));
//        return faker.address().city();
//    }

    public static class RegistrationData {
        private RegistrationData() {
        }

        public static RegistrationDataForCard generateForCard() {
            Faker faker = new Faker(new Locale("ru"));
            return new RegistrationDataForCard(faker.address().city(), faker.name().name().replace("ё", "е"), faker.phoneNumber().phoneNumber());
        }
    }
}
