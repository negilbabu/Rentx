// package com.innovature.rentx.controller;

// import static org.mockito.Mockito.when;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.innovature.rentx.form.ChangePasswordForm;
// import com.innovature.rentx.form.EmailForm;
// import com.innovature.rentx.form.OtpForm;
// import com.innovature.rentx.service.UserService;
// import com.innovature.rentx.view.EmailTokenView;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.ContextConfiguration;
// import org.springframework.test.context.junit.jupiter.SpringExtension;
// import org.springframework.test.web.servlet.ResultActions;
// import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// @ContextConfiguration(classes = {ForgetContoller.class})
// @ExtendWith(SpringExtension.class)
// class ForgetContollerTest {
//     @Autowired
//     private ForgetContoller forgetContoller;

//     @MockBean
//     private UserService userService;

 
//     @Test
//     void testChangePassword() throws Exception {
//         ChangePasswordForm changePasswordForm = new ChangePasswordForm();
//         changePasswordForm.setEmailToken("ABC123");
//         changePasswordForm.setPassword("Test123");
//         String content = (new ObjectMapper()).writeValueAsString(changePasswordForm);
//         MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/changePassword")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(content);
//         ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(forgetContoller)
//                 .build()
//                 .perform(requestBuilder);
//         actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
//     }

 
//     @Test
//     void testForgetemail() throws Exception {
//         when(userService.forgetPasswordEmail(Mockito.<EmailForm>any())).thenReturn(new EmailTokenView("Msg", "ABC123"));

//         EmailForm emailForm = new EmailForm();
//         emailForm.setEmail("jane.doe@example.org");
//         String content = (new ObjectMapper()).writeValueAsString(emailForm);
//         MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/forgetPassword")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(content);
//         MockMvcBuilders.standaloneSetup(forgetContoller)
//                 .build()
//                 .perform(requestBuilder)
//                 .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                 .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"Msg\",\"emailToken\":\"ABC123\"}"));
//     }

   
//     @Test
//     void testForgetverify() throws Exception {
//         when(userService.forgetVerify(Mockito.<OtpForm>any())).thenReturn(new EmailTokenView("Msg", "ABC123"));

//         OtpForm otpForm = new OtpForm();
//         otpForm.setEmailToken("ABC123");
//         otpForm.setOtp("Otp");
//         String content = (new ObjectMapper()).writeValueAsString(otpForm);
//         MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/otp/verify/forgetpassword")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(content);
//         MockMvcBuilders.standaloneSetup(forgetContoller)
//                 .build()
//                 .perform(requestBuilder)
//                 .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                 .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"Msg\",\"emailToken\":\"ABC123\"}"));
//     }
// }

