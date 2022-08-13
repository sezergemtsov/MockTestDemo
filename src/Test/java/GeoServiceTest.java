import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;

public class GeoServiceTest {

    GeoService gs;

    @BeforeEach
    public void init() {
        gs = new GeoServiceImpl();
    }

    @AfterEach
    public void clear() {
        gs = null;
    }

    @ParameterizedTest
    @ValueSource(strings = {"127.0.0.1", "172.0.32.11", "96.44.183.149"})
    public void byIPPTest(String strings) {
        //arrange
        //act
        Location result = gs.byIp(strings);
        //assertion
        if (strings.startsWith("172.")) {
            Assertions.assertEquals(result.getCountry(), Country.RUSSIA);
        } else if (strings.startsWith("127.")) {
            Assertions.assertEquals(result.getCountry(), null);
        } else {
            Assertions.assertEquals(result.getCountry(), Country.USA);
        }
    }
}
