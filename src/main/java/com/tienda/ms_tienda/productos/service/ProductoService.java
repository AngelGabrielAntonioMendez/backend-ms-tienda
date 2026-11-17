package com.tienda.ms_tienda.productos.service;

import com.tienda.ms_tienda.comun.exception.RecursoNoEncontradoException;
import com.tienda.ms_tienda.productos.entity.Producto;
import com.tienda.ms_tienda.productos.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service 
public class ProductoService {

    private final ProductoRepository repo;


    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }

    public List<Producto> listar(String nombre) {
        if (nombre == null || nombre.isBlank()) return repo.findAll();
        return repo.findByNombreContainingIgnoreCase(nombre);
    }

    public Producto obtener(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto con id " + id + " no existe"));
    }

    @Transactional
    public Producto crear(Producto producto) {
        producto.setId(null);
        return repo.save(producto);
    }

    @Transactional
    public Producto actualizar(Long id, Producto datos) {
        Producto actual = obtener(id);
        actual.setNombre(datos.getNombre());
        actual.setDescripcion(datos.getDescripcion());
        actual.setPrecio(datos.getPrecio());
        actual.setStock(datos.getStock());
        return repo.save(actual);
    }

    @Transactional
    public void eliminar(Long id) {
        repo.delete(obtener(id));
        repo.flush();
    }
}