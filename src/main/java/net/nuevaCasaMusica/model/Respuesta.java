package net.nuevaCasaMusica.model;

public class Respuesta {

    private String estado;
    private String msg;

    public Respuesta() {
    }

    public Respuesta(String estado, String msg) {
        this.estado = estado;
        this.msg = msg;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
