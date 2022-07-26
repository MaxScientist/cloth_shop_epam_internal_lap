package com.epam.shop.controller;

import com.epam.shop.dto.OrderDTO;
import com.epam.shop.entity.Order;
import com.epam.shop.entity.Status;
import com.epam.shop.mapper.DTOMapper;
import com.epam.shop.service.impl.OrderServiceImpl;
import com.epam.shop.service.impl.ProductServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerTest {
    
    private static final int PRODUCT_ID = 9;
    private static final int ORDER_ID = 3;
    private static final int ORDER_ITEM_ID = 3;
    
    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DTOMapper<Order, OrderDTO> orderMapper;
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void findAllOrders() throws Exception {
        Order order = createOrder();
        orderService.save(order);

        String httpResponse = this.mockMvc.perform(get("/api/orders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<OrderDTO> orderDTOS = objectMapper.readValue(httpResponse, new TypeReference<>() {});
        List<Order> ordersFromDTO = orderDTOS.stream().map(orderMapper::fromDTO).collect(Collectors.toList());

        List<Order> fromDB = orderService.findAll();

        assertEquals(fromDB.toString(), ordersFromDTO.toString());
    }

    @Test
    void deleteOrderById() throws Exception{
        Order order = createOrder();
        order = orderService.save(order);

        this.mockMvc.perform(delete("/api/orders/"+order.getId()))
                .andDo(print())
                .andExpect(status().isOk());
        try {
            orderService.findById(order.getId());
            System.out.println("Entity has not been deleted!");
        } catch (EntityNotFoundException e) {
            System.out.println("successfully deleted!");
        }
    }

    @Test
    void cancelOrder() throws Exception {
        Order order = createOrder();
        order = orderService.save(order);

        this.mockMvc.perform(post("/api/orders/"+order.getId()+"/cancel"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Status canceledOrder = Status.CANCELED;
        Order orderDB = orderService.findById(order.getId());

        assertEquals(canceledOrder, orderDB.getStatus());
    }

@Test
    void findOrderById() throws Exception {
        Order order = createOrder();
        order = orderService.save(order);

        String httpResponse = this.mockMvc.perform(get("/api/orders/"+order.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderDTO responseDTOOrder = objectMapper.readValue(httpResponse, new TypeReference<>() {});
        Order responseOrder = orderMapper.fromDTO(responseDTOOrder);

        Order orderDB = orderService.findById(order.getId());

        assertEquals(orderDB, responseOrder);
    }

    @Test
    void addProductToOrder() throws Exception {

    }


    private Order createOrder() {
        Order order = new Order();
        order.setId(ORDER_ID);
        order.setStatus(Status.PLACED);
        order.setItems(new ArrayList<>());
        return order;
    }

}
