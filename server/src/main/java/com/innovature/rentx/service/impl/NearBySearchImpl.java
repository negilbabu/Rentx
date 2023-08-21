package com.innovature.rentx.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.innovature.rentx.entity.Store;
import com.innovature.rentx.repository.StoreRepository;
import com.innovature.rentx.service.NearBySearchService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NearBySearchImpl implements NearBySearchService {

    private final StoreRepository storeRepository;

    private static final double EARTH_RADIUS = 6371;

    @Override
    public List<Store> userProductDetailView(Double latitude, Double longitude, Double radius) {
        List<Store> allStores = storeRepository.findByStatus(Store.Status.ACTIVE.value);
        List<Store> nearbyStores = new ArrayList<>();

        for (Store store : allStores) {
            double storeLatitude = Double.parseDouble(store.getLattitude());
            double storeLongitude = Double.parseDouble(store.getLongitude());

            double distance = calculateDistance(latitude, longitude, storeLatitude, storeLongitude);

            if (distance <= radius) {
                nearbyStores.add(store);
            }
        }

        // Sort the nearbyStores list based on the distance dynamically during the
        // sorting process
        nearbyStores.sort((store1, store2) -> {
            double distance1 = calculateDistance(latitude, longitude, Double.parseDouble(store1.getLattitude()),
                    Double.parseDouble(store1.getLongitude()));
            double distance2 = calculateDistance(latitude, longitude, Double.parseDouble(store2.getLattitude()),
                    Double.parseDouble(store2.getLongitude()));
            return Double.compare(distance1, distance2);
        });

        return nearbyStores;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

}
