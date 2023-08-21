package com.innovature.rentx.form;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CategoryFormTest {
  
    @Test
    public void testConstructor() {
        CategoryForm actualCategoryForm = new CategoryForm();
        actualCategoryForm.setName("Name");
        assertEquals("Name", actualCategoryForm.getName());
    }
}

