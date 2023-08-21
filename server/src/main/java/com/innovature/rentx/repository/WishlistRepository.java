package com.innovature.rentx.repository;

import com.innovature.rentx.entity.User;
import com.innovature.rentx.entity.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
    Wishlist save(Wishlist wishlist);
    Wishlist findByProductIdAndUserIdAndStatusIn(Integer id, User userId, byte[] status);

    Optional<Wishlist> findByIdAndStatusAndUserId(Integer id,byte status, User userId);
    Page<Wishlist> findByUserIdAndStatusAndProductStatus(User userId, byte status,byte productStatus, Pageable pageable);
    int countByUserIdAndStatusAndProductStatus(User userId, byte status,byte productStatus);

}
