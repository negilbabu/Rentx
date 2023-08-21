package com.innovature.rentx.repository;
import org.springframework.data.repository.Repository;
import com.innovature.rentx.entity.Otp;

import java.util.Optional;

public interface OtpRepository extends Repository<Otp, Integer>{
    Otp save(Otp otp);
    Otp findByEmail(String email);
    Optional <Otp> findByEmailAndStatus(String email, byte Status);
}
