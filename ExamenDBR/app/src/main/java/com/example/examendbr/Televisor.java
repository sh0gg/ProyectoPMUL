package com.example.examendbr;

public class Televisor {
    private Bar bar;
    private int id;
    private Canal canalActual;
    private String estatus;
    private String detalles;

    public Televisor(Bar bar,int id) {
        this.bar = bar;
        this.id = id;
        this.canalActual = null;
        this.estatus = "";
        this.detalles = "";
    }

    public int getId() {
        return id;
    }

    public Canal getCanalActual() {
        return canalActual;
    }

    public void cambiarCanal(Canal nuevoCanal) {
        if (nuevoCanal.getDisponibilidad().equals("PAGO")) {
            bar.liberarCanal(canalActual);
            if (bar.getEnUso()[bar.getCanalesSuscritos().indexOf(nuevoCanal)]++ < bar.getSuscripcionesCanal().get(bar.getCanalesSuscritos().indexOf(nuevoCanal))) {
                bar.usarCanal(nuevoCanal);
                canalActual = nuevoCanal;
                estatus = "Esta viendo:" + canalActual.getNombre();
                int precioActual = canalActual.getCuota() + (canalActual.getPrecioPorPantalla() * bar.getSuscripcionesCanal().get(bar.getCanalesSuscritos().indexOf(canalActual)));
                detalles = "Pago actual = " + precioActual;
            } else {
                bar.usarCanal(nuevoCanal);
                canalActual = nuevoCanal;
                int precioActual = nuevoCanal.getCuota() + (canalActual.getPrecioPorPantalla() * bar.getSuscripcionesCanal().get(bar.getCanalesSuscritos().indexOf(nuevoCanal)));
                int precioNuevo = nuevoCanal.getCuota() + (nuevoCanal.getPrecioPorPantalla() * (bar.getSuscripcionesCanal().get(bar.getCanalesSuscritos().indexOf(nuevoCanal)) + 1));
                estatus = "Suscripción agotada. Apague otra TV.";
                detalles = "Pulse el botón para añadir una suscripción. Pago actual = " + precioActual + "€, si añade una nueva suscripción el precio será de " + precioNuevo + "€";
            }
        } else if (nuevoCanal.getDisponibilidad().equals("PUBLICO")) {
            bar.liberarCanal(canalActual);
            bar.usarCanal(nuevoCanal);
            canalActual = nuevoCanal;
            estatus= "Esta viendo:" + canalActual.getNombre();
            detalles= "Este canal es público, ya lo está pagando! :3";
        }
    }


}
