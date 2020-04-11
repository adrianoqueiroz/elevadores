/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author adria_000
 */
public class Usuario {

    private String nome;
    private float peso;
    private int andarOrigem;
    private int andarDestino;

    public Usuario() {
        Integer nomeHash = this.hashCode();
        this.nome = nomeHash.toString();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public int getAndarOrigem() {
        return andarOrigem;
    }

    public void setAndarOrigem(int andarOrigem) {
        this.andarOrigem = andarOrigem;
    }

    public int getAndarDestino() {
        return andarDestino;
    }

    public void setAndarDestino(int andarDestino) {
        this.andarDestino = andarDestino;
    }

    public String verificaObjetivo() {
        if (this.getAndarOrigem() > this.getAndarDestino()) {
            return "Descer";
        } else if (this.getAndarOrigem() < this.getAndarDestino()) {
            return "Subir";
        } else {
            return "Parar";
        }
    }

}
