package com.innovature.rentx.repository;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.innovature.rentx.entity.VendorDetails;

public interface VendorDetailsRepository extends Repository<VendorDetails,Integer>  {
    
    VendorDetails save(VendorDetails vendor);

    Optional <VendorDetails> findByUserId(Integer currentUserId);
}
