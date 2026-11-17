package com.tienda.ms_tienda.ventas.controller;

import com.tienda.ms_tienda.ventas.dto.VentaRequest;
import com.tienda.ms_tienda.ventas.entity.Venta;
import com.tienda.ms_tienda.ventas.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService service;

    public VentaController(VentaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Venta> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public Venta obtener(@PathVariable Long id) { return service.obtener(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Venta registrar(@Valid @RequestBody VentaRequest request) {
        return service.registrar(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelar(@PathVariable Long id) { service.cancelar(id); }
}