package com.example.challenge.controller;

import com.example.challenge.exception.ServiceException;
import com.example.challenge.resource.AssetResource;
import com.example.challenge.service.AssetService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/assets")
public class AssetController {
    private final AssetService assetService;

    @GetMapping()
    public ResponseEntity<List<AssetResource>> getAssets(){
        List<AssetResource> resources = assetService.getAssetsResources();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<AssetResource> getAsset(@PathVariable final String symbol) throws ServiceException {
        AssetResource resource = assetService.getAssetResourceBySymbol(symbol);
        return ResponseEntity.ok(resource);
    }
}
