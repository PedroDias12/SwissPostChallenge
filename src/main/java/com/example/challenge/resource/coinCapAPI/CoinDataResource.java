package com.example.challenge.resource.coinCapAPI;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoinDataResource {
    private String id;
    private String symbol;
    private String priceUsd;
}
