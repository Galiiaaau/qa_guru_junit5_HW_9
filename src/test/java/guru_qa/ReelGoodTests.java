package guru_qa;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.*;

public class ReelGoodTests {

    @BeforeEach
    void setUp() {
        Selenide.open("https://reelgood.com/");
    }

    @ValueSource(
            strings = {
                    "Horror of Dracula (1958)",
                    "American Horror Story (2011)"
            }
    )
    @ParameterizedTest(name = "Проверка страницы reelgood на наличие фильма {0}")
    void checkIfPageContainsAGivenFilm(String horrorName) {
        $("#searchbox").setValue("horror").pressEnter();
        $("#app_mountpoint").shouldHave(Condition.text(horrorName));
    }


    @CsvSource(value = {
            "american horror story, Movies & TV Shows with 'american horror story",
            "Halloween, Movies & TV Shows with 'halloween'"
    })
    @ParameterizedTest(name = "Поиск фильма {0} должен возвращать {1}")
    void checkIfMovieSearchingReturnsSpecificText(String movieName, String specificText) {
        $("#searchbox").setValue(movieName).pressEnter();
        $("#app_mountpoint").shouldHave(Condition.text(specificText));
    }



    @CsvFileSource(resources = "/testdata/movieNameAndYear.csv")
    @ParameterizedTest(name = "Дата выхода фильма {0} - {1} год")
    void checkTheReleaseDateOfSpecificMovie(String movieName, String releaseDate) {
        $("#searchbox").setValue(movieName).pressEnter();
        $(".ReactVirtualized__Grid").shouldHave(Condition.text(releaseDate));
    }



    static Stream<Arguments> resultShouldHaveAMaturityRating() {
        return Stream.of(
                Arguments.of("Midsommar", "18+ (R)"),
                Arguments.of("Cinderella", "7+ (PG)")
        );
    }
    @MethodSource()
    @ParameterizedTest(name = "{0} movie should have {1} maturity rating")
    void resultShouldHaveAMaturityRating(String movieName, String maturityRating) {
        $("#searchbox").setValue(movieName).pressEnter();
        $(".ReactVirtualized__Grid__innerScrollContainer div div").click();
        $("[title='Maturity rating']").shouldHave(Condition.exactText(maturityRating));
    }
}
