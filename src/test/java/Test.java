import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

public class Test {
    @org.junit.jupiter.api.Test
    public void messageSenderRUTest() {
        String str = "Это Россия!";
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(Mockito.<String>any()))
                .thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));
        LocalizationService localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationService.locale(Country.RUSSIA)).thenReturn(str);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.*.*.*");
        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        Assertions.assertEquals(str, messageSender.send(headers));
    }

    @org.junit.jupiter.api.Test
    public void messageSenderUSATest() {
        String str = "Это США!";
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(Mockito.<String>any())).thenReturn(new Location("New York", Country.USA, null, 0));
        LocalizationService localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationService.locale(Country.USA)).thenReturn(str);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "96.*.*.*");
        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        Assertions.assertEquals(str, messageSender.send(headers));
    }

    @org.junit.jupiter.api.Test
    public void geoServiceByIPTest() {
        Location location;
        String localHost = "127.0.0.1";
        String russianIP = "172.0.0.1";
        String usaIP = "96.0.0.1";
        GeoService geoService = new GeoServiceImpl();
        location = geoService.byIp(localHost);
        Assertions.assertNull(location.getCountry());
        location = geoService.byIp(russianIP);
        Assertions.assertEquals(Country.RUSSIA, location.getCountry());
        location = geoService.byIp(usaIP);
        Assertions.assertEquals(Country.USA, location.getCountry());
    }

    @org.junit.jupiter.api.Test
    public void localizationServiceLocaleTest() {
        String str;
        LocalizationService localizationService = new LocalizationServiceImpl();
        str = localizationService.locale(Country.RUSSIA);
        Assertions.assertEquals(str, "Добро пожаловать");
        str = localizationService.locale(Country.GERMANY);
        Assertions.assertEquals(str, "Welcome");
        str = localizationService.locale(Country.USA);
        Assertions.assertEquals(str, "Welcome");
        str = localizationService.locale(Country.BRAZIL);
        Assertions.assertEquals(str, "Welcome");
    }
}
