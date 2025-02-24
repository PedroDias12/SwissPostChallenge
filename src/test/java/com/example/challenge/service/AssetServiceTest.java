package com.example.challenge.service;

import com.example.challenge.exception.ServiceException;
import com.example.challenge.mapper.AssetMapper;
import com.example.challenge.model.Asset;
import com.example.challenge.repository.AssetRepository;
import com.example.challenge.resource.AssetResource;
import com.example.challenge.resource.coinCapAPI.CoinDataResource;
import com.example.challenge.resource.coinCapAPI.CoinPriceResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.challenge.TestContants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AssetServiceTest {
    @Mock
    private AssetRepository assetRepository;
    @Mock
    private AssetMapper assetMapper;
    @InjectMocks
    private AssetService assetService;
    private final Asset asset = Asset.builder().id(1L).symbol(SYMBOL1).price(100.0).build();
    private final AssetResource assetResource = AssetResource.builder().symbol(SYMBOL1).price(100.0).build();

    @Test
    void testGetAssetsResources() {
        when(assetRepository.findAll()).thenReturn(Collections.singletonList(asset));
        when(assetMapper.entitiesToResources(Collections.singletonList(asset))).thenReturn(Collections.singletonList(assetResource));

        List<AssetResource> assetResources = assetService.getAssetsResources();

        assertEquals(1, assetResources.size());
        assertEquals(Collections.singletonList(assetResource), assetResources);
    }

    @Test
    void testGetAssetResourceBySymbol() throws ServiceException {
        when(assetRepository.findBySymbol(SYMBOL1)).thenReturn(Optional.of(asset));
        when(assetMapper.entityToResource(asset)).thenReturn(assetResource);

        AssetResource result = assetService.getAssetResourceBySymbol(SYMBOL1);

        assertNotNull(result);
        assertEquals(assetResource, result);
    }

    @Test
    void testGetAssetBySymbolNotFound() {
        when(assetRepository.findBySymbol(SYMBOL1)).thenReturn(Optional.empty());

        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            assetService.getAssetResourceBySymbol(SYMBOL1);
        });
        assertEquals("Asset with symbol " + SYMBOL1 + " not found", thrown.getMessage());
    }

    @Test
    void testUpdateAssetsFromCoinPriceResource() throws Exception {
        when(assetRepository.findByToken(TOKEN1)).thenReturn(Optional.of(asset));
        CoinPriceResource coinPriceResource = new CoinPriceResource();
        CoinDataResource data = CoinDataResource.builder().priceUsd("10.5").symbol(SYMBOL1).id(TOKEN1).build();
        coinPriceResource.setData(data);

        assetService.updateAssetsFromCoinPriceResource(coinPriceResource);

        verify(assetRepository).save(asset);
        assertEquals(10.5, asset.getPrice());
    }

    @Test
    void testUpdateAssetsFromCoinPriceResourceTokenNotFound() {
        when(assetRepository.findByToken(SYMBOL2)).thenReturn(Optional.empty());
        CoinPriceResource coinPriceResource = new CoinPriceResource();
        CoinDataResource data = CoinDataResource.builder().priceUsd("10.5").symbol(SYMBOL1).id(TOKEN1).build();
        coinPriceResource.setData(data);

        assertThrows(Exception.class, () -> assetService.updateAssetsFromCoinPriceResource(coinPriceResource));
    }
}
