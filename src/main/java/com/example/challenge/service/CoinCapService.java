package com.example.challenge.service;

import com.example.challenge.exception.ServiceException;
import com.example.challenge.resource.coinCapAPI.CoinPriceHistoryResource;
import com.example.challenge.resource.coinCapAPI.CoinPriceResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class CoinCapService {
    private final String apiUrl;
    private final RestTemplate restTemplate;

    @Autowired
    public CoinCapService(@Value("${coincap.api.url}") String apiUrl, RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
    }

    @Async
    public CompletableFuture<CoinPriceResource> getTokensPriceAsync(String token) {
        log.info("getTokensPriceAsync for token {} with thread {}", token, Thread.currentThread().getName());
        return CompletableFuture.supplyAsync(() -> getTokensPriceResource(token));
    }

    public CoinPriceResource getTokensPriceResource(String token) throws RestClientException {
        String url = apiUrl + "/assets/" + token;
        log.info("Requesting coin cap api: {}", url);
        ResponseEntity<CoinPriceResource> response = restTemplate.getForEntity(
                url,
                CoinPriceResource.class
        );
        log.info("Response from coin cap api {}: {}", url, response.getBody());
        return response.getBody();
    }

    public double getHistoricalPrice(String token, LocalDate date) throws ServiceException {
        if (date == null || LocalDate.now().isEqual(date)) {
            return Double.parseDouble(getTokensPriceResource(token).getData().getPriceUsd());
        }
        if (LocalDate.now().isBefore(date)) {
            throw new ServiceException("Invalid date " + date, HttpStatus.BAD_REQUEST);
        }

        String fullUrl = UriComponentsBuilder.fromUriString(apiUrl)
                .pathSegment("assets", token, "history")
                .queryParam("interval", "d1")
                .queryParam("start", date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli())
                .queryParam("end", date.plusDays(1L).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli())
                .toUriString();

        log.info("Requesting coin cap api: {}", fullUrl);
        ResponseEntity<CoinPriceHistoryResource> response = restTemplate.getForEntity(
                fullUrl,
                CoinPriceHistoryResource.class
        );
        log.info("Response from coin cap api {}: {}", fullUrl, response.getBody());
        return Double.parseDouble(response.getBody().getData().get(0).getPriceUsd());
    }
}