package com.innovature.rentx.repository;


import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.innovature.rentx.entity.Address;
import com.innovature.rentx.entity.User;
import com.innovature.rentx.view.UserAddressView;


public interface AddressRepository extends Repository<Address,Integer>{

    void save(Address address);

    Long countByUserIdAndStatus(User user, byte value);

	Collection<UserAddressView> findByUserIdAndStatus(User currentUserId, byte value);

    Optional<Address> findByIdAndUserIdAndStatus(Integer id, User user, byte value);

    Optional <Address> findByIdAndStatus(Integer addressId, byte value);
    
}
