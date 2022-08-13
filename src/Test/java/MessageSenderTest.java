import org.junit.jupiter.api.AfterEach;
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
import static org.hamcrest.Matchers.nullValue;

public class MessageSenderTest {

    MessageSender ms;
    GeoService geoService;
    LocalizationService localizationService;
    Map<String, String> headers = new HashMap<String, String>();


    @BeforeEach
    public void init() {
        geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp("172.0.0.1"))
                .thenReturn(new Location(null, Country.RUSSIA, null, 0));
        Mockito.when(geoService.byIp("96.0.0.1"))
                .thenReturn(new Location(null, Country.USA, null, 0));
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

    @AfterEach
    public void clear() {
        ms =null;
        geoService=null;
        localizationService=null;
        headers.clear();
    }


    @ParameterizedTest
    @ValueSource(strings = {"172.0.32.11", "96.44.183.149","127.0.0.1"})
    public void sendTest(String strings) {
        //arrange
        if (strings.startsWith("172.")) {
            strings = "172.0.0.1";
        } else if (strings.startsWith("127.")) {
            strings="127.0.0.1";
        } else {
            strings = "96.0.0.1";
        }
        String expectedResult = "Welcome";
        String expectedResult1 = "Добро";
        headers.put("x-real-ip", strings);
        //act
        String result = ms.send(headers);
        //assert

        if (strings.startsWith("172.")) {
            assertThat(result, containsString(expectedResult1));
        } else if (strings.startsWith("127.")) {
            assertThat(result, nullValue());
        } else {
            assertThat(result, containsString(expectedResult));
        }
    }
}
