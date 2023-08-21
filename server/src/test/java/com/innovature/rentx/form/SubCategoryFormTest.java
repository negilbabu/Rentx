package com.innovature.rentx.form;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SubCategoryFormTest {
   
    @Test
    public void testConstructor() {
        SubCategoryForm actualSubCategoryForm = new SubCategoryForm();
        actualSubCategoryForm.setCategoryId("42");
        actualSubCategoryForm.setName("Name");
        assertEquals("42", actualSubCategoryForm.getCategoryId());
        assertEquals("Name", actualSubCategoryForm.getName());
    }
}

