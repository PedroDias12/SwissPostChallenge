package com.example.challenge.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "assets")
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String token;
    @Column(unique = true)
    private String symbol;
    @Column
    private Double price;

    @OneToMany(mappedBy = "asset", fetch = FetchType.LAZY)
    private List<WalletAsset> walletAssets;
}
