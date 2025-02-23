package com.example.challenge.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WalletPerformanceRequestResource {
    private List<WalletAssetPerformanceResource> assets;

    @Data
    public static class WalletAssetPerformanceResource {
        private String symbol;
        private Double quantity;
        private Double value;
    }
}
