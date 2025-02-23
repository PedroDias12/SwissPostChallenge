package com.example.challenge.mapper;

import com.example.challenge.model.Asset;
import com.example.challenge.resource.AssetResource;
import com.example.challenge.resource.coinCapAPI.CoinDataResource;
import com.example.challenge.resource.coinCapAPI.CoinPriceResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssetMapper {
    AssetResource entityToResource(Asset entity);
    Asset resourceToEntity(AssetResource resource);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "token", source = "id")
    @Mapping(target = "symbol", source = "symbol")
    @Mapping(target = "price", source = "priceUsd")
    Asset coinDataToAsset(CoinDataResource data);

    List<AssetResource> entitiesToResources(List<Asset> assets);
}
