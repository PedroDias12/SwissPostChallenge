package com.example.challenge.runner;

import com.example.challenge.model.Asset;
import com.example.challenge.repository.AssetRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private AssetRepository assetRepository;

    @Override
    @Transactional
    public void run(String... args) {
        List<Asset> assetList = List.of(Asset.builder().token("bitcoin").symbol("BTC").price(0.0).build(),
                                        Asset.builder().token("ethereum").symbol("ETH").price(0.0).build(),
                                        Asset.builder().token("xrp").symbol("XRP").price(0.0).build(),
                                        Asset.builder().token("cardano").symbol("ADA").price(0.0).build(),
                                        Asset.builder().token("litecoin").symbol("LTC").price(0.0).build(),
                                        Asset.builder().token("bitcoin-cash").symbol("BCH").price(0.0).build(),
                                        Asset.builder().token("eos").symbol("EOS").price(0.0).build());
        for (Asset asset : assetList) {
            Optional<Asset> assetOptional = assetRepository.findByToken(asset.getToken());
            if (assetOptional.isEmpty()) {
                assetRepository.save(asset);
            }
        }
        log.info("DataLoader ran successfully!");
    }
}
