package com.learning.kafka.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.kafka.brokers.OrderMessageProducer;
import com.learning.kafka.dtos.OrderDTO;
import com.learning.kafka.entities.Order;
import com.learning.kafka.entities.OrderItem;
import com.learning.kafka.messages.OrderMessage;
import com.learning.kafka.repositories.OrderRepository;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderMessageProducer orderMessageProducer;
	

	@PostMapping
	public ResponseEntity<OrderDTO> creatOrder(@RequestBody OrderDTO orderDTO){
		
		if(!StringUtils.isEmpty(orderDTO.getOrderId())) {
			throw new RuntimeException("Order can not have orderID while creating, passed value "+orderDTO.getOrderId());
		}
		//could have moved to service class
		//creating onrder only without items
		Order order = new Order(orderDTO);
		orderDTO.getOrderItems()
				.forEach(orderItemDTO -> {
					order.addItem(new OrderItem(orderItemDTO));
				});
		//cascade is all and hence it saves even orderitems
		Order savedOrder = orderRepository.save(order);
		OrderDTO returnOrderDTO = new OrderDTO(savedOrder);
		
		//after creating order item lets push to topic
		savedOrder.getOrderItems().forEach(orderItem ->{
			orderMessageProducer.produceOrderMessage(new OrderMessage(savedOrder, orderItem));
		});
		return new ResponseEntity<>(returnOrderDTO, HttpStatus.CREATED);
		
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderDTO> findORderById(@PathVariable String orderId){
		if(StringUtils.isEmpty(orderId)) {
			throw new RuntimeException("Invalid Order ID  passed while retrieving order by id, passed value "+orderId);
		}
		Optional<Order> findById = orderRepository.findById(Integer.parseInt(orderId));
		return findById
			.map(order -> new OrderDTO(order))
			.map(order -> new ResponseEntity<>(order, HttpStatus.OK))
			.orElseThrow(() -> new RuntimeException("No Order present with passed order id "+orderId));
		
	}
}
