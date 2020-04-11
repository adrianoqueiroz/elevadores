/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author adria_000
 */
public class Elevador implements Serializable {

    private int numero;
    private float capacidadePeso;
    private int capacidadePessoas;
    private int andar;
    //subindo ou descendo
    private String estado;
    private String objetivo;
    
    private String local;
    
    private Collection<Integer> paradasOrigem;
    private Collection<Integer> paradasDestino;
    
    private Collection<Usuario> usuarioCollection;

    public Elevador() {
        this.objetivo = "Nenhum";
        this.estado = "Parado";
        this.usuarioCollection = new ArrayList<>();
        this.paradasOrigem = new ArrayList<>();
        this.paradasDestino = new ArrayList<>();
    }

    public float getCapacidadePeso() {
        return capacidadePeso;
    }

    public void setCapacidadePeso(float capacidadePeso) {
        this.capacidadePeso = capacidadePeso;
    }

    public int getCapacidadePessoas() {
        return capacidadePessoas;
    }

    public void setCapacidadePessoas(int capacidadePessoas) {
        this.capacidadePessoas = capacidadePessoas;
    }

    public int getAndar() {
        return andar;
    }

    public void setAndar(int andar) {
        this.andar = andar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Collection<Usuario> getUsuarioCollection() {
        return usuarioCollection;
    }

    public void setUsuarioCollection(Collection<Usuario> usuarioCollection) {
        this.usuarioCollection = usuarioCollection;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public float calculaCarga() {
        float carga = 0;
        for (Usuario usuario : usuarioCollection) {
            carga += usuario.getPeso();
        }
        return carga;
    }

    public float calculaCargaRestante() {
        return getCapacidadePeso() - calculaCarga();
    }

    public Collection<Integer> getParadasOrigem() {
        return paradasOrigem;
    }

    public void setParadasOrigem(Collection<Integer> paradasOrigem) {
        this.paradasOrigem = paradasOrigem;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public Collection<Integer> getParadasDestino() {
        return paradasDestino;
    }

    public void setParadasDestino(Collection<Integer> paradasDestino) {
        this.paradasDestino = paradasDestino;
    }
    
    public Collection<Integer> getTodasParadas(){
        Collection<Integer> todasParadas = new ArrayList<>();
        
        for(int i : getParadasOrigem()){
            if(!todasParadas.contains(i)){
                todasParadas.add(i);
            }
        }
        
        for(int j : getParadasDestino()){
            if(!todasParadas.contains(j)){
                todasParadas.add(j);
            }
        }
        return todasParadas;
    }
    
}
