package com.synopsis.orders.repository;

import com.synopsis.orders.entity.Pedido;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends ReactiveCrudRepository<Pedido, Long> {

}