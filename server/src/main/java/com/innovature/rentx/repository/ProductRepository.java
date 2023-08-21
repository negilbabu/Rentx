package com.innovature.rentx.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.innovature.rentx.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product save(Product product);

    Product findById(Integer id);

    Product findByStoreId(Integer currentUserId);

    Product findAllByStoreId(Integer storeId);

    boolean existsByNameAndStoreId(String name, Integer store);

    boolean existsByNameAndStatusInAndIdNot(String name,byte[] status, Integer store);

    Optional<Product> findByIdAndStatus(Integer id, byte status);

    Product findByIdAndStatusIn(Integer id, byte[] statuses);

    Collection <Product> findByStoreIdAndStatus( Integer storeId, byte value);
    List<Product>findByStoreUserIdAndStatus(Integer storeUserId,byte status);

    List <Product> findByCategoryIdAndStatusIn(Integer categoryId,byte[] status);

    List <Product> findBySubCategoryIdAndStatusIn(Integer subCategoryId,byte[] status);

}
