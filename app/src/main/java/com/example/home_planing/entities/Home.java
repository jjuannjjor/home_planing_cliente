package com.example.home_planing.entities;

public class Home {
    private Long id;
    private String houseCode;
    private String nombre;

    public Home(){}

    public Home(String houseCode,String nombre) {
        super();
        this.houseCode = houseCode;
        this.nombre = nombre;

    }

    @Override
    public String toString() {
        return "Home [id=" + id + ", houseCode=" + houseCode + ", name=" + nombre + "]";
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getHouseCode() {return houseCode;}

    public void setHouseCode(String houseCode) {this.houseCode = houseCode;}

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}
}
