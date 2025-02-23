package com.example.challenge.scheduler;

import com.example.challenge.model.Asset;
import com.example.challenge.resource.coinCapAPI.CoinPriceResource;
import com.example.challenge.service.AssetService;
import com.example.challenge.service.CoinCapService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@AllArgsConstructor
@Component
public class AssetPriceUpdaterScheduler {
    private final CoinCapService coinCapService;
    private final AssetService assetService;

    @Scheduled(fixedRateString = "${asset.price.update.interval}")
    public void updateCoinPriceScheduling() {
        log.info("Update coin prices scheduler started with thread {}", Thread.currentThread().getName());
        List<CompletableFuture<Void>> futures = assetService.getAssets().stream()
                .map(Asset::getToken)
                .map(token ->
                    coinCapService.getTokensPriceAsync(token)
                            .thenAccept(coinPriceResource -> {
                                try {
                                    saveTokenPrice(coinPriceResource);
                                } catch (Exception ex) {
                                    log.error("Error updating the price of token {}}: {}", token,  ex.getMessage());
                                }
                            })
                            .exceptionally(ex -> {
                                log.error("Error getting the price of token {} from CoinCap API: {}", token, ex.getMessage());
                                return null;
                            })
                ).toList();
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.join();
        log.info("Update coin prices scheduler finished with thread {}", Thread.currentThread().getName());
    }

    private void saveTokenPrice(CoinPriceResource coinPriceResource) throws Exception {
        assetService.updateAssetsFromCoinPriceResource(coinPriceResource);
    }
}
