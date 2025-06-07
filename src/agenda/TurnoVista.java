package com.centroestetico.agenda;

import java.time.LocalDate;

/**
 * Representa un turno listo para mostrarse en tablas o exportarse.
 */
public class TurnoVista {
    public final LocalDate fecha;
    public final String   hora;
    public final String   cliente;
    public final String   servicio;
    public final String   empleado;
    public final String   estado;

    public TurnoVista(
            LocalDate fecha,
            String hora,
            String cliente,
            String servicio,
            String empleado,
            String estado
    ) {
        this.fecha   = fecha;
        this.hora    = hora;
        this.cliente = cliente;
        this.servicio= servicio;
        this.empleado= empleado;
        this.estado  = estado;
    }
}
