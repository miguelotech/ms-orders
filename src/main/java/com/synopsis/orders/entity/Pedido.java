package com.synopsis.orders.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("pedidos")
public class Pedido {
    @Id
    private Long id;

    private String cliente;
    private LocalDateTime fecha;
    private Double total;
    private String estado; // PENDIENTE, PROCESADO, CANCELADO

    @Transient
    private List<DetallePedido> detalles;
}