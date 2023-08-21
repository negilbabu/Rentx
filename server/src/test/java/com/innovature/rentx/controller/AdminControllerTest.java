// package com.innovature.rentx.controller;

// import static org.mockito.Mockito.when;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.innovature.rentx.form.AdminForm;
// import com.innovature.rentx.form.ChangePasswordForm;
// import com.innovature.rentx.form.EmailForm;
// import com.innovature.rentx.form.LoginForm;
// import com.innovature.rentx.form.OtpForm;
// import com.innovature.rentx.form.RefreshTokenForm;
// import com.innovature.rentx.service.AdminService;
// import com.innovature.rentx.util.Pager;
// import com.innovature.rentx.view.EmailTokenView;
// import com.innovature.rentx.view.RefreshTokenView;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.test.context.ContextConfiguration;
// import org.springframework.test.context.junit.jupiter.SpringExtension;
// import org.springframework.test.web.servlet.ResultActions;
// import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// @ContextConfiguration(classes = {AdminController.class})
// @ExtendWith(SpringExtension.class)
// class AdminControllerTest {
//     @Autowired
//     private AdminController adminController;

//     @MockBean
//     private AdminService adminService;

//     @Test
//     void testLogin() throws Exception {
//         when(adminService.login(Mockito.<LoginForm>any())).thenReturn(null);

//         LoginForm loginForm = new LoginForm();
//         loginForm.setEmail("jane.doe@example.org");
//         loginForm.setPassword("iloveyou");
//         String content = (new ObjectMapper()).writeValueAsString(loginForm);
//         MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/admin/login")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(content);
//         MockMvcBuilders.standaloneSetup(adminController)
//                 .build()
//                 .perform(requestBuilder)
//                 .andExpect(MockMvcResultMatchers.status().isOk());
//     }

//     @Test
//     void testRefresh() throws Exception {
//         RefreshTokenView.TokenView accessToken = new RefreshTokenView.TokenView("42", 1L);

//         when(adminService.refresh(Mockito.<RefreshTokenForm>any()))
//                 .thenReturn(new RefreshTokenView(accessToken, new RefreshTokenView.TokenView("42", 1L)));

//         RefreshTokenForm refreshTokenForm = new RefreshTokenForm();
//         refreshTokenForm.setRefreshToken("ABC123");
//         String content = (new ObjectMapper()).writeValueAsString(refreshTokenForm);
//         MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/admin/login")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(content);
//         MockMvcBuilders.standaloneSetup(adminController)
//                 .build()
//                 .perform(requestBuilder)
//                 .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                 .andExpect(MockMvcResultMatchers.content()
//                         .string(
//                                 "{\"accessToken\":{\"value\":\"42\",\"expiry\":\"1970-01-01 05:30:00\"},\"refreshToken\":{\"value\":\"42\",\"expiry\":"
//                                         + "\"1970-01-01 05:30:00\"}}"));
//     }

//     @Test
//     void testApproveReject() throws Exception {
//         when(adminService.approveReject(Mockito.<Integer>any(), Mockito.<Integer>any()))
//                 .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
//         MockHttpServletRequestBuilder putResult = MockMvcRequestBuilders.put("/admin/vendor/approve/{id}", 1);
//         MockHttpServletRequestBuilder requestBuilder = putResult.param("btnValue", String.valueOf(1));
//         ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(adminController)
//                 .build()
//                 .perform(requestBuilder);
//         actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
//     }

//     @Test
//     void testAdd() throws Exception {
//         AdminForm adminForm = new AdminForm();
//         adminForm.setEmail("jane.doe@example.org");
//         adminForm.setPassword("iloveyou");
//         String content = (new ObjectMapper()).writeValueAsString(adminForm);
//         MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/admin/add")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(content);
//         ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(adminController)
//                 .build()
//                 .perform(requestBuilder);
//         actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
//     }

//     @Test
//     void testChangePassword() throws Exception {
//         ChangePasswordForm changePasswordForm = new ChangePasswordForm();
//         changePasswordForm.setEmailToken("ABC123");
//         changePasswordForm.setPassword("iloveyou");
//         String content = (new ObjectMapper()).writeValueAsString(changePasswordForm);
//         MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/admin/changePassword")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(content);
//         ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(adminController)
//                 .build()
//                 .perform(requestBuilder);
//         actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
//     }

//     @Test
//     void testForgetemail() throws Exception {
//         when(adminService.forgetPasswordEmail(Mockito.<EmailForm>any())).thenReturn(new EmailTokenView("Msg", "ABC123"));

//         EmailForm emailForm = new EmailForm();
//         emailForm.setEmail("jane.doe@example.org");
//         String content = (new ObjectMapper()).writeValueAsString(emailForm);
//         MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/admin/forgetPassword")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(content);
//         MockMvcBuilders.standaloneSetup(adminController)
//                 .build()
//                 .perform(requestBuilder)
//                 .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                 .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"Msg\",\"emailToken\":\"ABC123\"}"));
//     }

//     @Test
//     void testForgetverify() throws Exception {
//         when(adminService.forgetVerify(Mockito.<OtpForm>any())).thenReturn(new EmailTokenView("Msg", "ABC123"));

//         OtpForm otpForm = new OtpForm();
//         otpForm.setEmailToken("ABC123");
//         otpForm.setOtp("Otp");
//         String content = (new ObjectMapper()).writeValueAsString(otpForm);
//         MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/admin/otp/verify/forgetpassword")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(content);
//         MockMvcBuilders.standaloneSetup(adminController)
//                 .build()
//                 .perform(requestBuilder)
//                 .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                 .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"Msg\",\"emailToken\":\"ABC123\"}"));
//     }

//     @Test
//     void testUserBlockUnblock() throws Exception {
//         when(adminService.UserBlockUnblock(Mockito.<Integer>any(), Mockito.<Integer>any()))
//                 .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
//         MockHttpServletRequestBuilder putResult = MockMvcRequestBuilders.put("/admin/user/manageUser/{id}", 1);
//         MockHttpServletRequestBuilder requestBuilder = putResult.param("btnValue", String.valueOf(1));
//         ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(adminController)
//                 .build()
//                 .perform(requestBuilder);
//         actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
//     }

//     @Test
//     void testUserList() throws Exception {
//         when(adminService.listUser(Mockito.<String>any(), Mockito.<Integer>any(), Mockito.<Integer>any(),
//                 Mockito.<String>any(), Mockito.<String>any())).thenReturn(new Pager<>(3, 1));
//         MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/user/list")
//                 .param("order", "foo")
//                 .param("sort", "foo");
//         MockMvcBuilders.standaloneSetup(adminController)
//                 .build()
//                 .perform(requestBuilder)
//                 .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                 .andExpect(MockMvcResultMatchers.content()
//                         .string(
//                                 "{\"currentPage\":1,\"numItems\":1,\"pageSize\":3,\"startIndex\":0,\"displayCount\":10,\"displayStart\":1,\"displayEnd"
//                                         + "\":1,\"itemStart\":1,\"itemEnd\":1,\"result\":[],\"firstPage\":1,\"previousPage\":1,\"nextPage\":1,\"lastPage\":1,"
//                                         + "\"hasNext\":false,\"hasPrevious\":false,\"pagerInfo\":{\"count\":1,\"page\":1,\"prevPage\":1,\"hasPrev\":false,"
//                                         + "\"pagerStart\":1,\"pagerEnd\":1,\"itemStart\":1,\"firstPage\":1,\"nextPage\":1,\"lastPage\":1,\"itemEnd\":1,\"hasNext"
//                                         + "\":false,\"limit\":3}}"));
//     }

//     @Test
//     void testVendorList() throws Exception {
//         when(adminService.listVendor(Mockito.<String>any(), Mockito.<Integer>any(), Mockito.<Integer>any(),
//                 Mockito.<String>any(), Mockito.<String>any(), Mockito.<Integer>any())).thenReturn(new Pager<>(3, 1));
//         MockHttpServletRequestBuilder paramResult = MockMvcRequestBuilders.get("/admin/vendor/list")
//                 .param("order", "foo")
//                 .param("sort", "foo");
//         MockHttpServletRequestBuilder requestBuilder = paramResult.param("statusValue", String.valueOf(1));
//         MockMvcBuilders.standaloneSetup(adminController)
//                 .build()
//                 .perform(requestBuilder)
//                 .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                 .andExpect(MockMvcResultMatchers.content()
//                         .string(
//                                 "{\"currentPage\":1,\"numItems\":1,\"pageSize\":3,\"startIndex\":0,\"displayCount\":10,\"displayStart\":1,\"displayEnd"
//                                         + "\":1,\"itemStart\":1,\"itemEnd\":1,\"result\":[],\"firstPage\":1,\"previousPage\":1,\"nextPage\":1,\"lastPage\":1,"
//                                         + "\"hasNext\":false,\"hasPrevious\":false,\"pagerInfo\":{\"count\":1,\"page\":1,\"prevPage\":1,\"hasPrev\":false,"
//                                         + "\"pagerStart\":1,\"pagerEnd\":1,\"itemStart\":1,\"firstPage\":1,\"nextPage\":1,\"lastPage\":1,\"itemEnd\":1,\"hasNext"
//                                         + "\":false,\"limit\":3}}"));
//     }
// }

