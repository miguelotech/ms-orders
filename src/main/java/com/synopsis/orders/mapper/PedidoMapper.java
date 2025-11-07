package com.synopsis.orders.mapper;

import com.synopsis.orders.entity.Pedido;
import com.synopsis.orders.entity.DetallePedido;
import com.synopsis.orders.entity.dto.PedidoRequest;
import com.synopsis.orders.entity.dto.PedidoResponse;
import com.synopsis.orders.entity.dto.DetallePedidoRequest;
import com.synopsis.orders.entity.dto.DetallePedidoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PedidoMapper {
    PedidoMapper INSTANCE = Mappers.getMapper(PedidoMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fecha", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "estado", constant = "PENDIENTE")
    @Mapping(target = "total", ignore = true)
    Pedido toPedido(PedidoRequest request);

    PedidoResponse toPedidoResponse(Pedido pedido);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedidoId", ignore = true)
    DetallePedido toDetallePedido(DetallePedidoRequest request);

    DetallePedidoResponse toDetallePedidoResponse(DetallePedido detalle);
}