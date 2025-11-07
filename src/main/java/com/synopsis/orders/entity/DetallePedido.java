package com.synopsis.orders.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("detalles_pedido")
public class DetallePedido {
    @Id
    private Long id;

    @Column("pedido_id")
    private Long pedidoId;

    @Column("producto_id")
    private Long productoId;
    @Transient
    private String nombreProducto;

    private Integer cantidad;

    @Column("precio_unitario")
    private Double precioUnitario;

        @Transient
        private Double subtotal;
}