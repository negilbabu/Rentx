package com.innovature.rentx.repository;

import com.innovature.rentx.entity.Cart;

import org.springframework.data.domain.Page;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Set;

public interface CartRepository extends Repository<Cart, Integer> {

    Cart save(Cart cart);

    Cart findByProductIdAndStatusAndUserId(Integer id, byte status, Integer userId);

    Cart findByProductIdAndUserIdAndStatusIn(Integer id, Integer userId,byte[] status);

    List<Cart> findByUserIdAndStatus(Integer userId, byte status);

    List<Cart>findByUserIdAndIdInAndStatus(Integer userId, Set<Integer> id, byte status);
    Page<Cart>findByUserIdAndIdInAndStatus(Integer userId, Set<Integer> id, byte status,Pageable pageable);

    Page<Cart> findByUserIdAndStatus(Integer userId, byte status, Pageable pageable);

    int countByUserIdAndStatus(Integer id, byte status);

    Optional<Cart> findByIdAndStatusAndUserId(Integer id, byte status, Integer currentUserId);
}
