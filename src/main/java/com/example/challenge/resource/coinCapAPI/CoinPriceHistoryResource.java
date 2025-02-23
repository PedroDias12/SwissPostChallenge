package com.example.challenge.resource.coinCapAPI;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoinPriceHistoryResource {
    private List<CoinDataResource> data;
}
