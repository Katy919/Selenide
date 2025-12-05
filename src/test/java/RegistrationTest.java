import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static com.codeborne.selenide.Selectors.byText;
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
        $$("button").filter(Condition.visible).find(Condition.text("Забронировать")).click();
        $(withText("Успешно!")).should(Condition.visible, Duration.ofSeconds(15));
        $(".notification__content").should(Condition.text("Встреча успешно забронирована на " + planningDate));
    }

    @Test
    void shouldOrderCardDeliveryAcross7Days() {
        LocalDate targetDate = LocalDate.now().plusDays(7);
        String planningDate = targetDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        int targetDay = targetDate.getDayOfMonth();
        int targetMonth = targetDate.getMonthValue();
        int currentMonth = LocalDate.now().getMonthValue();

        $("[data-test-id='city'] input").click();
        $("[data-test-id='city'] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        //$(".input__control").sendKeys("Ка");
        $("[data-test-id='city'] .input__control").sendKeys("Ка");
        $(byText("Махачкала")).shouldBe(Condition.visible, Duration.ofSeconds(15)).click();
        $("[data-test-id=date] button.icon-button").click();
        sleep(1000);
        if (targetMonth == currentMonth) {
        } else {
            $(".calendar__arrow_direction_right[data-step='1']").click();
        }
        $$(".calendar__day").filter(Condition.visible).findBy(Condition.text(String.valueOf(targetDay))).click();
        $("[data-test-id='name'] input").setValue("Голубева Екатерина");
        $("[data-test-id='phone'] input").setValue("+79117115611");
        $("[data-test-id='agreement']").click();
        $$("button").filter(Condition.visible).find(Condition.text("Забронировать")).click();
        $(withText("Успешно!")).should(Condition.visible, Duration.ofSeconds(15));
        $(".notification__content").should(Condition.text("Встреча успешно забронирована на " + planningDate));
    }
}
