package com.epam.shop.controller;


import com.epam.shop.dto.ProductDTO;
import com.epam.shop.dto.VendorDTO;
import com.epam.shop.entity.Product;
import com.epam.shop.entity.Vendor;
import com.epam.shop.mapper.DTOMapper;
import com.epam.shop.service.impl.VendorServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@WithMockUser
public class VendorControllerTest {

    private static final String VENDOR_NAME = "1st Vendor";
    private static final Integer VENDOR_ID = 1;
    private static final int PRODUCT_ID = 1;
    private static final Integer CATEGORY_ID = 1;


    @MockBean
    VendorServiceImpl vendorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private DTOMapper<Vendor, VendorDTO> vendorMapper;

    @Autowired
    private DTOMapper<Product, ProductDTO> productmapper;

    @Test
    void getAllVendors() throws Exception {
        Vendor vendor = new Vendor();
        fillVendorObjectWithData(vendor);
        vendor.setProducts(new ArrayList<>());

        List<Vendor> fromDB = vendorService.findAll();


        String httpResponse = mockMvc.perform(get("/api/vendors"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<VendorDTO> vendorDTOS = objectMapper.readValue(httpResponse, new TypeReference<>() {
        });
        List<Vendor> vendors = vendorDTOS.stream().
                map(vendorMapper::fromDTO).collect(toList());

        assertEquals(fromDB.toString(), vendors.toString());
    }

    @Test
    void createVendor() throws Exception {
        Vendor vendor = new Vendor();
        fillVendorObjectWithData(vendor);

        String requestBody = objectMapper.writeValueAsString(vendorMapper.toDTO(vendor));

        Mockito.when(vendorService.save(vendor)).thenReturn(vendor);
        String httpResponse = mockMvc.perform(post("/api/vendors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        VendorDTO vendorDTO = objectMapper.readValue(httpResponse, new TypeReference<>() {
        });

        Vendor vendorFromDto = vendorMapper.fromDTO(vendorDTO);
        Mockito.when(vendorService.findById(vendorFromDto.getId())).thenReturn(vendorFromDto);
        Vendor fromDB = vendorService.findById(vendorFromDto.getId());

        assertEquals(fromDB.toString(), vendorFromDto.toString());
    }

    @Test
    void findVendorById() throws Exception {
        Vendor vendor = new Vendor();
        fillVendorObjectWithData(vendor);

        Mockito.when(vendorService.findById(vendor.getId())).thenReturn(vendor);

        String httpResponse = mockMvc.perform(get("/api/vendors/" + vendor.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        VendorDTO vendorDTO = objectMapper.readValue(httpResponse, new TypeReference<>() {
        });
        Vendor vendorFromDTO = vendorMapper.fromDTO(vendorDTO);

        assertEquals(vendor.toString(), vendorFromDTO.toString());
    }

    @Test
    void updateVendor() throws Exception {
        Vendor vendor = new Vendor();
        fillVendorObjectWithData(vendor);

        when(vendorService.save(vendor)).thenReturn(vendor);
        String requestBody = objectMapper.writeValueAsString(vendor);
        mockMvc.perform(put("/api/vendors/" + vendor.getId())
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(vendorService, times(1)).updateVendorById(VENDOR_ID, vendor);
    }

    @Test
    void deleteVendor() throws Exception {
        Vendor vendor = new Vendor();
        fillVendorObjectWithData(vendor);

        when(vendorService.findById(VENDOR_ID)).thenReturn(vendor);
        mockMvc.perform(delete("/api/vendors/" + vendor.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(vendorService, times(1)).deleteById(VENDOR_ID);
    }

    @Test
    void getProductsByVendor() throws Exception {
        Product product = createProduct();
        Vendor vendor = new Vendor();
        fillVendorObjectWithData(vendor);
        List<Product> products = List.of(product);
        vendor.setProducts(products);

        when(vendorService.findProductsByVendorId(PRODUCT_ID)).thenReturn(products);

        String httpResponse = mockMvc.perform(get("/api/vendors/" + vendor.getId() + "/products"))
                .andDo(print())
                .andExpect(status().isOk()
                ).andReturn()
                .getResponse()
                .getContentAsString();


        List<ProductDTO> productDTO = objectMapper.readValue(httpResponse, new TypeReference<>() {});

        List<Product> productsFromDB = vendorService.findProductsByVendorId(vendor.getId());
        List<ProductDTO> productDTOSFROMDB = productsFromDB.stream().map(p->productmapper.toDTO(p)).collect(toList());

        assertEquals(productDTOSFROMDB.toString(), productDTO.toString());
    }

    private Product createProduct() {
        Product product = new Product();
        product.setId(PRODUCT_ID);
        product.setCategoryId(CATEGORY_ID);
        return product;
    }


    private void fillVendorObjectWithData(Vendor vendor) {
        vendor.setId(VENDOR_ID);
        vendor.setName(VENDOR_NAME);
    }
}
