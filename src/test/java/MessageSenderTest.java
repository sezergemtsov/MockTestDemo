import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class MessageSenderTest {

    MessageSender ms;
    GeoService geoService;
    LocalizationService localizationService;
    Map<String, String> headers = new HashMap<String, String>();


    @BeforeEach
    public void init() {
        headers.clear();
        geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp("172."))
                .thenReturn(new Location("Moscow", Country.RUSSIA, "Lenina", 1));
        Mockito.when(geoService.byIp("96."))
                .thenReturn(new Location("New York", Country.USA, " 10th Avenue", 1));
        Mockito.when(geoService.byIp("127.0.0.1"))
                .thenReturn(new Location(null, null, null, 0));
        localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Country.RUSSIA))
                .thenReturn("Добро");
        Mockito.when(localizationService.locale(Country.USA))
                .thenReturn("Welcome");
        Mockito.when(localizationService.locale(Country.GERMANY))
                .thenReturn("Welcome");
        Mockito.when(localizationService.locale(Country.BRAZIL))
                .thenReturn("Welcome");
        ms = new MessageSenderImpl(geoService, localizationService);
    }


    @ParameterizedTest
    @ValueSource(strings = {"172.", "96."})
    public void sendTest(String strings) {
        //arrange
        String expectedResult = "Welcome";
        String expectedResult1 = "Добро";
        headers.put("x-real-ip", strings);
        //act
        String result = ms.send(headers);
        //assert
        switch (strings) {
            case "172.":
                assertThat(result, containsString(expectedResult1));
                break;
            default:
                assertThat(result, containsString(expectedResult));
                break;
        }
    }
}
