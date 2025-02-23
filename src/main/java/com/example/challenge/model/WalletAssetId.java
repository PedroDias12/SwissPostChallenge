package com.example.challenge.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@Embeddable
public class WalletAssetId implements Serializable {
    @Column(name = "wallet_id")
    private Long walletId;
    @Column(name = "asset_id")
    private Long assetId;
}
