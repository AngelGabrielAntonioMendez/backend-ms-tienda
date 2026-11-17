package com.tienda.ms_tienda.ventas.repository;

import com.tienda.ms_tienda.ventas.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findAllByOrderByFechaDesc();   // Las más recientes primero
}