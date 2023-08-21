
package com.innovature.rentx.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.innovature.rentx.entity.*;
import com.innovature.rentx.exception.BadRequestException;
import com.innovature.rentx.exception.ConflictException;
import com.innovature.rentx.form.*;
import com.innovature.rentx.repository.AdminRepository;
import com.innovature.rentx.repository.OtpRepository;
import com.innovature.rentx.repository.UserRepository;
import com.innovature.rentx.security.config.SecurityConfig;
import com.innovature.rentx.security.util.InvalidTokenException;
import com.innovature.rentx.security.util.SecurityUtil;
import com.innovature.rentx.security.util.TokenExpiredException;
import com.innovature.rentx.security.util.TokenGenerator;
import com.innovature.rentx.security.util.TokenGenerator.Status;
import com.innovature.rentx.security.util.TokenGenerator.Token;
import com.innovature.rentx.service.UserService;
import com.innovature.rentx.util.LanguageUtil;
import com.innovature.rentx.util.NewEmailUtil;
import com.innovature.rentx.util.UserUtil;
import com.innovature.rentx.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.validation.Valid;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import static com.innovature.rentx.security.AccessTokenUserDetailsService.PURPOSE_ACCESS_TOKEN;

@Service
public class UserServiceImpl implements UserService {

    private static final String PURPOSE_REFRESH_TOKEN = "REFRESH_TOKEN";

    private static final String PURPOSE_EMAIL_TOKEN = "EMAIL_TOKEN";

    private static final String PURPOSE_FORGET_TOKEN = "FORGET_TOKEN";

    private static final String PURPOSE_VENDOR_REG_STAGE_1 = "PURPOSE_VENDOR_REG_STAGE_1";

    private static final String PURPOSE_VENDOR_REG_STAGE_2 = "PURPOSE_VENDOR_REG_STAGE_2";

    public static final String SUCCESSFULLY_CREATED = "successfully created";

    public static final String OTP_VERIFIED = "otp Verified successfully";

    private static final String EMAIL_ERROR = "email.password.incorrect";

    private static final String DIGIT_FORMAT = "%10d";

    private static final String PURPOSE_CHANGE_PASSWORD = "CHANGE_PASSWORD";

    private static final String EMAIL_FOUND_ERROR = "email.found.error";

    private static final String UNABLE_TO_PERFORM = "unable.to.perform.this.action";

    private static final String OTP_TEMPLATE = "OtpTemplate.html";


    @Autowired
    private LanguageUtil languageUtil;

    @Autowired
    private NewEmailUtil newEmailUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private UserUtil userUtil;

    Random random1 = new Random();

    @Value("${google.id}")
    private String googleId;

    @Value("${purpose.token.expiry}")
    private Integer ex;

    @Value("${otp.expiry}")
    private Integer otpExp;

    @Override
    public LoginView login(LoginForm form) throws BadRequestException {

        User user = userRepository.findByEmail(form.getEmail()).orElseThrow(
                () -> new BadRequestException(languageUtil.getTranslatedText(EMAIL_FOUND_ERROR, null, "en")));

        userUtil.verifyUserLogin(user, form);

        if (user.getStatus() == User.Status.ACTIVE.value
                || user.getRole() == User.Role.VENDOR.value && user.getStatus() == User.Status.BLOCKED.value) {

            return userUtil.generateTokenForActiveUser(user);

        } else if (user.getRole() == User.Role.VENDOR.value && user.getStatus() == User.Status.VERIFIED.value) {

            TokenGenerator.Token emailToken = userUtil.generatePurposeToken(user, PURPOSE_VENDOR_REG_STAGE_1);
            return new LoginView(user, emailToken.value);

        } else if (user.getStatus() == User.Status.STAGE1.value) {

            TokenGenerator.Token emailToken = userUtil.generatePurposeToken(user, PURPOSE_VENDOR_REG_STAGE_2);
            return new LoginView(user, emailToken.value);

        } else if (user.getStatus() == User.Status.STAGE2.value) {
            return new LoginView(user);

        }
        throw new BadRequestException(languageUtil.getTranslatedText(EMAIL_ERROR, null, "en"));

    }

    @Override
    public RefreshTokenView refresh(RefreshTokenForm form) throws BadRequestException {
        Status status;
        try {
            status = tokenGenerator.verify(PURPOSE_REFRESH_TOKEN, form.getRefreshToken());
        } catch (InvalidTokenException e) {
            throw new BadRequestException("1091-Token is invalid", e);
        } catch (TokenExpiredException e) {
            throw new BadRequestException("1090-Authorization token expired", e);
        }
        int userId;
        try {
            userId = Integer.parseInt(status.data.substring(0, 10).trim());
        } catch (NumberFormatException e) {
            throw new BadRequestException("1091-Token is invalid", e);
        }

        String password = status.data.substring(10);

        User user = userRepository.findByIdAndPassword(userId, password).orElseThrow(BadRequestException::new);

        String roleId = String.format(DIGIT_FORMAT, user.getRole());

        String id = String.format(DIGIT_FORMAT, user.getId());
        Token accessToken = tokenGenerator.create(PURPOSE_ACCESS_TOKEN, id, roleId,
                securityConfig.getAccessTokenExpiry());
        return new RefreshTokenView(
                new RefreshTokenView.TokenView(accessToken.value, accessToken.expiry),
                new RefreshTokenView.TokenView(form.getRefreshToken(), status.expiry));
    }

    @Override
    public UserView currentUser() {
        try {
            return new UserView(
                    userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(
                            () -> new BadRequestException(
                                    languageUtil.getTranslatedText("USER_NOT_FOUND", null, "en"))));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(languageUtil.getTranslatedText("Invalid.id.token1", null, "en"));
        }
    }

    private void sendVerificationEmail(String email, String otp) {
        String to = email;
        String emailTemplate = OTP_TEMPLATE;
        String subject = "Email Verification";
        newEmailUtil.sendEmail(to, emailTemplate, subject, otp);

    }

    private EmailTokenView generateToken(String email, String tokenType) {
        User emailUser = userRepository.findByEmailId(email);
        String roleId = String.format(DIGIT_FORMAT, emailUser.getRole());
        Duration duration = Duration.ofMinutes(ex);
        TokenGenerator.Token emailToken = tokenGenerator.create(tokenType, email, roleId, duration);
        return new EmailTokenView(SUCCESSFULLY_CREATED, emailToken.value);

    }

    @Override
    public TestViewOtp add(UserForm form, Byte userRole) throws BadRequestException {

        User emailUser = userRepository.findByEmailId(form.getEmail());
        if (emailUser == null) {

            Otp otp = userUtil.generateOtp(form.getEmail());

            if (userRole == User.Role.USER.value) {
                userRepository.save(new User(
                        form.getEmail(),
                        passwordEncoder.encode(form.getPassword())));

                sendVerificationEmail(form.getEmail(), otp.getOtp());
                EmailTokenView emailTokenView = userUtil.generatePurposeTokenWithEmail(form.getEmail(),
                        PURPOSE_EMAIL_TOKEN);
                return new TestViewOtp(emailTokenView.getMessage(), emailTokenView.getEmailToken(), otp.getOtp());

            } else {

                userRepository.save(new User(
                        form.getEmail(),
                        passwordEncoder.encode(form.getPassword())));
                User user = userRepository.findByEmailId(form.getEmail());
                user.setRole(User.Role.VENDOR.value);
                userRepository.save(user);

                sendVerificationEmail(form.getEmail(), otp.getOtp());
                EmailTokenView emailTokenView = userUtil.generatePurposeTokenWithEmail(form.getEmail(),
                        PURPOSE_EMAIL_TOKEN);
                return new TestViewOtp(emailTokenView.getMessage(), emailTokenView.getEmailToken(), otp.getOtp());

            }

        } else {
            if ((emailUser.getStatus() == User.Status.UNVERIFIED.value)) {

                Otp otp1 = userUtil.generateOtp(form.getEmail());
                sendVerificationEmail(form.getEmail(), otp1.getOtp());
                EmailTokenView emailTokenView = userUtil.generatePurposeTokenWithEmail(form.getEmail(),
                        PURPOSE_EMAIL_TOKEN);
                return new TestViewOtp(emailTokenView.getMessage(), emailTokenView.getEmailToken(), otp1.getOtp());

            } else if (emailUser.getStatus() == User.Status.DELETE.value) {

                throw new ConflictException(languageUtil.getTranslatedText("email.already.exist", null, "en"));
            } else {

                throw new ConflictException(languageUtil.getTranslatedText("email.already.exist", null, "en"));
            }
        }

    }

    @Override
    public ResponseEntity<String> verify(@Valid OtpForm form) {

        Status status = userUtil.verifyPurposeToken(PURPOSE_EMAIL_TOKEN, form.getEmailToken());
        String email = status.data;
        Otp otp = otpRepository.findByEmail(email);

        if (Boolean.TRUE.equals(userUtil.verifyOtp(otp, form.getOtp()))) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new BadRequestException(
                languageUtil.getTranslatedText(UNABLE_TO_PERFORM, null, "en"));

    }

    // Resend Otp
    @Override
    public TestViewOtp resend(@Valid ResendOtpForm form, Integer forgetValue) {
        Status status;
        EmailTokenView emailTok;

        switch (forgetValue) {
            // case 0 for forget password resend OTP
            case 0 -> {

                status = userUtil.verifyPurposeToken(PURPOSE_FORGET_TOKEN, form.getEmailToken());
                Otp otp = otpRepository.findByEmail(status.data);
                if (otp.getStatus() == Otp.STATUS.ACTIVE.value) {
                    String roleIdString = status.roleId.trim();
                    byte roleIdByte;

                    try {
                        roleIdByte = Byte.parseByte(roleIdString);
                    } catch (NumberFormatException e) {
                        throw new BadRequestException(languageUtil.getTranslatedText(UNABLE_TO_PERFORM, null, "en"));
                    }

                    if (roleIdByte == User.Role.VENDOR.value || roleIdByte == User.Role.USER.value) {
                        // forget token creation for user and vendor
                        emailTok = userUtil.generatePurposeTokenWithEmail(status.data, PURPOSE_FORGET_TOKEN);

                    } else {
                        // forget token creation for admin
                        emailTok = userUtil.generateAdminPurposeToken(status.data, PURPOSE_FORGET_TOKEN);
                    }
                } else {
                    throw new BadRequestException(languageUtil.getTranslatedText(UNABLE_TO_PERFORM, null, "en"));

                }
            }
            // case 1 for userReg resend OTP
            case 1 -> {
                status = userUtil.verifyPurposeToken(PURPOSE_EMAIL_TOKEN, form.getEmailToken());

                Otp otp = otpRepository.findByEmail(status.data);

                if (otp.getStatus() == Otp.STATUS.ACTIVE.value) {
                    emailTok = userUtil.generatePurposeTokenWithEmail(status.data, PURPOSE_EMAIL_TOKEN);
                } else {
                    throw new BadRequestException(languageUtil.getTranslatedText(UNABLE_TO_PERFORM, null, "en"));

                }
            }
            default ->
                throw new BadRequestException(languageUtil.getTranslatedText(UNABLE_TO_PERFORM, null, "en"));

        }

        String email = status.data;
        Otp otp = userUtil.generateOtp(email);
        sendVerificationEmail(email, otp.getOtp());
        return new TestViewOtp(emailTok.getMessage(), emailTok.getEmailToken(), otp.getOtp());

    }

    @Override
    public VendorOtpView verifyVendorOtp(@Valid OtpForm form) {

        Status status = userUtil.verifyPurposeToken(PURPOSE_EMAIL_TOKEN, form.getEmailToken());
        String email = status.data;
        Otp otp = otpRepository.findByEmailAndStatus(email, User.Status.ACTIVE.value)
                .orElseThrow(() -> new BadRequestException(
                        languageUtil.getTranslatedText(UNABLE_TO_PERFORM, null, "en")));

        if (Boolean.TRUE.equals(userUtil.verifyOtp(otp, form.getOtp()))) {
            User user = userRepository.findByEmailId(email);
            Token purposeTokenStage1 = userUtil.generatePurposeToken(user, PURPOSE_VENDOR_REG_STAGE_1);
            return new VendorOtpView(OTP_VERIFIED, purposeTokenStage1.value, user.getStatus(), user.getRole());

        }
        throw new BadRequestException(
                languageUtil.getTranslatedText(UNABLE_TO_PERFORM, null, "en"));

    }

    @Override
    public VendorRegStage1View vendorRegStage1(VendorRegStage1Form form) throws BadRequestException {

        Status status = userUtil.verifyPurposeToken(PURPOSE_VENDOR_REG_STAGE_1, form.getEmailToken());

        String email = status.data;

        User user = userRepository.findByEmailId(email);

        if (user.getStatus() == User.Status.VERIFIED.value || user.getStatus() == User.Status.STAGE1.value) {

            user.setUsername(form.getUsername());
            user.setPhone(form.getPhone());
            user.setStatus(User.Status.STAGE1.value);
            userRepository.save(user);

            Token vendorEmailToken = userUtil.generatePurposeToken(user, PURPOSE_VENDOR_REG_STAGE_2);

            return new VendorRegStage1View(user.getUsername(), user.getPhone(), vendorEmailToken.value);

        } else {
            throw new BadRequestException(languageUtil.getTranslatedText(UNABLE_TO_PERFORM, null, "en"));
        }

    }

    @Override
    public LoginView googleAuth(GoogleSignInForm form, Byte userRole) throws GeneralSecurityException, IOException {

        var token = form.getToken();
        GoogleIdToken idToken = verifyToken(token);
        String email = idToken.getPayload().getEmail();

        if (userRepository.existsByEmail(email)) {
            User user = userRepository.findByEmailId(email);
            return handleExistingUser(user);
        } else {

            return handleNewUser(email, userRole);
        }
    }

    private GoogleIdToken verifyToken(String token) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleId))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(token);

            if (idToken == null) {
                throw new BadRequestException(languageUtil.getTranslatedText("Invalid.id.token", null, "en"));
            } else {
                return idToken;
            }

        } catch (IllegalArgumentException e) {
            throw new BadRequestException(languageUtil.getTranslatedText("Invalid.id.token", null, "en"));
        }

    }

    private LoginView handleExistingUser(User user) {

        switch (user.getStatus()) {

            case 0 -> {
                return userUtil.generateTokenForActiveUser(user);
            }
            case 1 -> {
                if (user.getRole() == User.Role.USER.value) {

                    user.setStatus(User.Status.ACTIVE.value);
                    userRepository.save(user);
                    return userUtil.generateTokenForActiveUser(user);

                } else if (user.getRole() == User.Role.VENDOR.value) {
                    user.setStatus(User.Status.VERIFIED.value);
                    userRepository.save(user);
                    return userUtil.generatePurposeTokenForVendor(user, PURPOSE_VENDOR_REG_STAGE_1);
                }
            }
            case 2 -> {
                throw new BadRequestException((languageUtil.getTranslatedText(EMAIL_FOUND_ERROR, null, "en")));
            }
            case 3 -> {
                return userUtil.generatePurposeTokenForVendor(user, PURPOSE_VENDOR_REG_STAGE_1);
            }
            case 4 -> {
                return userUtil.generatePurposeTokenForVendor(user, PURPOSE_VENDOR_REG_STAGE_2);

            }
            case 5 -> {
                String message = "Waiting for admin approval";
                String msg = "please wait";
                return new LoginView(user, message, msg);
            }
            case 6, 7 ->
                throw new BadRequestException(languageUtil.getTranslatedText("user.blocked", null, "en"));
            default ->

                throw new BadRequestException(languageUtil.getTranslatedText(UNABLE_TO_PERFORM, null, "en"));

        }
        return null;

    }

    private LoginView handleNewUser(String email, Byte userRole) {

        if (userRole == User.Role.USER.value) {
            new UserView(
                    userRepository.save(new User(
                            email)));
            User user = userRepository.findByEmailId(email);
            return userUtil.generateTokenForActiveUser(user);

        } else if (userRole == User.Role.VENDOR.value) {
            User user = new User();
            user.setEmail(email);
            user.setRole(User.Role.VENDOR.value);
            user.setType(User.Type.GOOGLE.value);
            user.setStatus(User.Status.VERIFIED.value);
            userRepository.save(user);
            return userUtil.generatePurposeTokenForVendor(user, PURPOSE_VENDOR_REG_STAGE_1);
        }
        return null;
    }

    public static final String SUCCESS_STRING = "successfully created";

    @Override
    public TestViewOtp forgetPasswordEmail(EmailForm form) throws BadRequestException {

        User forgetUser = userRepository.findByEmailAndStatus(form.getEmail(), User.Status.ACTIVE.value);

        if (forgetUser == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(EMAIL_FOUND_ERROR, null, "en"));

        } else if (forgetUser.getType() == User.Type.GOOGLE.value) {
            throw new BadRequestException(languageUtil.getTranslatedText(UNABLE_TO_PERFORM, null, "en"));

        } else {

            Otp otp = userUtil.generateOtp(form.getEmail());
            sendVerificationEmail(form.getEmail(), otp.getOtp());
            EmailTokenView emailTokenView = userUtil.generatePurposeTokenWithEmail(form.getEmail(),
                    PURPOSE_FORGET_TOKEN);
            return new TestViewOtp(emailTokenView.getMessage(), emailTokenView.getEmailToken(), otp.getOtp());
        }

    }

    @Override
    public EmailTokenView forgetVerify(@Valid OtpForm form) {

        Status status = userUtil.verifyPurposeToken(PURPOSE_FORGET_TOKEN, form.getEmailToken());
        String email = status.data;
        Otp otp = otpRepository.findByEmail(email);
        if (otp.getStatus() == Otp.STATUS.ACTIVE.value) {

            LocalTime myObj = LocalTime.now();
            var expDiff = otp.getExpiry().until(myObj, ChronoUnit.SECONDS);

            if ((form.getOtp().equals(otp.getOtp()))) {
                if (expDiff < 86400) {

                    User user = userRepository.findByEmailId(email);
                    user.setStatus(User.Status.ACTIVE.value);
                    userRepository.save(user);
                    otp.setStatus(Otp.STATUS.INACTIVE.value);
                    otpRepository.save(otp);
                    return generateToken(email, PURPOSE_CHANGE_PASSWORD);

                }
                throw new BadRequestException(languageUtil.getTranslatedText(EMAIL_FOUND_ERROR, null, "en"));

            } else {
                throw new BadRequestException("1042-Incorrect otp");
            }
        } else {
            throw new BadRequestException(languageUtil.getTranslatedText(UNABLE_TO_PERFORM, null, "en"));

        }

    }

    @Override
    public ResponseEntity<String> changePassword(@Valid ChangePasswordForm form) {

        Status status = userUtil.verifyPurposeToken(PURPOSE_CHANGE_PASSWORD, form.getEmailToken());
        String email = status.data;
        User user = userRepository.findByEmailId(email);
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        userRepository.save(user);

        String message = "password changed successfully";
        String json = String.format("{\"message\":\"%s\"}", message);
        return ResponseEntity.ok(json);

    }

    @Override
    public ResponseEntity<String> UserChangePassword(@Valid UserChangePasswordForm form) {
        User user = userUtil.validateAnyRoleUser(UNABLE_TO_PERFORM);
        if (user.getType() == User.Type.NORMAL.value) {
            if (form.getOldPassword() == null || form.getOldPassword().isEmpty()) {
                throw new BadRequestException(languageUtil.getTranslatedText("password.required", null, "en"));
            }
            if (passwordEncoder.matches(form.getOldPassword(), user.getPassword())) {
                samePasswordValidate(form.getOldPassword(), form.getNewPassword());
                user.setPassword(passwordEncoder.encode(form.getNewPassword()));
            } else {
                // password does not match
                throw new BadRequestException(languageUtil.getTranslatedText("password.incorrect", null, "en"));
            }
        } else {
            user.setType(User.Type.NORMAL.value);
            user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        }

        userRepository.save(user);
        String message = "password changed successfully";
        String json = String.format("{\"message\":\"%s\"}", message);
        return ResponseEntity.ok(json);
    }

    public void samePasswordValidate(String oldPassword, String newPassword) {
        if (oldPassword.equals(newPassword)) {
            throw new BadRequestException(languageUtil.getTranslatedText("same.password", null, "en"));
        }
    }

    @Override
    public AdminVendorView userDetailView() {
        User user = userUtil.validateAnyRoleUser(UNABLE_TO_PERFORM);
        return new AdminVendorView(user);
    }

    @Override
    public ResponseEntity<String> updateProfile(ProfileEditForm form, byte role) {

        User user = userUtil.validateUser(UNABLE_TO_PERFORM);
        user.setUsername(form.getUsername());
        user.setPhone(form.getPhone());
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);

    }

}