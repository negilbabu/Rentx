package com.innovature.rentx.util;

import com.innovature.rentx.entity.Admin;
import com.innovature.rentx.entity.Otp;
import com.innovature.rentx.entity.User;
import com.innovature.rentx.exception.BadRequestException;
import com.innovature.rentx.form.LoginForm;
import com.innovature.rentx.repository.AdminRepository;
import com.innovature.rentx.repository.OtpRepository;
import com.innovature.rentx.repository.UserRepository;
import com.innovature.rentx.security.config.SecurityConfig;
import com.innovature.rentx.security.util.InvalidTokenException;
import com.innovature.rentx.security.util.SecurityUtil;
import com.innovature.rentx.security.util.TokenExpiredException;
import com.innovature.rentx.security.util.TokenGenerator;
import com.innovature.rentx.security.util.TokenGenerator.Status;
import com.innovature.rentx.view.EmailTokenView;
import com.innovature.rentx.view.LoginView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import static com.innovature.rentx.security.AccessTokenUserDetailsService.PURPOSE_ACCESS_TOKEN;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;
import java.util.Random;
import java.time.temporal.ChronoUnit;

@Component
public class UserUtil {

    @Autowired
    private LanguageUtil languageUtil;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private SecurityConfig securityConfig;

    @Value("${purpose.token.expiry}")
    private Integer ex;

    Random random1 = new Random();

    private byte[] userStatusCheck = { User.Status.ACTIVE.value };
    private byte[] userRoleCheck = { User.Role.USER.value,User.Role.VENDOR.value };


    private static final String DIGIT_FORMAT = "%10d";

    private static final String PURPOSE_REFRESH_TOKEN = "REFRESH_TOKEN";

    private static final String INVALID_TOKEN_EMAIL = "Invalid.Token.Email";

    public static final String SUCCESSFULLY_CREATED = "successfully created";

    private static final String EMAIL_ERROR = "email.password.incorrect";

    private static final String EMAIL_FOUND_ERROR = "email.found.error";

    public User validateUser(String msg) {
        User user= userRepository
                .findByIdAndRoleAndStatusIn(SecurityUtil.getCurrentUserId(), User.Role.USER.value, userStatusCheck)
                .orElseThrow(() -> new BadRequestException(languageUtil.getTranslatedText(msg, null, "en")));
        if(user!=null){
            return user;
        }
        else{
            return null;
        }
    }
    public User validateUser1(String msg) {
        User user= userRepository
                .findByIdAndRoleInAndStatusIn(SecurityUtil.getCurrentUserId(), new byte[]{User.Role.USER.value}, userStatusCheck);
        if(user!=null){
            return user;
        }
        else{
            return null;
        }
    }


    public User validateVendor(String msg) {
        return userRepository
                .findByIdAndRoleAndStatusIn(SecurityUtil.getCurrentUserId(), User.Role.VENDOR.value, userStatusCheck)
                .orElseThrow(() -> new BadRequestException(languageUtil.getTranslatedText(msg, null, "en")));
    }

    public User validateVendorWithStatus0And7(String msg) {
        final byte[] vendorStatuses = { User.Status.ACTIVE.value, User.Status.BLOCKED.value };

        return userRepository
                .findByIdAndRoleAndStatusIn(SecurityUtil.getCurrentUserId(), User.Role.VENDOR.value, vendorStatuses)
                .orElseThrow(() -> new BadRequestException(languageUtil.getTranslatedText(msg, null, "en")));
    }

    public User validateVendorAndUser(byte role,String msg) {
        User user= userRepository
                .findByIdAndRoleAndStatusIn(SecurityUtil.getCurrentUserId(), role, userStatusCheck)
                .orElseThrow(() -> new BadRequestException(languageUtil.getTranslatedText(msg, null, "en")));

                return user;

    }


    public Void validateVendorCheck(String msg){
        User user= userRepository.findByIdAndStatus(SecurityUtil.getCurrentUserId(),User.Status.BLOCKED.value);
        if(user!=null){
            throw new BadRequestException(languageUtil.getTranslatedText(msg, null, "en"));
        }
        return null;
    }
    public User validateAnyRoleUser(String msg) {
        return userRepository
              .findByIdAndRoleInAndStatus(SecurityUtil.getCurrentUserId(), userRoleCheck,User.Status.ACTIVE.value)
              .orElseThrow(() -> new BadRequestException(languageUtil.getTranslatedText(msg, null, "en")));
    }

    public User verifyUserLogin(User user, LoginForm form) {
        // check wether not a google sign in and password not matching
        if (user.getType() != User.Type.NORMAL.value
                || !passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            throw new BadRequestException((languageUtil.getTranslatedText(EMAIL_ERROR, null, "en")));
        }
        if (user.getStatus() == User.Status.UNVERIFIED.value
                || user.getStatus() == User.Status.DELETE.value) {
            throw new BadRequestException((languageUtil.getTranslatedText(EMAIL_FOUND_ERROR, null, "en")));
        }
        if (user.getStatus() == User.Status.INACTIVE.value
                || user.getStatus() == User.Status.BLOCKED.value && user.getRole() == User.Role.USER.value) {
            // ie the rejected vendor
            throw new BadRequestException((languageUtil.getTranslatedText("user.blocked", null, "en")));

        }
        return user;

    }

    public LoginView generateTokenForActiveUser(User user) {

        String roleId = String.format(DIGIT_FORMAT, user.getRole());
        String id = String.format(DIGIT_FORMAT, user.getId());

        TokenGenerator.Token accessToken = tokenGenerator.create(PURPOSE_ACCESS_TOKEN, id, roleId,
                securityConfig.getAccessTokenExpiry());
        TokenGenerator.Token refreshToken = tokenGenerator.create(PURPOSE_REFRESH_TOKEN,
                id + user.getPassword(), roleId, securityConfig.getRefreshTokenExpiry());
        return new LoginView(user, accessToken, refreshToken);

    }

    public TokenGenerator.Token generatePurposeToken(User user, String tokenType) {
        Duration duration = Duration.ofMinutes(ex);
        String roleId = String.format(DIGIT_FORMAT, user.getRole());
        return tokenGenerator.create(tokenType, user.getEmail(), roleId, duration);
    }

    public EmailTokenView generatePurposeTokenWithEmail(String email, String tokenType) {

        User emailUser = userRepository.findByEmailId(email);
        String roleId = String.format(DIGIT_FORMAT, emailUser.getRole());
        Duration duration = Duration.ofMinutes(ex);
        TokenGenerator.Token emailToken = tokenGenerator.create(tokenType, email, roleId, duration);
        return new EmailTokenView(SUCCESSFULLY_CREATED, emailToken.value);

    }

    public LoginView generatePurposeTokenForVendor(User user,String tokenType) {
        String roleId = String.format(DIGIT_FORMAT, user.getRole());
        Duration duration = Duration.ofMinutes(ex);
        TokenGenerator.Token emailToken = tokenGenerator.create(tokenType, user.getEmail(), roleId, duration);
        return new LoginView(user, emailToken.value);

    }


    public EmailTokenView generateAdminPurposeToken(String email, String tokenType) {

        Admin adminUser = adminRepository.findByEmailId(email);
        String roleId = String.format(DIGIT_FORMAT, adminUser.getRole());
        Duration duration = Duration.ofMinutes(ex);
        TokenGenerator.Token emailToken = tokenGenerator.create(tokenType, email, roleId, duration);
        return new EmailTokenView(SUCCESSFULLY_CREATED, emailToken.value);

    }

    public Status verifyPurposeToken(String tokenType, String token) {

        try {
            return tokenGenerator.verify(tokenType, token);
        } catch (InvalidTokenException e) {
            throw new BadRequestException(languageUtil.getTranslatedText(INVALID_TOKEN_EMAIL, null, "en"), e);
        } catch (TokenExpiredException e) {
            throw new BadRequestException(languageUtil.getTranslatedText("Token.expired", null, "en"), e);
        }
    }

    public Otp generateOtp(String email) {

        int otp = 100000 + random1.nextInt(900000);
        String otpStr = String.valueOf(otp);
        LocalTime exp = LocalTime.now();

        Otp otp1 = otpRepository.findByEmail(email);
        if (otp1 != null) {
            otp1.setOtp(otpStr);
            otp1.setExpiry(exp);
            otp1.setStatus(Otp.STATUS.ACTIVE.value);
            otpRepository.save(otp1);
            return otp1;
        } else {
            Otp otpEntity = new Otp();
            otpEntity.setEmail(email);
            otpEntity.setOtp(otpStr);
            otpEntity.setExpiry(exp);
            otpEntity.setCreatedAt(new Date());
            otpEntity.setStatus(Otp.STATUS.ACTIVE.value);
            otpRepository.save(otpEntity);
            return otpEntity;
        }
    }

    public Boolean verifyOtp(Otp otpEntity, String otp) {
        LocalTime myObj = LocalTime.now();
        var expDiff = otpEntity.getExpiry().until(myObj, ChronoUnit.SECONDS);
        if ((otp.equals(otpEntity.getOtp()))) {
            if (expDiff < 86400) {
                User user = userRepository.findByEmailId(otpEntity.getEmail());
                if (user.getRole() == User.Role.VENDOR.value && user.getStatus() == User.Status.UNVERIFIED.value) {
                    user.setStatus(User.Status.VERIFIED.value);
                    otpEntity.setStatus(Otp.STATUS.INACTIVE.value);
                    otpRepository.save(otpEntity);
                    userRepository.save(user);
                    return true;
                } else if (user.getRole() == User.Role.USER.value && user.getStatus() == User.Status.UNVERIFIED.value) {
                    user.setStatus(User.Status.ACTIVE.value);
                    userRepository.save(user);
                    otpEntity.setStatus(Otp.STATUS.INACTIVE.value);
                    otpRepository.save(otpEntity);
                    return true;
                } else {
                    return false;
                }
            }
            throw new BadRequestException(languageUtil.getTranslatedText("otp.expired", null, "en"));
        }
        throw new BadRequestException(languageUtil.getTranslatedText("Incorrect.otp", null, "en"));

    }

}