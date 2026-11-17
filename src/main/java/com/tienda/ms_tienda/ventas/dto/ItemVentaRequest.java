package com.tienda.ms_tienda.ventas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemVentaRequest(
        @NotNull Long productoId,
        @NotNull @Min(value = 1, message = "debe ser al menos 1") Integer cantidad
) {}