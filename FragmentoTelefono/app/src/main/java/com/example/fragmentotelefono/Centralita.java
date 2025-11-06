package com.example.fragmentotelefono;

import java.util.List;

public class Centralita {
    private List<Telefono> telefonos;

    public void registrarTelefono(Telefono telefono) {
        telefonos.add(telefono);
    }

    public Telefono getTelefono(int id) {
        for (Telefono telefono : telefonos) {
            if (telefono.getId() == id) {
                return telefono;
            }
        }
        return null;
    }

    public boolean llamar(int idOrigen, int idDestino) {
        Telefono origen = getTelefono(idOrigen);

        if (origen == null || origen.isOcupado()) return false;

        if (idDestino >= 1 && idDestino <= telefonos.size()) {
            Telefono destino = getTelefono(idDestino);
            if (destino != null && !destino.isOcupado()) {
                origen.setOcupado(true);
                destino.setOcupado(true);
                origen.setHablaCon(destino);  // origen guarda el telefono destino
                destino.setHablaCon(origen);  // destino guarda el telefono origen
                return true;
            } else {
                return false;  // destino esta ocupado
            }
        } else {  // llamada externa (número fuera de la lista)
            origen.setOcupado(true);
            origen.setHablaCon(new Telefono(idDestino));  // se guarda un nuevo telefono con el ID del número
            return true;
        }
    }

    public void terminarLlamada(int idOrigen) {
        Telefono origen = getTelefono(idOrigen);

        if (origen != null) {
            Telefono destino = origen.getHablaCon();

            // liberamos los tlfs
            if (destino != null) {
                destino.setOcupado(false);
                destino.setHablaCon(null);
            }

            origen.setOcupado(false);
            origen.setHablaCon(null);
        }
    }
}
