/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author adria_000
 */
public class Fila implements Serializable{
    private Elevador elevador;
    private Usuario usuario;
    private Custo custo;

    public Elevador getElevador() {
        return elevador;
    }

    public void setElevador(Elevador elevador) {
        this.elevador = elevador;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Custo getCusto() {
        return custo;
    }

    public void setCusto(Custo custo) {
        this.custo = custo;
    }
    
}
