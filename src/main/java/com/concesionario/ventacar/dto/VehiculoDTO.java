package com.concesionario.ventacar.dto;

public class VehiculoDTO {
    private Long id;
    private String marca;
    private String tipo;
    private Integer precio;
    private String imagen;
    private String descripcion;

    public VehiculoDTO() {

    }

    public VehiculoDTO(Long id, String marca, String tipo, Integer precio, String imagen, String descripcion) {
        this.id = id;
        this.marca = marca;
        this.tipo = tipo;
        this.precio = precio;
        this.imagen = imagen;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
