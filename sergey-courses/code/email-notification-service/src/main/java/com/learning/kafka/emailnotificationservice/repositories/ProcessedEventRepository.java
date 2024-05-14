package com.learning.kafka.emailnotificationservice.repositories;

import com.learning.kafka.emailnotificationservice.entities.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedEventRepository extends JpaRepository<ProcessedEventEntity, String> {
	


}