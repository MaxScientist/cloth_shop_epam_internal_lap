package com.epam.shop.controller;


import com.epam.shop.entity.Vendor;
import com.epam.shop.mapper.VendorMapper;
import com.epam.shop.service.impl.VendorServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional

public class VendorControllerTest {

    @MockBean
    VendorServiceImpl vendorService;

    @Autowired
    private VendorMapper vendorMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getAllVendors() throws Exception{
        Vendor vendor = new Vendor();
        vendor.setId(1);
        vendor.setName("1st Vendor");
        vendor.setProducts(new ArrayList<>());


        Mockito.when(vendorService.findAll()).thenReturn(List.of(vendor));

        this.mockMvc.perform(get("/api/vendors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].name", Matchers.is("1st Vendor")));

//        verify(vendorService, times(1)).getAllVendors();
    }
}
