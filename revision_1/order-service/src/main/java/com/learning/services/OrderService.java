package com.learning.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.learning.dtos.OrderDTO;
import com.learning.entities.Order;
import com.learning.entities.OrderItem;
import com.learning.messages.FlashSale;
import com.learning.messages.InventoryItem;
import com.learning.messages.ItemFeedBack;
import com.learning.messages.OnlineOrderMessage;
import com.learning.messages.OrderFeedback;
import com.learning.messages.OrderMessage;
import com.learning.repositories.OrderRepository;

@Service
public class OrderService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private PromotionDiscountService promotionDiscountService;

	public void crateAndPublish(OrderDTO orderDTO) {
		logger.info("start processing order "+orderDTO);
		Order order = new Order(orderDTO);
		
		List<OrderItem> orderItems = orderDTO.getOrderItems().stream().map(OrderItem::new).collect(Collectors.toList());
		
		orderItems.forEach(order::addItem);
//cascade is all and hence it saves even orderitems
		Order savedOrder = orderRepository.save(order);
		
		//saved order have generated ids and hence we can pass one by one
		//use savedOrder items individually to send n messages based on size of order item
		
		savedOrder.getOrderItems().stream()
					.map(savedORderITem -> new OrderMessage(savedOrder, savedORderITem))
					.forEach(orderMessage -> {
						kafkaTemplate.send("order-topic", orderMessage.getOrderNumber(), orderMessage);
					//just for demo
					//in same topic we can pass discount, pmortion, order as well as object
					//promotionDiscountService.sendTestableOrderMessage(orderMessage);
					});
					;
	}

	public void publishOrderFeedBack(OrderFeedback orderFeedback) {
		orderFeedback.setFeedbackTime(LocalDateTime.now());
		logger.info("publishOrderFeedBack: publishing order feedback "+orderFeedback);
		kafkaTemplate.send("order-feedback", orderFeedback);
	}

	public void publishFlashSale(FlashSale flashSale) {
		logger.info("publishFlashSale: publishing flashsale "+flashSale);

		kafkaTemplate.send("flashsale", flashSale);
	}
	
	public void publishFeedBack(ItemFeedBack itemFeedBack) {
		logger.info("publishFeedBack: publishing itemFeedBack "+itemFeedBack);

		kafkaTemplate.send("itemFeedBack", itemFeedBack);
	}

	public void publishInventoryUpdate(InventoryItem item) {
		logger.info("publishInventoryUpdate: publishing inventory update "+item);

		ProducerRecord<String, Object> producerRecord = new ProducerRecord<>("inventoryupdate", item.getItemName(), item);
		kafkaTemplate.send(producerRecord);
			
	}
	
	public void publishOnlineOrder(OnlineOrderMessage messageitem) {
		messageitem.setCreationDate(LocalDateTime.now());
		logger.info("publishOnlineOrder: publishing onlineorder update "+messageitem);
		
		kafkaTemplate.send("online-order", messageitem.getOrderId(), messageitem);
	}
}
