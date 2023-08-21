package com.innovature.rentx.entity;

import com.innovature.rentx.form.CartForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class CartTest {

    private CartForm cartForm;
    private Date startDate;
    private Date endDate;
    private Product product;
    private User user;

    @BeforeEach
    public void setup() {
        cartForm = new CartForm();
        cartForm.setQuantity(2);

        startDate = new Date();
        endDate = new Date();

        product = new Product();
        user = new User();
    }

    @Test
    public void testCartConstructor() {
        Cart cart = new Cart(cartForm, startDate, endDate, product, user);

        Assertions.assertEquals(product, cart.getProduct());
        Assertions.assertEquals(user, cart.getUser());
        Assertions.assertEquals(cartForm.getQuantity(), cart.getQuantity());
        Assertions.assertEquals(startDate, cart.getStartDate());
        Assertions.assertEquals(endDate, cart.getEndDate());
    }

    @Test
    public void testSettersAndGetters() {
        Cart cart = new Cart();

        cart.setId(1);
        cart.setProduct(product);
        cart.setUser(user);
        cart.setQuantity(3);
        cart.setStartDate(startDate);
        cart.setEndDate(endDate);
        cart.setStatus((byte) 1);

        Assertions.assertEquals(1, cart.getId());
        Assertions.assertEquals(product, cart.getProduct());
        Assertions.assertEquals(user, cart.getUser());
        Assertions.assertEquals(3, cart.getQuantity());
        Assertions.assertEquals(startDate, cart.getStartDate());
        Assertions.assertEquals(endDate, cart.getEndDate());
        Assertions.assertEquals((byte) 1, cart.getStatus());
    }
}
