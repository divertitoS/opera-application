package com.dev.opera.controller;

import com.dev.opera.dto.response.OrderResponseDto;
import com.dev.opera.model.ShoppingCart;
import com.dev.opera.model.User;
import com.dev.opera.service.OrderService;
import com.dev.opera.service.ShoppingCartService;
import com.dev.opera.service.UserService;
import com.dev.opera.service.mapper.OrderMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final ShoppingCartService shoppingCartService;
    private final OrderService orderService;
    private final UserService userService;
    private final OrderMapper orderMapper;

    public OrderController(ShoppingCartService shoppingCartService,
                           OrderService orderService,
                           UserService userService,
                           OrderMapper orderMapper) {
        this.shoppingCartService = shoppingCartService;
        this.orderService = orderService;
        this.userService = userService;
        this.orderMapper = orderMapper;
    }

    @PostMapping("/complete")
    public OrderResponseDto completeOrder(Authentication auth) {
        UserDetails details = (UserDetails) auth.getPrincipal();
        User user = userService.findByEmail(details.getUsername());
        ShoppingCart cart = shoppingCartService.getByUser(user);
        return orderMapper.mapToDto(orderService.completeOrder(cart));
    }

    @GetMapping
    public List<OrderResponseDto> getOrderHistory(Authentication auth) {
        UserDetails details = (UserDetails) auth.getPrincipal();
        User user = userService.findByEmail(details.getUsername());
        return orderService.getOrdersHistory(user)
                .stream()
                .map(orderMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
