package com.tienda.ms_tienda.productos.controller;

import com.tienda.ms_tienda.productos.entity.Producto;
import com.tienda.ms_tienda.productos.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController                   
@RequestMapping("/api/productos")  
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping                                 
    public List<Producto> listar(@RequestParam(required = false) String nombre) {
        return service.listar(nombre);
    }

    @GetMapping("/{id}")                         
    public Producto obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping                                 
    @ResponseStatus(HttpStatus.CREATED)          
    public Producto crear(@Valid @RequestBody Producto producto) {
        return service.crear(producto);
    }

    @PutMapping("/{id}")                          
    public Producto actualizar(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        return service.actualizar(id, producto);
    }

    @DeleteMapping("/{id}")                     
    @ResponseStatus(HttpStatus.NO_CONTENT)     
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}