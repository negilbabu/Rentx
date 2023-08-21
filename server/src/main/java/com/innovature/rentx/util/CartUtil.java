package com.innovature.rentx.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.innovature.rentx.entity.Cart;
import com.innovature.rentx.exception.BadRequestException;
import com.innovature.rentx.form.CartForm;
import com.innovature.rentx.repository.CartRepository;
import com.innovature.rentx.security.util.SecurityUtil;


@Component
public class CartUtil {

    @Autowired
    private LanguageUtil languageUtil;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private DateValidation dateValidation;

    private byte[] cartAcceptedStatus;

    private static final String UNABLE_TO_PERFORM = "unable.to.perform.this.action";

    private static final String INVALID_START_DATE = "start.date.validation";

    private static final String INVALID_END_DATE = "end.date.validation";

    public Cart validateCartId(Integer cartId) {

        Cart cart = cartRepository
                .findByIdAndStatusAndUserId(cartId, Cart.Status.ACTIVE.value, SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new BadRequestException(
                        languageUtil.getTranslatedText("invalid.cart.id", null, "en")));

        return cart;
    }

    public Boolean getProduct(Integer productId) {
        Cart cart = cartRepository.findByProductIdAndStatusAndUserId(productId, Cart.Status.ACTIVE.value,
                SecurityUtil.getCurrentUserId());
        if (cart == null) {
            return false;
        } else {
            return true;
        }
    }

    public Cart validateItemExists(CartForm form) {

        cartAcceptedStatus = new byte[] { Cart.Status.ACTIVE.value, Cart.Status.INACTIVE.value,
                Cart.Status.DELETED.value };

        Cart cart = cartRepository.findByProductIdAndUserIdAndStatusIn(form.getProductId(),
                SecurityUtil.getCurrentUserId(),
                cartAcceptedStatus);

        if (cart != null) {

            switch (cart.getStatus()) {

                case 0 -> {
                    throw new BadRequestException(languageUtil.getTranslatedText("cart.product.exist", null, "en"));

                }
                case 1 -> {
                    throw new BadRequestException(languageUtil.getTranslatedText("cart.product.exist", null, "en"));

                }
                case 2 -> {             

                    Date startDate = dateValidation.stringToDateFormat(form.getStartDate(), INVALID_START_DATE);
                    Date endDate = dateValidation.stringToDateFormat(form.getEndDate(), INVALID_END_DATE);

                    cart.setStartDate(startDate);
                    cart.setEndDate(endDate);
                    cart.setQuantity(form.getQuantity());
                    cart.setStatus(Cart.Status.ACTIVE.value);
                    return cart;

                }
                default ->
                    throw new BadRequestException(languageUtil.getTranslatedText(UNABLE_TO_PERFORM, null, "en"));

            }
        }
        return null;
    }

}
