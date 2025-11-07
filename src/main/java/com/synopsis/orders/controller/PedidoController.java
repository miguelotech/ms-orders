package com.synopsis.orders.controller;

import com.synopsis.orders.entity.dto.PedidoRequest;
import com.synopsis.orders.entity.dto.PedidoResponse;
import com.synopsis.orders.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PedidoResponse> crearPedido(@RequestBody PedidoRequest request) {
        return pedidoService.crearPedido(request);
    }

    @GetMapping
    public Flux<PedidoResponse> listarPedidos() {
        return pedidoService.listarPedidos();
    }

    @GetMapping("/{id}")
    public Mono<PedidoResponse> obtenerPedido(@PathVariable Long id) {
        return pedidoService.obtenerPedido(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarPedido(@PathVariable Long id) {
        return pedidoService.eliminarPedido(id);
    }
}