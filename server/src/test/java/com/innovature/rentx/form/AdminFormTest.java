package com.innovature.rentx.form;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AdminFormTest {

    @Test
    void testConstructor() {
        AdminForm actualAdminForm = new AdminForm();
        actualAdminForm.setEmail("jane.doe@example.org");
        actualAdminForm.setPassword("Admin123");
        assertEquals("jane.doe@example.org", actualAdminForm.getEmail());
        assertEquals("Admin123", actualAdminForm.getPassword());
    }
}

