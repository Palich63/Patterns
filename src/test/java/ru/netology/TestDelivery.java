package ru.netology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class TestDelivery {
    //    private Faker faker;
    private RegistrationDataForCard fakeData = FakeDataGenerator.RegistrationData.generateForCard();

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
//        faker = new Faker(new Locale("ru"));
    }

    @Test
    void shouldTestDelivery() {
        //Предусмотрел, что в случае если в ФИО появится буква "ё" заменять её на "е"
//        String name = faker.name().name().replace("ё", "е");
//        String phone = faker.phoneNumber().phoneNumber();

        //Проверка возможности доставки в выбранный город
        do {
            $("[data-test-id='city']").$("[type='text']").sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE);
//            String city = faker.address().city();
            $("[data-test-id='city']").$("[type='text']").setValue(fakeData.getCity());
        } while ($("iframe").is(hidden));

        //Выбираем день доставки +4 дня от текушей даты
        LocalDate today = LocalDate.now();
        LocalDate day = today.plus(4, ChronoUnit.DAYS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String deliveryDay = day.format(formatter);

        //Заполняем оставшиеся поля
        $("[data-test-id='date']").$("[type='tel']").sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE);
        $("[data-test-id='date']").$("[type='tel']").setValue(deliveryDay);
        $("[data-test-id='name']").$("[type='text']").setValue(fakeData.getName());
        $("[data-test-id='phone']").$("[type='tel']").setValue(fakeData.getPhone());
        $("[data-test-id='agreement']").click();
        $$("[type='button']").find(exactText("Запланировать")).click();
        $(withText(String.format(deliveryDay))).waitUntil(visible, 15000);

        //Пререпланируем день визита +2 дня от даты визита
        $("[data-test-id='success-notification']").$("[class='icon-button__content']").click();
        LocalDate newDay = day.plus(2, ChronoUnit.DAYS);
        String newDeliveryDay = newDay.format(formatter);
        $("[data-test-id='date']").$("[type='tel']").sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE);
        $("[data-test-id='date']").$("[type='tel']").setValue(newDeliveryDay);
        $$("[type='button']").find(exactText("Запланировать")).click();
        $$("[type='button']").find(exactText("Перепланировать")).click();
        $(withText(String.format(newDeliveryDay))).waitUntil(visible, 15000);
    }
}
