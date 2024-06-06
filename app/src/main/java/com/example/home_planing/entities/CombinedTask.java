package com.example.home_planing.entities;

public class CombinedTask {
    private Long id;
    private String description;
    private boolean terminado;

    public CombinedTask(Long id, String description, boolean terminado) {
        this.id = id;
        this.description = description;
        this.terminado = terminado;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return description;
    }

    public void setDescripcion(String descripcion) {
        this.description = descripcion;
    }

    public boolean isTerminado() {
        return terminado;
    }

    public void setTerminado(boolean terminado) {
        this.terminado = terminado;
    }

    @Override
    public String toString() {
        return "CombinedTask{" +
                "id=" + id +
                ", descripcion='" + description + '\'' +
                ", terminado=" + terminado +
                '}';
    }
}

