package com.example.challenge.mapper;

import com.example.challenge.model.Wallet;
import com.example.challenge.model.WalletAsset;
import com.example.challenge.resource.WalletAssetResource;
import com.example.challenge.resource.WalletResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    @Mapping(target = "assets", ignore = true)
    @Mapping(target = "userEmail", source = "user.email")
    WalletResource walletToResource(Wallet wallet);

    @Mapping(target = "total", expression = "java(calculateTotal(walletResource.getAssets()))")
    @Mapping(target = "assets", source = "walletAssets")
    WalletResource walletToResourceWithDetails(Wallet wallet);

    @Mapping(target = "price", expression = "java(new java.math.BigDecimal(walletAsset.getAsset().getPrice()).setScale(2, java.math.RoundingMode.FLOOR))")
    @Mapping(target = "value", expression = "java(walletAssetResource.getPrice().multiply(walletAssetResource.getQuantity()).setScale(2, java.math.RoundingMode.FLOOR))")
    @Mapping(target = "symbol", source = "asset.symbol")
    WalletAssetResource walletAssetToWalletAssetResource(WalletAsset walletAsset);

    default Double calculateTotal(List<WalletAssetResource> walletAssetResources) {
        return walletAssetResources.stream().mapToDouble(resource -> resource.getValue().doubleValue()).sum();
    }
}
