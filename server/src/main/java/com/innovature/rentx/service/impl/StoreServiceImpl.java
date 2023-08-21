package com.innovature.rentx.service.impl;

import com.innovature.rentx.entity.Product;
import com.innovature.rentx.entity.Store;
import com.innovature.rentx.entity.User;
import com.innovature.rentx.entity.Store.Status;
import com.innovature.rentx.exception.BadRequestException;
import com.innovature.rentx.exception.ConflictException;
import com.innovature.rentx.form.StoreForm;
import com.innovature.rentx.repository.ProductRepository;
import com.innovature.rentx.repository.StoreRepository;
import com.innovature.rentx.repository.UserRepository;
import com.innovature.rentx.security.util.SecurityUtil;
import com.innovature.rentx.service.StoreService;
import com.innovature.rentx.util.LanguageUtil;
import com.innovature.rentx.util.Pager;
import com.innovature.rentx.util.StoreUtil;
import com.innovature.rentx.util.UserUtil;
import com.innovature.rentx.view.StoreView;
import com.innovature.rentx.view.VendorStoreDetailView;
import com.innovature.rentx.view.VendorStoreListView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private StoreUtil storeUtil;

    @Autowired
    LanguageUtil languageUtil;

    private final UserUtil userUtil;
    private static final String UNABLE_PERFORM = "unable.to.perform.this.action";
    private static final String PERMISSION_DENIED = "dont.have.access";
    private static final String INVALID_STORE_ID = "invalid.store.id";


    private byte[] storesStatusCheck = { Store.Status.ACTIVE.value, Store.Status.INACTIVE.value };

    @Override
    public ResponseEntity<String> addStore(StoreForm form) {
        userUtil.validateVendorCheck(PERMISSION_DENIED);
        User user = userRepository.findByIdAndStatus(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value);
        while (user != null) {

            if (storeRepository.findByNameAndLattitudeAndLongitude(form.getName(), form.getLattitude(),
                    form.getLongitude()) != null) {
                throw new ConflictException(languageUtil.getTranslatedText("store.already.exists", null, "en"));
            } else {
                storeRepository.save(new Store(form, user));
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        throw new ConflictException(languageUtil.getTranslatedText(UNABLE_PERFORM, null, "en"));
    }

    public Pager<VendorStoreListView> listStore(String search, Integer page, Integer size, String sort,
            String direction) {

        search = search.trim();
        try {
            page = Integer.parseInt(String.valueOf(page));
            size = Integer.parseInt(String.valueOf(size));
        } catch (NumberFormatException e) {
            throw new BadRequestException(languageUtil.getTranslatedText(UNABLE_PERFORM, null, "en"));
        }

        if (page < 1) {
            throw new BadRequestException(languageUtil.getTranslatedText("Page.natual", null, "en"));
        }

        if (size < 1) {
            throw new BadRequestException(languageUtil.getTranslatedText("Size.natural", null, "en"));
        }
        Pager<VendorStoreListView> viewStore;
        boolean directionSort = !direction.equalsIgnoreCase("asc");
        List<VendorStoreListView> storeList;
        storeList = StreamSupport
                .stream(storeRepository.findByUserIdAndUserRoleAndUserStatusAndStatusInAndNameContainingIgnoreCase(
                        SecurityUtil.getCurrentUserId(),
                        User.Role.VENDOR.value,
                        User.Status.ACTIVE.value,
                                storesStatusCheck,
                        search,
                        PageRequest.of(page - 1, size,
                                (directionSort == true) ? Sort.Direction.DESC : Sort.Direction.ASC, sort))
                        .spliterator(), false)
                .map(VendorStoreListView::new)
                .collect(Collectors.toList());
        int countData = storeRepository.countByUserIdAndUserRoleAndUserStatusAndStatusInAndNameContainingIgnoreCase(
                SecurityUtil.getCurrentUserId(),
                User.Role.VENDOR.value,
                User.Status.ACTIVE.value,
                storesStatusCheck,
                search);
        viewStore = new Pager<>(size, countData, page);
        viewStore.setResult(storeList);
        return viewStore;

    }

    @Override
    public Collection<StoreView> list() {
        Iterable<Store> iterableCategories = storeRepository.findAllByStatusOrderByUpdatedAtDesc(Status.ACTIVE.value);

        // Map Category objects to CategoryListView objects using Stream API and
        // Collectors.toList()
        List<StoreView> storeView = StreamSupport.stream(iterableCategories.spliterator(), false)
                .map(store -> new StoreView(store))
                .collect(Collectors.toList());
        return storeView;
    }

    public ResponseEntity<String> updateStore(Integer storeId, StoreForm form) {
        final byte[] activeStoresCheck = { Store.Status.ACTIVE.value, Store.Status.INACTIVE.value };

        User user = userUtil.validateVendor(UNABLE_PERFORM);

        Store store = storeRepository.findByIdAndUserIdAndStatusIn(storeId, user.getId(), activeStoresCheck)
                .orElseThrow(
                        () -> new BadRequestException(
                                languageUtil.getTranslatedText(INVALID_STORE_ID, null, "en")));

        storeUtil.checkProductNameExist(storeId,activeStoresCheck,form.getName());
        store.setMobile(form.getMobile());
        store.setName(form.getName());
        store.setPincode(form.getPincode());
        store.setCity(form.getCity());
        store.setState(form.getState());
        store.setDistrict(form.getDistrict());
        store.setCountry(form.getCountry());
        store.setBuildingName(form.getBuildingName());
        store.setLattitude(form.getLattitude());
        store.setLongitude(form.getLongitude());

        storeRepository.save(store);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    public ResponseEntity<String> deleteStore(Integer storeId) {

        User user = userUtil.validateVendor(UNABLE_PERFORM);

        Store store = storeRepository.findByIdAndUserIdAndStatusIn(storeId, user.getId(), storesStatusCheck)
                .orElseThrow(
                        () -> new BadRequestException(
                                languageUtil.getTranslatedText(INVALID_STORE_ID, null, "en")));

        store.setStatus(Store.Status.DELETED.value);
        storeRepository.save(store);

        Collection<Product> products = productRepository.findByStoreIdAndStatus(storeId, Product.Status.ACTIVE.value);
        for (Product product : products) {
            product.setStatus(Product.Status.DELETED.value);
            productRepository.save(product);
        }

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Override
    @Transactional
    public VendorStoreDetailView detailView(Integer storeId) {

        User user = userUtil.validateVendorWithStatus0And7(UNABLE_PERFORM);

        Store store = storeRepository.findByIdAndUserIdAndStatusIn(storeId, user.getId(), storesStatusCheck)
                .orElseThrow(
                        () -> new BadRequestException(
                                languageUtil.getTranslatedText(INVALID_STORE_ID, null, "en")));

        return new VendorStoreDetailView(store);
    }
}
