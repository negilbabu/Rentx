package com.innovature.rentx.controller;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovature.rentx.entity.Cart;
import com.innovature.rentx.entity.Product;
import com.innovature.rentx.entity.User;
import com.innovature.rentx.form.CartForm;
import com.innovature.rentx.service.CartService;
import com.innovature.rentx.view.UserCartView;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {CartController.class})
@ExtendWith(SpringExtension.class)
public class CartControllerTest {


    @MockBean
    private CartService cartService;

    @Autowired
    private CartController cartController;

    /**
     * Method under test: {@link CartController#cartAdd(CartForm)}
     */
    @Test
    void testCartAdd() throws Exception {
        CartForm form = new CartForm();
        Date startDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        Date endDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        Product product = new Product();
        when(cartService.save(Mockito.<CartForm>any()))
                .thenReturn(new UserCartView(new Cart(form, startDate, endDate, product, new User()), 3));

        CartForm cartForm = new CartForm();
        cartForm.setEndDate("2020-03-01");
        cartForm.setProductId(1);
        cartForm.setQuantity(1);
        cartForm.setStartDate("2020-03-01");
        String content = (new ObjectMapper()).writeValueAsString(cartForm);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"productId\":null,\"quantity\":null,\"startDate\":0,\"endDate\":0,\"cartCount\":3}"));
    }


}
