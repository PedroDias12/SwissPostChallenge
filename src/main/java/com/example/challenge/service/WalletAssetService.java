package com.example.challenge.service;

import com.example.challenge.exception.CannotBuyAssetException;
import com.example.challenge.exception.ServiceException;
import com.example.challenge.model.Asset;
import com.example.challenge.model.Wallet;
import com.example.challenge.model.WalletAsset;
import com.example.challenge.model.WalletAssetId;
import com.example.challenge.repository.WalletAssetRepository;
import com.example.challenge.resource.WalletAssetResource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class WalletAssetService {
    private final static int NUMBER_OF_RETRIES = 3;
    private WalletAssetRepository walletAssetRepository;
    private WalletService walletService;
    private AssetService assetService;
    private CoinCapService coinCapService;

    public List<WalletAsset> getWalletAssetsByWalletId(Long walletId) {
        return walletAssetRepository.findByWalletId(walletId);
    }

    @Transactional
    public void addAssetToWallet(WalletAssetResource resource) throws ServiceException {
        validateResource(resource);
        Wallet wallet = walletService.getById(resource.getWalletId());
        Asset asset = assetService.getAssetBySymbol(resource.getSymbol());
        WalletAssetId id = WalletAssetId.builder().walletId(wallet.getId()).assetId(asset.getId()).build();
        tryToMatchPrice(resource, asset);

        WalletAsset walletAsset = walletAssetRepository.findById(id)
                .orElse(WalletAsset.builder().id(id).wallet(wallet).asset(asset).quantity(0.0).build());
        walletAsset.setQuantity(walletAsset.getQuantity() + resource.getQuantity().doubleValue());

        walletAssetRepository.save(walletAsset);
    }

    private void tryToMatchPrice(WalletAssetResource resource, Asset asset) throws ServiceException {
        for (int i = 1, coinCapExceptions = 0; i <= NUMBER_OF_RETRIES; i++) {
            try {
                double assetCurrentPrice = Double.parseDouble(coinCapService.getTokensPriceResource(asset.getToken()).getData().getPriceUsd());
                if (resource.getPrice().doubleValue() >= assetCurrentPrice) {
                    break;
                }
                log.info("Can't add asset to wallet {} because {} price is {} and requested value is {}", resource.getWalletId(), resource.getSymbol(), assetCurrentPrice, resource.getPrice());
                if (i == NUMBER_OF_RETRIES) {
                    throw new CannotBuyAssetException("Can't add asset " + resource.getSymbol() + " to wallet " + resource.getWalletId() + " for " + resource.getPrice() + " because its price is " + assetCurrentPrice);
                }
                Thread.sleep(1000);
            } catch (RestClientException ignored) {
                if (++coinCapExceptions == NUMBER_OF_RETRIES) {
                    throw new ServiceException("Can't get information from provider...", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void validateResource(WalletAssetResource resource) throws ServiceException {
        if (resource.getQuantity() == null || resource.getQuantity().compareTo(BigDecimal.ZERO) <= 0 || Strings.isBlank(resource.getSymbol())) {
            throw new ServiceException("Invalid information to add an asset: " + resource, HttpStatus.BAD_REQUEST);
        }
    }
}
