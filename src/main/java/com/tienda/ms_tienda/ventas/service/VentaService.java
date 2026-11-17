package com.tienda.ms_tienda.ventas.service;

import com.tienda.ms_tienda.comun.exception.NegocioException;
import com.tienda.ms_tienda.comun.exception.RecursoNoEncontradoException;
import com.tienda.ms_tienda.productos.entity.Producto;
import com.tienda.ms_tienda.productos.service.ProductoService;
import com.tienda.ms_tienda.ventas.dto.ItemVentaRequest;
import com.tienda.ms_tienda.ventas.dto.VentaRequest;
import com.tienda.ms_tienda.ventas.entity.DetalleVenta;
import com.tienda.ms_tienda.ventas.entity.Venta;
import com.tienda.ms_tienda.ventas.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepo;
    private final ProductoService productoService;  // ← usamos el SERVICIO del otro módulo

    public VentaService(VentaRepository ventaRepo, ProductoService productoService) {
        this.ventaRepo = ventaRepo;
        this.productoService = productoService;
    }

    public List<Venta> listar() {
        return ventaRepo.findAllByOrderByFechaDesc();
    }

    public Venta obtener(Long id) {
        return ventaRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta con id " + id + " no existe"));
    }

    @Transactional
    public Venta registrar(VentaRequest request) {
        Venta venta = new Venta();
        venta.setCliente(request.cliente());
        venta.setFecha(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        for (ItemVentaRequest item : request.items()) {
            Producto producto = productoService.obtener(item.productoId());

            // Regla de negocio #1: no vender más de lo que hay
            if (producto.getStock() < item.cantidad()) {
                throw new NegocioException("Stock insuficiente para '" + producto.getNombre()
                        + "'. Disponible: " + producto.getStock());
            }

            // Regla #2: descontar el inventario
            producto.setStock(producto.getStock() - item.cantidad());

            // Regla #3: el precio lo pone el SERVIDOR, no el cliente
            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(item.cantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(item.cantidad())));

            venta.agregarDetalle(detalle);
            total = total.add(detalle.getSubtotal());
        }

        venta.setTotal(total);
        return ventaRepo.save(venta);  // Guarda la venta Y sus detalles (cascade)
    }

    @Transactional
    public void cancelar(Long id) {
        Venta venta = obtener(id);
        // Al cancelar, devolvemos el stock al inventario
        for (DetalleVenta d : venta.getDetalles()) {
            Producto p = d.getProducto();
            p.setStock(p.getStock() + d.getCantidad());
        }
        ventaRepo.delete(venta);  // ON DELETE CASCADE / orphanRemoval borra los detalles
    }
}