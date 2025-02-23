package com.example.challenge.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@Entity
@Table(name = "wallet_assets")
public class WalletAsset {
    @EmbeddedId
    private WalletAssetId id;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("walletId")
    @JoinColumn(name = "wallet_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private Wallet wallet;


    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("assetId")
    @JoinColumn(name = "asset_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private Asset asset;

    @Column(nullable = false)
    private Double quantity;

    public Double getValue() {
        return quantity * asset.getPrice();
    }
}