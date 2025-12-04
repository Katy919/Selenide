import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class RegistrationTest {


    public String generateDate(int days, String pattern) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
    }

    @BeforeAll
    static void setUp() {
        Selenide.open("http://localhost:9999");
    }

    @Test
    void shouldOrderCardDelivery() {
        String planningDate = generateDate(4, "dd.MM.yyyy");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").click();
        $("[data-test-id='date'] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Голубева Екатерина");
        $("[data-test-id='phone'] input").setValue("+79117115611");
        $("[data-test-id='agreement']").click();
        $$("button").filter(Condition.visible).find(Condition.text("Забронировать")).click();
        $(withText("Успешно!")).should(Condition.visible, Duration.ofSeconds(15));
        $(".notification__content").should(Condition.text("Встреча успешно забронирована на " + planningDate));
    }
}
