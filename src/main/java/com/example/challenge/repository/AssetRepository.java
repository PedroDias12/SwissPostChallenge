package com.example.challenge.repository;

import com.example.challenge.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    Optional<Asset> findByToken(String token);
    Optional<Asset> findBySymbol(String symbol);
}
