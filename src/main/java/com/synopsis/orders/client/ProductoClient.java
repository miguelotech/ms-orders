package com.synopsis.orders.client;

import com.synopsis.orders.entity.dto.ProductoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductoClient {
    
    private final WebClient productoWebClient;
    
    public Mono<ProductoDTO> obtenerProducto(Long id) {
        return productoWebClient.get()
                .uri("/api/productos/{id}", id)
                .retrieve()
                .bodyToMono(ProductoDTO.class);
    }
    
    public Mono<Void> actualizarStock(Long id, Integer cantidad) {
        return productoWebClient.put()
                .uri(uriBuilder -> uriBuilder
                    .path("/api/productos/{id}/stock")
                    .queryParam("cantidad", cantidad)
                    .build(id))
                .retrieve()
                .bodyToMono(Void.class);
    }
}