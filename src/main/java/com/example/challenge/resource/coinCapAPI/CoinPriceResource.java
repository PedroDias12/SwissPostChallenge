package com.example.challenge.resource.coinCapAPI;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoinPriceResource {
    private CoinDataResource data;
}
