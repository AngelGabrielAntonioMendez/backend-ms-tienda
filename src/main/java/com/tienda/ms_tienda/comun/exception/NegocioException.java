package com.tienda.ms_tienda.comun.exception;

public class NegocioException extends RuntimeException {
    public NegocioException(String mensaje) { super(mensaje); }
}