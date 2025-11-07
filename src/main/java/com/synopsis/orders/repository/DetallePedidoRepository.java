package com.synopsis.orders.repository;

import com.synopsis.orders.entity.DetallePedido;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DetallePedidoRepository extends ReactiveCrudRepository<DetallePedido, Long> {
    Flux<DetallePedido> findByPedidoId(Long pedidoId);
    Mono<Void> deleteByPedidoId(Long pedidoId);
}