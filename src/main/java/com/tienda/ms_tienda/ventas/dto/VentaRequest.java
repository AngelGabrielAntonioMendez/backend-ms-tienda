package com.tienda.ms_tienda.ventas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record VentaRequest(
        @NotBlank(message = "es obligatorio") String cliente,
        @NotEmpty(message = "la venta debe tener al menos un producto") List<@Valid ItemVentaRequest> items
) {}