package com.tienda.ms_tienda.productos.repository;

import com.tienda.ms_tienda.productos.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
   
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}