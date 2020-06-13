package com.chat.projectprg.Entidades;

import com.chat.projectprg.Entidades.Mensaje;

public class MensajeRecibir extends Mensaje {

    private  Long hora;

    public MensajeRecibir() {
    }

    public MensajeRecibir(Long hora) {
        this.hora = hora;
    }

    public MensajeRecibir(String mensaje, String nombre, String type_mensaje, Long hora) {
        super(mensaje, nombre, type_mensaje);
        this.hora = hora;
    }

    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;
    }
}
