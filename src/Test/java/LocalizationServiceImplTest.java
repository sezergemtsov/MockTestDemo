import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.netology.entity.Country;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class LocalizationServiceImplTest {

    LocalizationService lcs;

    @BeforeEach
    public void init() {
        lcs = new LocalizationServiceImpl();
    }
    @AfterEach
    public void clear() {
        lcs = null;
    }


    @ParameterizedTest
    @EnumSource(Country.class)
    public void localePTest(Country country) {

        //arrange
        //act
        String expectedResult = lcs.locale(country);

        //assert
        switch (country) {
            case RUSSIA:
                assertThat(expectedResult, containsString("Добро"));
                break;
            default:
                assertThat(expectedResult, containsString("Welcome"));
                break;
        }
    }
}
