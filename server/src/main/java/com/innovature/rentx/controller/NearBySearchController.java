package com.innovature.rentx.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovature.rentx.entity.Store;
import com.innovature.rentx.service.NearBySearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class NearBySearchController {

    private final NearBySearchService nearBySearchService;

    @GetMapping("/nearBySearch")
    public List<Store> nearByLoactionList(
            @RequestParam(name = "latitude", required = true) Double latitude,
            @RequestParam(name = "longitude", required = true) Double longitude,
            @RequestParam(name = "radius", required = true) Double radius) {

        return nearBySearchService.userProductDetailView(latitude, longitude, radius);
    }

}
