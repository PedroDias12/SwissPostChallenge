package com.example.challenge.service;

import com.example.challenge.exception.ServiceException;
import com.example.challenge.exception.UserNotFoundException;
import com.example.challenge.exception.WalletConflictException;
import com.example.challenge.exception.WalletNotFoundException;
import com.example.challenge.mapper.WalletMapper;
import com.example.challenge.model.User;
import com.example.challenge.model.Wallet;
import com.example.challenge.repository.WalletRepository;
import com.example.challenge.resource.WalletPerformanceRequestResource;
import com.example.challenge.resource.WalletPerformanceResponseResource;
import com.example.challenge.resource.WalletResource;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Service
public class WalletService {
    private final WalletMapper walletMapper;
    private final WalletRepository walletRepository;
    private final UserService userService;
    private final CoinCapService coinCapService;
    private final AssetService assetService;

    public Long createWallet(String email) throws UserNotFoundException, WalletConflictException {
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));;
        if (user.getWallet() != null) {
            throw new WalletConflictException("Wallet with email " + email + " already created!");
        }
        return walletRepository.save(Wallet.builder().user(user).build())
                .getId();
    }

    public WalletResource getWalletResourceById(Long id, boolean details) throws WalletNotFoundException {
        Wallet wallet = getById(id);
        if (details) {
            return walletMapper.walletToResourceWithDetails(wallet);
        }
        return walletMapper.walletToResource(wallet);
    }

    public Wallet getById(Long id) throws WalletNotFoundException {
        return walletRepository.findById(id).orElseThrow(() -> new WalletNotFoundException("Wallet with id " + id + " not found"));
    }

    public WalletPerformanceResponseResource calculatePerformance(WalletPerformanceRequestResource walletPerformanceRequestResource, LocalDate date) throws ServiceException {
        double totalValue = 0.0;
        String bestAsset = Strings.EMPTY;
        double bestPerformance = -Double.MAX_VALUE;
        String worstAsset = Strings.EMPTY;
        double worstPerformance = Double.MAX_VALUE;

        for (WalletPerformanceRequestResource.WalletAssetPerformanceResource asset : walletPerformanceRequestResource.getAssets()) {
            String token = assetService.getAssetBySymbol(asset.getSymbol()).getToken();
            double historicalPrice = coinCapService.getHistoricalPrice(token, date);
            double performance = calculatePerformance(asset.getValue()/asset.getQuantity(), historicalPrice);

            if (performance > bestPerformance) {
                bestPerformance = performance;
                bestAsset = asset.getSymbol();
            }
            if (performance < worstPerformance) {
                worstPerformance = performance;
                worstAsset = asset.getSymbol();
            }
            totalValue += asset.getQuantity() * historicalPrice;
        }
        return WalletPerformanceResponseResource.builder()
                .total(BigDecimal.valueOf(totalValue).setScale(2, java.math.RoundingMode.FLOOR))
                .bestAsset(bestAsset)
                .bestPerformance(BigDecimal.valueOf(bestPerformance).setScale(2, java.math.RoundingMode.FLOOR))
                .worstAsset(worstAsset.equals(bestAsset) ? null : worstAsset)
                .worstPerformance(worstAsset.equals(bestAsset) ? null : BigDecimal.valueOf(worstPerformance).setScale(2, java.math.RoundingMode.FLOOR))
                .build();
    }

    private double calculatePerformance(double initialValue, double historicalValue) {
        return ((historicalValue - initialValue) / initialValue) * 100;
    }
}
