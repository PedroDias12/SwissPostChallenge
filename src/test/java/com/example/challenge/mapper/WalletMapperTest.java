package com.example.challenge.mapper;

import com.example.challenge.model.Asset;
import com.example.challenge.model.User;
import com.example.challenge.model.Wallet;
import com.example.challenge.model.WalletAsset;
import com.example.challenge.resource.WalletAssetResource;
import com.example.challenge.resource.WalletResource;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.example.challenge.TestContants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WalletMapperTest {
    private final Wallet ENTITY = Wallet.builder()
            .id(1L)
            .user(User.builder().email(USER_EMAIL).build())
            .walletAssets(List.of(
                    WalletAsset.builder().quantity(2.5).asset(Asset.builder().price(10.0).symbol(SYMBOL1).build()).build(),
                    WalletAsset.builder().quantity(1.5).asset(Asset.builder().price(20.0).symbol(SYMBOL2).build()).build()))
            .build();
    private final WalletMapper walletMapper = Mappers.getMapper(WalletMapper.class);

    @Test
    void testWalletToResource() {
        WalletResource expected = new WalletResource();
        expected.setId(1L);
        expected.setUserEmail(USER_EMAIL);

        WalletResource walletResource = walletMapper.walletToResource(ENTITY);

        assertNotNull(walletResource);
        assertEquals(expected, walletResource);
    }

    @Test
    void testWalletToResourceWithDetails() {
        WalletResource expected = getWalletResource();

        WalletResource walletResource = walletMapper.walletToResourceWithDetails(ENTITY);

        assertNotNull(walletResource);
        assertEquals(expected, walletResource);
    }

    private static WalletResource getWalletResource() {
        WalletResource expected = new WalletResource();
        expected.setId(1L);
        expected.setTotal(55.0);
        WalletAssetResource walletAssetResource1 = new WalletAssetResource();
        walletAssetResource1.setPrice(new BigDecimal("10.00"));
        walletAssetResource1.setQuantity(new BigDecimal("2.5"));
        walletAssetResource1.setValue(new BigDecimal("25.00"));
        walletAssetResource1.setSymbol(SYMBOL1);
        WalletAssetResource walletAssetResource2 = new WalletAssetResource();
        walletAssetResource2.setPrice(new BigDecimal("20.00"));
        walletAssetResource2.setQuantity(new BigDecimal("1.5"));
        walletAssetResource2.setValue(new BigDecimal("30.00"));
        walletAssetResource2.setSymbol(SYMBOL2);
        expected.setAssets(List.of(walletAssetResource1, walletAssetResource2));
        return expected;
    }

    @Test
    void testWalletAssetToWalletAssetResource() {
        WalletAsset walletAsset = WalletAsset.builder()
                .wallet(Wallet.builder().id(1L).build())
                .asset(Asset.builder().symbol(SYMBOL1).price(10.5).build())
                .quantity(5.5)
                .build();
        WalletAssetResource expected = new WalletAssetResource();
        expected.setQuantity(BigDecimal.valueOf(walletAsset.getQuantity()));
        expected.setSymbol(walletAsset.getAsset().getSymbol());
        expected.setPrice(BigDecimal.valueOf(walletAsset.getAsset().getPrice()).setScale(2, RoundingMode.FLOOR));
        expected.setValue(BigDecimal.valueOf(walletAsset.getValue()));

        WalletAssetResource walletAssetResource = walletMapper.walletAssetToWalletAssetResource(walletAsset);

        assertNotNull(walletAssetResource);
        assertEquals(expected, walletAssetResource);
    }
}
