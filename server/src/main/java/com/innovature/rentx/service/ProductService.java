package com.innovature.rentx.service;

import javax.validation.Valid;

import com.innovature.rentx.view.*;
import org.springframework.http.ResponseEntity;

import com.innovature.rentx.form.ImageProductForm;
import com.innovature.rentx.form.ProductForm;
import com.innovature.rentx.util.Pager;

public interface ProductService {

    ProductView createProduct(ProductForm form);

    Pager<UserProductView> viewProduct(String searchData, Integer page, Integer limit, String sort, String order,
            String CategoryId, String StoreId, String startDate, String endDate, Integer quantity,Double latitude,Double longitude);

    Pager<UserProductView> vendorViewProduct(String searchData, Integer page, Integer limit, String sort, String order,
            String CategoryId, String StoreId);

    ResponseEntity<String> addImageProduct(@Valid ImageProductForm form);

    ResponseEntity<String> deleteProduct(Integer productId);

    ProductDetailView detailView(Integer productId);

    UserProductDetailView userProductDetailView(Integer productId, String startDate, String endDate, Integer quantity);

    UserAvailabilityView availabilityCheck(Integer productId,String startDate, String endDate, Integer quantity);

    ResponseEntity<String> updateProduct(Integer productId, ProductForm form);

    ResponseEntity<String> updateProductImage(ImageProductForm form);


}
