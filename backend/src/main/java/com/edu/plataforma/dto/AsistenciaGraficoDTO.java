package com.edu.plataforma.dto;

public class AsistenciaGraficoDTO {
    private String etiqueta;
    private Number valor;
    private String categoria;

    public AsistenciaGraficoDTO() {}

    public AsistenciaGraficoDTO(String etiqueta, Number valor, String categoria) {
        this.etiqueta = etiqueta;
        this.valor = valor;
        this.categoria = categoria;
    }

    public String getEtiqueta() { return etiqueta; }
    public void setEtiqueta(String etiqueta) { this.etiqueta = etiqueta; }
    public Number getValor() { return valor; }
    public void setValor(Number valor) { this.valor = valor; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}