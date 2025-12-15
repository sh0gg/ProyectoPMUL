package com.example.examendbr;

public class Televisor {
    private Bar bar;
    private int id;
    private Canal canalActual;
    private String estatus;
    private String detalles;

    private boolean ocupandoSuscripcion;

    public Televisor(Bar bar, int id) {
        this.bar = bar;
        this.id = id;
        this.canalActual = null;
        this.estatus = "Todavía no estás viendo nada";
        this.detalles = "";
        this.ocupandoSuscripcion = false;
    }

    public Bar getBar() {
        return bar;
    }

    public int getId() {
        return id;
    }

    public Canal getCanalActual() {
        return canalActual;
    }

    public String getEstatus() {
        return estatus;
    }

    public String getDetalles() {
        return detalles;
    }

    public void reset() {
        canalActual = null;
        estatus = "Todavía no estás viendo nada";
        detalles = "";
        ocupandoSuscripcion = false;
    }

    public void cambiarCanal(Canal nuevoCanal) {
        if (canalActual != null && ocupandoSuscripcion) {
            bar.liberarCanal(canalActual);
            ocupandoSuscripcion = false;
        }

        canalActual = nuevoCanal;

        if (nuevoCanal == null) {
            estatus = "Todavía no estás viendo nada";
            detalles = "";
            return;
        }

        if ("PUBLICO".equals(nuevoCanal.getDisponibilidad())) {
            estatus = "Viendo " + nuevoCanal.getNombre();
            detalles = "Este canal es público, ya lo está pagando.";
            ocupandoSuscripcion = false;
            return;
        }

        int posSuscrito = bar.getSuscritos().indexOf(nuevoCanal);

        if (posSuscrito < 0) {
            estatus = "No estás suscrito a " + nuevoCanal.getNombre();
            detalles = "Pulsa el botón para suscribirte a este canal.";
            ocupandoSuscripcion = false;
            return;
        }

        bar.usarCanal(nuevoCanal);
        ocupandoSuscripcion = true;

        int enUso = bar.getEnUso()[posSuscrito];
        int maxPantallas = bar.getSuscripciones().get(posSuscrito);

        if (enUso <= maxPantallas) {
            estatus = "Viendo " + nuevoCanal.getNombre();
            int precioActual = nuevoCanal.getCuota()
                    + nuevoCanal.getPrecioPorPantalla() * maxPantallas;
            detalles = "Pago actual = " + precioActual + "€";
        } else {
            bar.liberarCanal(nuevoCanal);
            ocupandoSuscripcion = false;

            estatus = "Suscripción agotada. Apague otra TV.";
            int precioActual = nuevoCanal.getCuota()
                    + nuevoCanal.getPrecioPorPantalla() * maxPantallas;
            int precioNuevo = nuevoCanal.getCuota()
                    + nuevoCanal.getPrecioPorPantalla() * (maxPantallas + 1);
            detalles = "Pulse el botón para añadir una suscripción. "
                    + "Pago actual = " + precioActual + "€, "
                    + "si añade una nueva suscripción el precio será de "
                    + precioNuevo + "€";
        }
    }
}
