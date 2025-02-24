package com.example.challenge.service;

import com.example.challenge.exception.ServiceException;
import com.example.challenge.resource.coinCapAPI.CoinDataResource;
import com.example.challenge.resource.coinCapAPI.CoinPriceResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static com.example.challenge.TestContants.TOKEN1;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@TestPropertySource(properties = "coincap.api.url=https://api.coincap.io")
@ExtendWith(MockitoExtension.class)
public class CoinCapServiceTest {
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private CoinCapService coinCapService;
    private static final LocalDate DATE = LocalDate.of(2025, 2, 23);

    @Test
    void testGetTokensPriceResource() throws Exception {
        CoinPriceResource coinPriceResource = new CoinPriceResource();
        CoinDataResource data = CoinDataResource.builder().priceUsd("100").build();
        coinPriceResource.setData(data);

        ResponseEntity<CoinPriceResource> response = ResponseEntity.ok(coinPriceResource);
        when(restTemplate.getForEntity(anyString(), eq(CoinPriceResource.class))).thenReturn(response);

        CoinPriceResource result = coinCapService.getTokensPriceResource(TOKEN1);

        assertNotNull(result);
        assertEquals("100", result.getData().getPriceUsd());
    }

    @Test
    void testGetTokensPriceResourceThrowsException() {
        when(restTemplate.getForEntity(anyString(), eq(CoinPriceResource.class)))
                .thenThrow(new RestClientException("RestClientException"));

        assertThrows(RestClientException.class, () -> coinCapService.getTokensPriceResource(TOKEN1));
    }

    @Test
    void testGetHistoricalPriceToday() throws ServiceException {
        CoinPriceResource coinPriceResource = new CoinPriceResource();
        CoinDataResource data = CoinDataResource.builder().priceUsd("1000").build();
        coinPriceResource.setData(data);

        ResponseEntity<CoinPriceResource> response = ResponseEntity.ok(coinPriceResource);
        when(restTemplate.getForEntity(anyString(), eq(CoinPriceResource.class))).thenReturn(response);

        double price = coinCapService.getHistoricalPrice(TOKEN1, null);

        assertEquals(1000.0, price);
    }

    @Test
    void testGetHistoricalPriceInvalidDate() {
        assertThrows(ServiceException.class, () -> coinCapService.getHistoricalPrice(TOKEN1, LocalDate.now().plusDays(1)));
    }
}
