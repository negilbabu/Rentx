package com.innovature.rentx.controller;

import java.util.Collection;
import java.util.List;

import com.innovature.rentx.entity.Category;
import com.innovature.rentx.service.ProductService;
import com.innovature.rentx.util.DateValidation;
import com.innovature.rentx.util.Pager;
import com.innovature.rentx.view.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.innovature.rentx.service.CategoryService;
import com.innovature.rentx.service.StoreService;
import com.innovature.rentx.service.SubCategoryService;

@RestController
@RequestMapping("/unAuthorized")
@RequiredArgsConstructor
public class UnauthorizedController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final StoreService storeService;
    private final SubCategoryService subCategoryService;
    private final DateValidation dateValidation;

    @GetMapping("/category/findAll")
    public Collection<CategoryListView> listCategory() {
        return categoryService.list();
    }

    @GetMapping("store/findAll")
    public Collection<StoreView> listStore() {
        return storeService.list();
    }

    @GetMapping("/subCategory/{categoryId}")
    public Collection<SubCategoryListView> getAllSubCategories(@PathVariable(required = false) Integer categoryId) {
        return subCategoryService.getAllSubCategoriesUnauthorized(categoryId);
    }
    
    @GetMapping("/user/product/list")
    public Pager<UserProductView> productList(
            @RequestParam(name = "search", defaultValue = "", required = false) String searchData,
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "15", required = false) Integer limit,
            @RequestParam(name = "sort", defaultValue = "name") String sort,
            @RequestParam(name = "order", defaultValue = "ASC") String order,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "store", required = false) String store,
            @RequestParam(name="startDate", required = false) String startDate,
            @RequestParam(name="endDate", required = false) String endDate,
            @RequestParam(name="quantity" ,defaultValue = "1", required = false) Integer quantity,
            @RequestParam(name = "latitude", required = false) Double latitude,
            @RequestParam(name="longitude", required = false) Double longitude) {

        String formattedStartDate=dateValidation.startDateToString(startDate);
        String formattedEndDate=dateValidation.endDateToString(endDate);
        return productService.viewProduct(searchData, page, limit, sort, order, category, store,formattedStartDate,formattedEndDate,quantity,latitude,longitude);
    }

    @GetMapping("/user/product/{productId}")
    public UserProductDetailView userProductView(@PathVariable Integer productId,
                                                 @RequestParam(name="startDate", required = false) String startDate,
                                                 @RequestParam(name="endDate", required = false) String endDate,
                                                 @RequestParam(name="quantity" ,defaultValue = "1", required = false) Integer quantity) {
        String formattedStartDate=dateValidation.startDateToString(startDate);
        String formattedEndDate=dateValidation.endDateToString(endDate);
        return productService.userProductDetailView(productId, formattedStartDate, formattedEndDate, quantity);
    }

    @GetMapping("/user/availability/{productId}")
    public UserAvailabilityView productAvailability(@PathVariable Integer productId,
                                                    @RequestParam(name="startDate", required = false) String startDate,
                                                    @RequestParam(name="endDate", required = false) String endDate,
                                                    @RequestParam(name="quantity" ,defaultValue = "1", required = false) Integer quantity) {
        String formattedStartDate=dateValidation.startDateToString(startDate);
        String formattedEndDate=dateValidation.endDateToString(endDate);

        return productService.availabilityCheck(productId, formattedStartDate, formattedEndDate,quantity);
    }
    @GetMapping("/category/priority")
    public List<Category> getPriorityList(){
        return categoryService.adminCategoryListPriorityList();
    }
}

