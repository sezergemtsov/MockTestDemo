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

    @ParameterizedTest
    @ValueSource(strings = {"127.0.0.1", "172.0.32.11", "96.44.183.149"})
    public void byIPPTest(String strings) {
        //arrange
        //act
        Location result = gs.byIp(strings);
        //assertion
        switch (strings) {
            case "172.0.32.11":
                Assertions.assertEquals(result.getCountry(), Country.RUSSIA);
                break;
            case "127.0.0.1":
                Assertions.assertEquals(result.getCountry(), null);
                break;
            default:
                Assertions.assertEquals(result.getCountry(), Country.USA);
                break;
        }
    }
}
