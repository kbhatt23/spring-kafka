package com.learning.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{

	
}
