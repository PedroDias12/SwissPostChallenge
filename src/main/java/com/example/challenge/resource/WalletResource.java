package com.example.challenge.resource;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WalletResource {
    private Long id;
    private Double total;
    private String userEmail;
    private List<WalletAssetResource> assets;
}
