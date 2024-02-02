package com.examen.examen2;

import java.util.Objects;

public class Elemento {
    private Integer id;
    private String nombre;
    private String simbolo;
    private int numero;
    private Estado estado;

    public Integer getId() {
        return id;
    }

    public Elemento setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return nombre;
    }

    public Elemento setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public Elemento setSimbolo(String simbolo) {
        this.simbolo = simbolo;
        return this;
    }

    public int getNumero() {
        return numero;
    }

    public Elemento setNumero(int numero) {
        this.numero = numero;
        return this;
    }

    public Estado getEstado() {
        return estado;
    }

    public Elemento setEstado(Estado estado) {
        this.estado = estado;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Elemento elemento = (Elemento) o;
        return Objects.equals(id, elemento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
