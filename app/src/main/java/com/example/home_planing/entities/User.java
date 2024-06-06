package com.example.home_planing.entities;

public class User {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private boolean enable = true;


    public User() {}

    public User(Long id, String nombre, String apellido, String email) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.enable = true;

    }

    @Override
    public String toString() {
        return "User [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", email=" + email
                + ", enable= "+ enable + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

}