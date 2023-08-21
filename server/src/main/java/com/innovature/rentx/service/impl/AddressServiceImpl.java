package com.innovature.rentx.service.impl;

import java.util.Collection;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.innovature.rentx.entity.Address;
import com.innovature.rentx.entity.User;
import com.innovature.rentx.exception.BadRequestException;
import com.innovature.rentx.form.AddressForm;
import com.innovature.rentx.repository.AddressRepository;
import com.innovature.rentx.service.AddressService;
import com.innovature.rentx.util.LanguageUtil;
import com.innovature.rentx.util.UserUtil;
import com.innovature.rentx.view.AddressView;
import com.innovature.rentx.view.UserAddressView;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

  private final AddressRepository addressRepository;

  private final UserUtil userUtil;

  private final LanguageUtil languageUtil;

  private static final String UNABLE_TO_PERFORM = "unable.to.perform";

  private static final String INVALID_ADDRESS_ID = "invalid.address.id";


  @Override
  public ResponseEntity<String> add(AddressForm form) {

    User user = userUtil.validateUser(UNABLE_TO_PERFORM);

    Long count = addressRepository.countByUserIdAndStatus(user, Address.Status.ACTIVE.value);

    if (count > 4) {
      throw new BadRequestException(languageUtil.getTranslatedText("address.limit.reached", null, "en"));
    }

    if (!verifyType(form.getType())) {
      throw new BadRequestException(languageUtil.getTranslatedText("invalid.type", null, "en"));
    }

    addressRepository.save(new Address(form, user));
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public Collection<UserAddressView> list() {
    User user = userUtil.validateUser(UNABLE_TO_PERFORM);
    return addressRepository.findByUserIdAndStatus(user, Address.Status.ACTIVE.value);
  }

  @Override
  public AddressView viewAddressDetail(Integer addressId) {

    User user = userUtil.validateUser(UNABLE_TO_PERFORM);

   Address address = addressRepository.findByIdAndUserIdAndStatus(addressId,user, Address.Status.ACTIVE.value)
   .orElseThrow(() -> new BadRequestException(
       languageUtil.getTranslatedText(INVALID_ADDRESS_ID, null, "en")));

    return new AddressView(address);
  
  }

  @Override
  public ResponseEntity<String> edit(Integer id, AddressForm form) {

    User user = userUtil.validateUser(UNABLE_TO_PERFORM);

    Address address = addressRepository.findByIdAndUserIdAndStatus(id, user, Address.Status.ACTIVE.value)
        .orElseThrow(() -> new BadRequestException(
            languageUtil.getTranslatedText(INVALID_ADDRESS_ID, null, "en")));

    if (!verifyType(form.getType())) {
      throw new BadRequestException(languageUtil.getTranslatedText("invalid.type", null, "en"));
    }
    address.setName(form.getName());
    address.setHouseName(form.getHouseName());
    address.setPhone(form.getPhone());
    address.setCity(form.getCity());
    address.setPinCode(form.getPinCode());
    address.setState(form.getState());
    address.setType(form.getType());
    addressRepository.save(address);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> delete(Integer id) {

    User user = userUtil.validateUser(UNABLE_TO_PERFORM);
    Address address = addressRepository.findByIdAndUserIdAndStatus(id, user, Address.Status.ACTIVE.value)
        .orElseThrow(() -> new BadRequestException(
            languageUtil.getTranslatedText(INVALID_ADDRESS_ID, null, "en")));
    address.setStatus(Address.Status.DELETED.value);
    addressRepository.save(address);
    return new ResponseEntity<>(HttpStatus.OK);

  }

  private boolean verifyType(byte type) {
    return type == 0 || type == 1 || type == 2;
}

}
