package com.innovature.rentx.service.impl;

import com.innovature.rentx.entity.Admin;
import com.innovature.rentx.entity.PaymentMethod;
import com.innovature.rentx.exception.BadRequestException;
import com.innovature.rentx.exception.ConflictException;
import com.innovature.rentx.form.PaymentAddForm;
import com.innovature.rentx.repository.AdminRepository;
import com.innovature.rentx.repository.PaymentRepository;
import com.innovature.rentx.security.util.SecurityUtil;
import com.innovature.rentx.service.PaymentMethodService;
import com.innovature.rentx.util.LanguageUtil;
import com.innovature.rentx.view.AdminPaymentMethodView;
import com.innovature.rentx.view.VendorPaymentListView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final LanguageUtil languageUtil;
    private final AdminRepository adminRepository;
    private final PaymentRepository paymentRepository;

    private static final String PAYMENT_ALREADY_EXISTS="payment.already.added";
    private static final String PAYMENT_ID_INVALID="payment.method.invalid";

    private final byte[] adminStatusIn={PaymentMethod.Status.ACTIVE.value,PaymentMethod.Status.INACTIVE.value};

    @Override
    public ResponseEntity<String>addPaymentMethod(@Valid @RequestBody PaymentAddForm form){
        if(paymentRepository.countByStatus(PaymentMethod.Status.ACTIVE.value)>4){
            throw new BadRequestException(languageUtil.getTranslatedText("payment.limit.error",null,"en"));
        }
        Optional<PaymentMethod> paymentMethodExist=paymentRepository.findByNameAndStatus(form.getType(),PaymentMethod.Status.ACTIVE.value);
        if(paymentMethodExist.isEmpty()){
            Admin admin=adminRepository.findByUserId(SecurityUtil.getCurrentUserId());
                paymentRepository.save(new PaymentMethod(form.getType(), PaymentMethod.Status.ACTIVE.value, admin));
            String message = "Payment Method Created successfully";
            return successResponse(message);

        }
        else{
            throw new ConflictException(languageUtil.getTranslatedText(PAYMENT_ALREADY_EXISTS,null,"en"));
        }

    }
    @Override
    public ResponseEntity<String>editPaymentMethod(Integer id,PaymentAddForm form){
        Optional<PaymentMethod> paymentMethodExist=paymentRepository.findByNameAndStatus(form.getType(),PaymentMethod.Status.ACTIVE.value);
        if(paymentMethodExist.isEmpty()){
            PaymentMethod paymentMethod=paymentRepository.findByIdAndStatus(id,PaymentMethod.Status.ACTIVE.value);
            if(paymentMethod==null){
                throw new ConflictException(languageUtil.getTranslatedText(PAYMENT_ID_INVALID,null,"en"));
            }
            paymentMethod.setName(form.getType());
            paymentRepository.save(paymentMethod);
            String message = "Payment Method Edited successfully";
            return successResponse(message);
        }
        else{
            throw new ConflictException(languageUtil.getTranslatedText(PAYMENT_ALREADY_EXISTS,null,"en"));
        }
    }

    @Override
    public Collection<AdminPaymentMethodView>adminListPaymentMethods(){
        return paymentRepository.findByStatusIn(adminStatusIn)
                .stream()
                .map(AdminPaymentMethodView::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<VendorPaymentListView> viewPaymentMethodsList(){
            return paymentRepository.findAllByStatus(PaymentMethod.Status.ACTIVE.value);
    }

    @Override
    public ResponseEntity<String>deletePaymentMethod(Integer id){
        if(id == null){
            throw new BadRequestException(languageUtil.getTranslatedText("payment.id.required",null,"en"));
        }
        PaymentMethod paymentMethod=paymentRepository.findByIdAndStatus(id,PaymentMethod.Status.ACTIVE.value);
        if(paymentMethod==null){
            throw new BadRequestException(languageUtil.getTranslatedText(PAYMENT_ID_INVALID,null,"en"));
        }
        paymentMethod.setStatus(PaymentMethod.Status.DELETED.value);
        paymentRepository.save(paymentMethod);
        String message = "Payment Method Deleted successfully";
        return successResponse(message);
    }

    @Override
    public AdminPaymentMethodView paymentDetailsView(Integer id){
        if(id == null){
            throw new BadRequestException(languageUtil.getTranslatedText("payment.id.required",null,"en"));
        }
        PaymentMethod paymentMethod= paymentRepository.findByIdAndStatus(id,PaymentMethod.Status.ACTIVE.value);
        if(paymentMethod==null){
            throw new BadRequestException(languageUtil.getTranslatedText(PAYMENT_ID_INVALID,null,"en"));
        }
        return new AdminPaymentMethodView(paymentMethod);
    }

    public ResponseEntity<String>successResponse(String message){
        return new ResponseEntity<>("{\"message\":\"" + message + "\"}", HttpStatus.OK);
    }
}
