package com.gamestore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamestore.model.Order;
import com.gamestore.model.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    List<Order> findAllByOrderByCreatedAtDesc();
}
