package com.example.challenge.service;

import com.example.challenge.exception.UserNotFoundException;
import com.example.challenge.exception.WalletConflictException;
import com.example.challenge.exception.WalletNotFoundException;
import com.example.challenge.mapper.WalletMapper;
import com.example.challenge.model.User;
import com.example.challenge.model.Wallet;
import com.example.challenge.repository.WalletRepository;
import com.example.challenge.resource.WalletResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.challenge.TestContants.USER_EMAIL;
import static com.example.challenge.TestContants.USER_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private WalletMapper walletMapper;
    @Mock
    private UserService userService;
    @Mock
    private CoinCapService coinCapService;
    @Mock
    private AssetService assetService;
    @InjectMocks
    private WalletService walletService;
    private final User user = User.builder().id(1L).email(USER_EMAIL).name(USER_NAME).build();
    private final Wallet wallet = Wallet.builder().id(WALLET_ID).user(user).build();

    private static final Long WALLET_ID = 1L;

    @Test
    void testCreateWallet() throws UserNotFoundException, WalletConflictException {
        when(userService.getUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        Long walletId = walletService.createWallet(USER_EMAIL);

        assertEquals(WALLET_ID, walletId);
    }

    @Test
    void testCreateWalletUserNotFound() {
        when(userService.getUserByEmail(USER_EMAIL)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> walletService.createWallet(USER_EMAIL));
    }

    @Test
    void testCreateWalletWalletConflict() throws UserNotFoundException, WalletConflictException {
        when(userService.getUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user.toBuilder().wallet(wallet).build()));
        assertThrows(WalletConflictException.class, () -> walletService.createWallet(USER_EMAIL));
    }

    @Test
    void testGetWalletResourceById() throws WalletNotFoundException {
        WalletResource walletResource = new WalletResource();
        walletResource.setId(WALLET_ID);
        walletResource.setUserEmail(USER_EMAIL);

        when(walletRepository.findById(WALLET_ID)).thenReturn(Optional.of(wallet));
        when(walletMapper.walletToResource(any(Wallet.class))).thenReturn(walletResource);

        WalletResource result = walletService.getWalletResourceById(WALLET_ID, false);

        assertNotNull(result);
        assertEquals(walletResource, result);
    }

    @Test
    void testGetWalletResourceByIdNotFound() {
        when(walletRepository.findById(WALLET_ID)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> walletService.getWalletResourceById(WALLET_ID, false));
    }
}
