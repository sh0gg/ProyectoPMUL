package com.example.examenrecdbr;

import java.util.List;

public class Ordenador {
    private int id;
    private String status;

    public Ordenador(int id) {
        this.id = id;
        this.status = "off";
    }

    public int getID() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void cambiarEstado(String boton) {
        switch (boton) {
            case "onoff":
                if (status.equals("off")) {
                    status = "on";
                } else if (status.equals("on")) {
                    status = "off";
                } else if (status.equals("hibernando")) {
                    status = "on";
                }
            case "hibernar":
                if (status.equals("off")) {
                    status = "off";
                } else if (status.equals("on")) {
                    status = "hibernando";
                } else if (status.equals("hibernando")) {
                    status = "on";
                }
        }
    }

}
