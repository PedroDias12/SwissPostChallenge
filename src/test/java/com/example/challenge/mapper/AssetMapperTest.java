package com.example.challenge.mapper;

import com.example.challenge.model.Asset;
import com.example.challenge.resource.AssetResource;
import com.example.challenge.resource.coinCapAPI.CoinDataResource;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.example.challenge.TestContants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AssetMapperTest {
    private static final Asset ENTITY = Asset.builder().token(TOKEN1).symbol(SYMBOL1).price(100.5).build();
    private static final AssetResource RESOURCE = AssetResource.builder().symbol(SYMBOL1).price(100.5).build();
    private final AssetMapper assetMapper = Mappers.getMapper(AssetMapper.class);

    @Test
    void testEntityToResource() {
        AssetResource assetResource = assetMapper.entityToResource(ENTITY);

        assertNotNull(assetResource);
        assertEquals(RESOURCE, assetResource);
    }

    @Test
    void testResourceToEntity() {
        Asset asset = assetMapper.resourceToEntity(RESOURCE);

        assertNotNull(asset);
        assertEquals(ENTITY.toBuilder().token(null).build(), asset);
    }

    @Test
    void testCoinDataToAsset() {
        CoinDataResource coinData = CoinDataResource.builder().id(TOKEN1).symbol(SYMBOL1).priceUsd("100.5").build();

        Asset asset = assetMapper.coinDataToAsset(coinData);

        assertNotNull(asset);
        assertEquals(ENTITY, asset);
    }

    @Test
    void testEntitiesToResources() {
        List<Asset> assets = List.of(
                ENTITY,
                Asset.builder().token(TOKEN2).symbol(SYMBOL2).price(200.5).build());
        List<AssetResource> expected = List.of(
                RESOURCE,
                AssetResource.builder().symbol(SYMBOL2).price(200.5).build());

        List<AssetResource> assetResources = assetMapper.entitiesToResources(assets);

        assertNotNull(assetResources);
        assertEquals(expected.size(), assetResources.size());
        assertEquals(expected, assetResources);
    }
}