package com.nicolas.orderservice.controller;

import com.nicolas.orderservice.dto.request.OrderRequestDTO;
import com.nicolas.orderservice.dto.response.OrderResponseDTO;
import com.nicolas.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@RequestBody @Valid OrderRequestDTO orderRequestDTO) {
        orderService.createOrder(orderRequestDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponseDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
    }

}
