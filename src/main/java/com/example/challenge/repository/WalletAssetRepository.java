package com.example.challenge.repository;

import com.example.challenge.model.WalletAsset;
import com.example.challenge.model.WalletAssetId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletAssetRepository extends JpaRepository<WalletAsset, WalletAssetId> {
    List<WalletAsset> findByWalletId(Long walletId);
}