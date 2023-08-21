package com.innovature.rentx.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovature.rentx.form.StoreForm;
import com.innovature.rentx.service.StoreService;
import com.innovature.rentx.util.Pager;
import com.innovature.rentx.view.VendorStoreListView;
import com.innovature.rentx.view.VendorStoreDetailView;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/vendor/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // Add store
    @PostMapping("add")
    public ResponseEntity<String> addStore(@Valid @RequestBody StoreForm form) {
        storeService.addStore(form);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // list all stores under the vendor
    @GetMapping("list")
    public Pager<VendorStoreListView> vendorStoreListViewPager(
            @RequestParam(name = "search", defaultValue = "", required = false) String search,
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "5", required = false) Integer size,
            @RequestParam(name = "sort", defaultValue = "updatedAt") String sort,
            @RequestParam(name = "direction", defaultValue = "DESC") String direction) {
        return storeService.listStore(search, page, size, sort, direction);
    }

    @GetMapping("detailView/{storeId}")
    public VendorStoreDetailView getVendorStoreDetailView(Principal p,
            @PathVariable("storeId") Integer storeId) {
        return storeService.detailView(storeId);
    }

    @PutMapping("update/{storeId}")
    public ResponseEntity<String> updateStore(Principal p,
            @PathVariable("storeId") Integer storeId,
            @RequestBody @Valid StoreForm form) {
        storeService.updateStore(storeId, form);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("delete/{storeId}")
    public ResponseEntity<String> deleteStore(Principal p,
            @PathVariable("storeId") Integer storeId) {
        storeService.deleteStore(storeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
