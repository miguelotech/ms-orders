package com.synopsis.orders.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoResponse {
    private Long id;
    private String cliente;
    private LocalDateTime fecha;
    private Double total;
    private String estado;
    private List<DetallePedidoResponse> detalles;
}