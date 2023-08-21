package com.innovature.rentx.view;
import java.util.List;

import com.innovature.rentx.entity.SubCategory;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class AllSubCategoryListView {
    private String category;
    private String coverImage;
    private List<SubCategoryListView> subcategories;

    public AllSubCategoryListView(String category,String coverImage, List<SubCategoryListView> subcategories) {
        this.category = category;
        this.coverImage=coverImage;
        this.subcategories = subcategories;
    }

    public AllSubCategoryListView(SubCategory subCategory) {
        
    }
}
