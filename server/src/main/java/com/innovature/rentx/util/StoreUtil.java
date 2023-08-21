package com.innovature.rentx.util;

import com.innovature.rentx.entity.Store;
import com.innovature.rentx.exception.BadRequestException;
import com.innovature.rentx.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StoreUtil {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private LanguageUtil languageUtil;

    public  void setStoreStatusUponBlockAndUnBlock(Integer userId,byte status,byte newStatus) {
        List<Store> storeList = storeRepository.findByUserIdAndStatus(userId, status);
        if (storeList != null) {

            for (Store store : storeList) {
                store.setStatus(newStatus);
                storeRepository.save(store);
            }
        }
    }

    public void checkProductNameExist(Integer storeId,byte[] status,String storeName){
        Boolean storeExistsInSameArea =  storeRepository.existsByNameAndStatusInAndIdNot(storeName,status,storeId);
        if(Boolean.TRUE.equals(storeExistsInSameArea)){
            throw new BadRequestException(languageUtil.getTranslatedText("store.already.exists", null, "en"));
        }
    }

}
