package com.synopsis.orders.service;

import com.synopsis.orders.entity.DetallePedido;
import com.synopsis.orders.entity.Pedido;
import com.synopsis.orders.entity.dto.PedidoRequest;
import com.synopsis.orders.entity.dto.PedidoResponse;
import com.synopsis.orders.entity.dto.ProductResponse;
import com.synopsis.orders.mapper.PedidoMapper;
import com.synopsis.orders.repository.DetallePedidoRepository;
import com.synopsis.orders.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
//import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final PedidoMapper pedidoMapper;
    private final WebClient productWebClient;

    /**
     * Lógica: guardar pedido (sin detalles) -> guardar detalles con pedidoId -> calcular total -> actualizar pedido -> devoluelve response
     */
    private Mono<ProductResponse> getProduct(Long id) {
        return productWebClient.get()
                .uri("/api/products/{id}", id)
                .retrieve()
                .bodyToMono(ProductResponse.class)
                .onErrorResume(e -> Mono.error(new RuntimeException("Producto no encontrado: " + id)));
    }

    public Mono<PedidoResponse> crearPedido(PedidoRequest request) {
        Pedido pedido = pedidoMapper.toPedido(request);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");

        // Validar productos y obtener sus precios
        return Flux.fromIterable(request.getDetalles())
                .flatMap(detalle -> getProduct(detalle.getProductoId())
                        .map(product -> {
                            DetallePedido det = pedidoMapper.toDetallePedido(detalle);
                            det.setPrecioUnitario(product.getPrecio());
                               det.setNombreProducto(product.getNombre());
                               det.setSubtotal(det.getCantidad() * product.getPrecio());
                               return det;
                        }))
                .collectList()
                .flatMap(detalles -> {
                    // Guardar pedido
                    return pedidoRepository.save(pedido)
                            .flatMap(saved -> {
                                // Asignar pedidoId a los detalles
                                detalles.forEach(det -> det.setPedidoId(saved.getId()));
                                
                                return detallePedidoRepository.saveAll(Flux.fromIterable(detalles))
                                        .collectList()
                                        .flatMap(savedDetalles -> {
                                            double total = savedDetalles.stream()
                                                    .mapToDouble(det -> det.getCantidad() * det.getPrecioUnitario())
                                                    .sum();
                                            saved.setTotal(total);
                                            saved.setDetalles(savedDetalles);
                                            return pedidoRepository.save(saved);
                                        })
                                        .map(pedidoMapper::toPedidoResponse);
                            });
                });
    }

    public Flux<PedidoResponse> listarPedidos() {
        return pedidoRepository.findAll()
                .flatMap(p -> detallePedidoRepository.findByPedidoId(p.getId())
                           .flatMap(detalle -> getProduct(detalle.getProductoId())
                               .map(product -> {
                                   detalle.setNombreProducto(product.getNombre());
                                   detalle.setSubtotal(detalle.getCantidad() * detalle.getPrecioUnitario());
                                   return detalle;
                               })
                               .onErrorResume(e -> {
                                   detalle.setNombreProducto("Producto no encontrado");
                                   detalle.setSubtotal(detalle.getCantidad() * detalle.getPrecioUnitario());
                                   return Mono.just(detalle);
                               }))
                           .collectList()
                        .map(detalles -> {
                            p.setDetalles(detalles);
                            return pedidoMapper.toPedidoResponse(p);
                        })
                );
    }

    public Mono<PedidoResponse> obtenerPedido(Long id) {
        return pedidoRepository.findById(id)
                .flatMap(p -> detallePedidoRepository.findByPedidoId(p.getId())
                           .flatMap(detalle -> getProduct(detalle.getProductoId())
                               .map(product -> {
                                   detalle.setNombreProducto(product.getNombre());
                                   detalle.setSubtotal(detalle.getCantidad() * detalle.getPrecioUnitario());
                                   return detalle;
                               })
                               .onErrorResume(e -> {
                                   detalle.setNombreProducto("Producto no encontrado");
                                   detalle.setSubtotal(detalle.getCantidad() * detalle.getPrecioUnitario());
                                   return Mono.just(detalle);
                               }))
                           .collectList()
                        .map(detalles -> {
                            p.setDetalles(detalles);
                            return pedidoMapper.toPedidoResponse(p);
                        }))
                .switchIfEmpty(Mono.error(new RuntimeException("Pedido no encontrado")));
    }

    public Mono<Void> eliminarPedido(Long id) {
        return pedidoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Pedido no encontrado")))
                .flatMap(pedido -> detallePedidoRepository.deleteByPedidoId(pedido.getId())
                        .then(pedidoRepository.deleteById(pedido.getId())));
    }
}