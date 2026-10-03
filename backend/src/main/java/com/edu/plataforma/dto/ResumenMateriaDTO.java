package com.edu.plataforma.dto;

public class ResumenMateriaDTO {
    private Long materiaId;
    private String nombreMateria;
    private String codigo;
    private double promedio;
    private double desviacion;
    private double minimo;
    private double maximo;
    private long totalEstudiantes;
    private double porcentajeReprobacion;

    public ResumenMateriaDTO() {}

    public ResumenMateriaDTO(Long materiaId, String nombreMateria, String codigo, double promedio, double desviacion, double minimo, double maximo, long totalEstudiantes, double porcentajeReprobacion) {
        this.materiaId = materiaId;
        this.nombreMateria = nombreMateria;
        this.codigo = codigo;
        this.promedio = promedio;
        this.desviacion = desviacion;
        this.minimo = minimo;
        this.maximo = maximo;
        this.totalEstudiantes = totalEstudiantes;
        this.porcentajeReprobacion = porcentajeReprobacion;
    }

    public Long getMateriaId() { return materiaId; }
    public void setMateriaId(Long materiaId) { this.materiaId = materiaId; }
    public String getNombreMateria() { return nombreMateria; }
    public void setNombreMateria(String nombreMateria) { this.nombreMateria = nombreMateria; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public double getPromedio() { return promedio; }
    public void setPromedio(double promedio) { this.promedio = promedio; }
    public double getDesviacion() { return desviacion; }
    public void setDesviacion(double desviacion) { this.desviacion = desviacion; }
    public double getMinimo() { return minimo; }
    public void setMinimo(double minimo) { this.minimo = minimo; }
    public double getMaximo() { return maximo; }
    public void setMaximo(double maximo) { this.maximo = maximo; }
    public long getTotalEstudiantes() { return totalEstudiantes; }
    public void setTotalEstudiantes(long totalEstudiantes) { this.totalEstudiantes = totalEstudiantes; }
    public double getPorcentajeReprobacion() { return porcentajeReprobacion; }
    public void setPorcentajeReprobacion(double porcentajeReprobacion) { this.porcentajeReprobacion = porcentajeReprobacion; }
}