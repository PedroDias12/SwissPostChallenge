package com.example.challenge.service;

import com.example.challenge.exception.ServiceException;
import com.example.challenge.mapper.AssetMapper;
import com.example.challenge.model.Asset;
import com.example.challenge.repository.AssetRepository;
import com.example.challenge.resource.AssetResource;
import com.example.challenge.resource.coinCapAPI.CoinPriceResource;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class AssetService {
    private final AssetMapper assetMapper;
    private final AssetRepository assetRepository;


    public List<Asset> getAssets() {
        return assetRepository.findAll();
    }

    public List<AssetResource> getAssetsResources() {
        return assetMapper.entitiesToResources(getAssets());
    }

    public AssetResource getAssetResourceBySymbol(String symbol) throws ServiceException {
        return assetMapper.entityToResource(getAssetBySymbol(symbol));
    }

    public Asset getAssetBySymbol(String symbol) throws ServiceException {
        return assetRepository.findBySymbol(symbol).orElseThrow(() -> new ServiceException("Asset with symbol " + symbol + " not found", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void updateAssetsFromCoinPriceResource(final CoinPriceResource resource) throws Exception {
        Asset entity = assetRepository.findByToken(resource.getData().getId())
                .orElseThrow(() -> new Exception("Token " + resource.getData().getId() + " not found in database"));
        entity.setPrice(Double.parseDouble(resource.getData().getPriceUsd()));
        updateAsset(entity);
    }

    public void updateAsset(Asset asset) {
        assetRepository.save(asset);
    }
}
