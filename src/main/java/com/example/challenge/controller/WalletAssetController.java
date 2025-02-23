package com.example.challenge.controller;

import com.example.challenge.exception.ServiceException;
import com.example.challenge.resource.WalletAssetResource;
import com.example.challenge.service.WalletAssetService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/wallets/{wallet_id}/assets")
public class WalletAssetController {
    private WalletAssetService walletAssetService;

    @PutMapping
    public ResponseEntity<Void> addAsset(@PathVariable(name = "wallet_id") Long walletId, @RequestBody WalletAssetResource resource) throws ServiceException {
        resource.setWalletId(walletId);
        walletAssetService.addAssetToWallet(resource);
        return ResponseEntity.ok().build();
    }
}
