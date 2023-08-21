package com.innovature.rentx.util;

import com.innovature.rentx.entity.*;
import com.innovature.rentx.form.ImageProductForm;
import com.innovature.rentx.repository.ImageProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.innovature.rentx.exception.BadRequestException;
import com.innovature.rentx.form.ProductForm;
import com.innovature.rentx.repository.ProductRepository;
import com.innovature.rentx.repository.StoreRepository;
import com.innovature.rentx.security.util.SecurityUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ProductUtil {

    @Autowired
    private LanguageUtil languageUtil;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
            private ImageProductRepository imageProductRepository;
    byte[] productStatus = { Product.Status.ACTIVE.value ,Product.Status.INACTIVE.value};

    public Store storeValidate(Integer id, byte[] StoreStatuses) {
        return storeRepository
                .findByIdAndUserIdAndStatusIn(id, SecurityUtil.getCurrentUserId(),
                        StoreStatuses)
                .orElseThrow(
                        () -> new BadRequestException(
                                languageUtil.getTranslatedText("invalid.store.id", null, "en")));
    }


    public Store validateIfVendorOwnProduct(Integer id, byte[] StoreStatuses) {
        return storeRepository
                .findByIdAndUserIdAndStatusIn(id, SecurityUtil.getCurrentUserId(),
                        StoreStatuses)
                .orElseThrow(
                        () -> new BadRequestException(
                                languageUtil.getTranslatedText("invalid.store.id", null, "en")));
    }

    public Product validateProduct(Integer productId) {

        return productRepository.findByIdAndStatus(productId, Product.Status.ACTIVE.value)
                .orElseThrow(() -> new BadRequestException(
                        languageUtil.getTranslatedText("invalid.product.id", null, "en")));
    }

    public void validateQuantity(Product product, Integer quantity) {

        if (quantity > product.getAvailableStock()) {
            throw new BadRequestException(languageUtil.getTranslatedText("cart.product.quantity.range", null, "en"));

        } else if (quantity < 1) {
            throw new BadRequestException(languageUtil.getTranslatedText("invalid.quantity", null, "en"));
        }

    }

    public void idMatcher(String id, String msg) {
        String pattern = "^(null|,|[0-9]+(,[0-9]+)*)$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(id);
        if (!matcher.matches()) {
            throw new BadRequestException(languageUtil.getTranslatedText(msg, null, "en"));
        }
    }
public void checkProductNameExist(Integer productId,String storeName){
    Boolean productExists =  productRepository.existsByNameAndStatusInAndIdNot(storeName,productStatus,productId);
    if(Boolean.TRUE.equals(productExists)){
        throw new BadRequestException(languageUtil.getTranslatedText("Product-Name.already.exists", null, "en"));
    }
}
    public Product saveProduct(Integer productId, ProductForm form, Category category, SubCategory subCategory,
            Store store) {

        try {
            Product product = productRepository.findByIdAndStatusIn(productId, productStatus);

            product.setName(form.getName());
            product.setCategory(category);
            product.setSubCategory(subCategory);
            product.setStore(store);
            product.setDescription(form.getDescription());
            product.setSpecification(form.getSpecification());
            product.setStock(form.getStock());
            product.setAvailableStock(form.getStock());
            product.setCoverImage(form.getCoverImage());
            product.setThumbnail(form.getThumbnail());

           productRepository.save(product);
            return product;
        } catch (Exception e) {
            throw new BadRequestException(languageUtil.getTranslatedText("invalid.product.id", null, "en"));
        }
    }

    public void imageTwoExists(ImageProduct image, ImageProductForm form){
        image.setImage2(form.getImage2());
        imageProductRepository.save(image);
    }
    public void imageThreeExists(ImageProduct image, ImageProductForm form){
        image.setImage3(form.getImage3());
        imageProductRepository.save(image);

    }

    public void imageFourExists(ImageProduct image, ImageProductForm form){
        image.setImage4(form.getImage4());
        imageProductRepository.save(image);

    }

    public  void setProductStatusUponBlockAndUnBlock(Integer userId,byte status,byte newStatus){
        List<Product> productList=productRepository.findByStoreUserIdAndStatus(userId,status);
        if(productList!=null) {

            for (Product product : productList) {
                product.setStatus(newStatus);
                productRepository.save(product);
            }
        }
    }
     public double calculateVat(double grandTotal){
         double vatPrice = 10.0;
         return grandTotal / vatPrice;
     }




}
