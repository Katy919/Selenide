import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;


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
        $$("button").find(Condition.text("Забронировать")).click();
        $(withText("Успешно!")).should(Condition.visible, Duration.ofSeconds(15));
        $(".notification__content").should(Condition.text("Встреча успешно забронирована на " + planningDate));
    }

    @Test
    void shouldOrderCardDeliveryAcross3Days() {
        LocalDate defaultCalendarDate = LocalDate.now().plusDays(3);
        LocalDate targetDate = defaultCalendarDate;
        String planningDate = targetDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        int targetDay = targetDate.getDayOfMonth();
        int targetMonth = targetDate.getMonthValue();
        int defaultMonth = defaultCalendarDate.getMonthValue();

        $("[data-test-id='city'] input").click();
        $("[data-test-id='city'] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='city'] .input__control").sendKeys("Ка");
        $(".input__popup").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $$(".menu-item__control").findBy(Condition.text("Махачкала")).click();
        $("[data-test-id=date] button.icon-button").click();
        sleep(1000);
        if (targetMonth != defaultMonth) {
            $(".calendar__arrow_direction_right[data-step='1']").click();
        }
        $$(".calendar__day").findBy(Condition.text(String.valueOf(targetDay))).click();
        $("[data-test-id='name'] input").setValue("Голубева Екатерина");
        $("[data-test-id='phone'] input").setValue("+79117115611");
        $("[data-test-id='agreement']").click();
        $$("button").find(Condition.text("Забронировать")).click();
        $(withText("Успешно!")).should(Condition.visible, Duration.ofSeconds(15));
        $(".notification__content").should(Condition.text("Встреча успешно забронирована на " + planningDate));
    }
}
