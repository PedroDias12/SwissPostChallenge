package com.example.challenge.controller;

import com.example.challenge.exception.ServiceException;
import com.example.challenge.exception.UserNotFoundException;
import com.example.challenge.exception.WalletConflictException;
import com.example.challenge.exception.WalletNotFoundException;
import com.example.challenge.resource.WalletPerformanceRequestResource;
import com.example.challenge.resource.WalletPerformanceResponseResource;
import com.example.challenge.resource.WalletResource;
import com.example.challenge.service.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@AllArgsConstructor
@RestController
@RequestMapping("/wallets")
public class WalletController {
    private final WalletService walletService;

    @GetMapping("/{id}")
    public ResponseEntity<WalletResource> getWallet(@PathVariable final Long id, @RequestParam(required = false) boolean details) throws WalletNotFoundException {
        WalletResource resource = walletService.getWalletResourceById(id, details);
        return ResponseEntity.ok(resource);
    }

    @PostMapping
    public ResponseEntity<Long> createWallet(@RequestBody String email) throws UserNotFoundException, WalletConflictException {
        return ResponseEntity.status(HttpStatus.CREATED).body(walletService.createWallet(email));
    }

    @PostMapping("/performance")
    public ResponseEntity<WalletPerformanceResponseResource> getWalletPerformance(@RequestBody WalletPerformanceRequestResource walletPerformanceRequestResource,
                                                                                  @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws ServiceException {
        return ResponseEntity.ok(walletService.calculatePerformance(walletPerformanceRequestResource, date));
    }
}
