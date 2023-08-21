package com.innovature.rentx.service;

import java.util.List;

import com.innovature.rentx.entity.Store;

public interface NearBySearchService {

    List<Store> userProductDetailView(Double latitude, Double longitude, Double radius);
    
}
