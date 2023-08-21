package com.innovature.rentx.view;

import com.innovature.rentx.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminCategoryPriorityView {
    private Integer category_id;
    private String category_name;
    private Long product_count;
    public AdminCategoryPriorityView(Category category,Long productCount){
        this.category_id=category.getId();
        this.category_name=category.getName();
    }
    public AdminCategoryPriorityView(Integer category_id, String category_name, Long product_count) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.product_count = product_count;
    }


    public AdminCategoryPriorityView(Integer id,String category_name, long productCount) {
        this.category_id = id;
        this.category_name = category_name;
        this.product_count = productCount;
    }
}
