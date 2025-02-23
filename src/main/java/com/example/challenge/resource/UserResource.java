package com.example.challenge.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResource {
    private Long id;
    private String name;
    private String email;
    private Long walletId;
}
