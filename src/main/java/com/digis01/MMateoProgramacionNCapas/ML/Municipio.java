package com.digis01.MMateoProgramacionNCapas.ML;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Municipio {

    private int IdMunicipio;
    private String Nombre;
    @JsonIgnore
    public Estado Estado;

    public int getIdMunicipio() {
        return IdMunicipio;
    }

    public void setIdMunicipio(int IdMunicipio) {
        this.IdMunicipio = IdMunicipio;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public Estado getEstado() {
        return Estado;
    }

    public void setEstado(Estado Estado) {
        this.Estado = Estado;
    }

}
