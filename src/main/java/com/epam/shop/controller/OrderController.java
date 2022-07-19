package com.epam.shop.controller;


import com.epam.shop.config.SwaggerConfig;
import com.epam.shop.dto.OrderDTO;
import com.epam.shop.dto.ProductDTO;
import com.epam.shop.entity.Order;
import com.epam.shop.entity.Product;
import com.epam.shop.mapper.DTOMapper;
import com.epam.shop.service.impl.OrderServiceImpl;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@Api(tags = {SwaggerConfig.ORDER})
@PreAuthorize("hasAnyAuthority('READ', 'WRITE')")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;
    private final DTOMapper<Order, OrderDTO> orderMapper;
    private final DTOMapper<Product, ProductDTO> productMapper;

    @GetMapping
    public ResponseEntity<?> findAllOrders() {
        try {
            List<Order> orders = orderService.findAll();
            List<OrderDTO> orderDTOS = orders.stream().map(orderMapper::toDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(orderDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findOrderById(@PathVariable("id") int id) {
        try {
            Order order = orderService.findById(id);
            return new ResponseEntity<>(orderMapper.toDTO(order), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteOrderById(@PathVariable("id") int id) {
        try {
            orderService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}/items")
    public ResponseEntity<?> findProductsByOrderId(@PathVariable("id") int id) {
        try {
            List<Product> products = orderService.findProductsByOrderId(id);
            List<ProductDTO> productDTOS = products.stream().map(productMapper::toDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(productDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/{id}/items")
    public ResponseEntity<?> saveProductToOrder(@PathVariable("id") int id, @RequestBody ProductDTO productDTO) {
        try {
            orderService.saveProductToOrder(id, productMapper.fromDTO(productDTO));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{oid}/items/{iid}")
    public ResponseEntity<?> findProductByOrderIdItemId(@PathVariable("oid") int oid, @PathVariable("iid") int iid) {
        try {
            Product product = orderService.findProductByOrderIdItemId(oid, iid);
            return new ResponseEntity<>(productMapper.toDTO(product), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{oid}/items/{iid}")
    public ResponseEntity<?> deleteProductFromOrder(@PathVariable("oid") int oid, @PathVariable("iid") int iid) {
        try {
            orderService.deleteProductFromOrder(oid, iid);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable("id") int id) {
        try {
            orderService.cancelOrder(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
