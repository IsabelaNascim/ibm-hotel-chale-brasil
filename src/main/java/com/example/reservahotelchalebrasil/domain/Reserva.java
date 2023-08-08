package com.example.reservahotelchalebrasil.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Reserva implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReserva;
    private String hospede;
    private int quantHospedes;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date checkIn;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date checkOut;
    @Enumerated(EnumType.STRING)
    private StatusReserva status;


    public Reserva(){}

    public Reserva(int idReserva, String maria_josé_pereira, int quantHospedes, Date ckeckIn, Date checkOut, String confirmada) { }

    public Reserva(Integer idReserva, String hospede, int quantHospedes, Date checkIn, Date checkOut, StatusReserva status) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.idReserva = idReserva;
        this.hospede = hospede;
        this.quantHospedes = quantHospedes;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
    }

    // Getters
    public Integer getIdReserva() {return idReserva;}

    public String getHospede() {return hospede;}

    public int getQuantHospedes() {return quantHospedes;}
    public Date getCheckIn() {return checkIn;}

    public Date getCheckOut() {return checkOut;}

    public StatusReserva getStatus() {return status;}

    // Setters
    public void setIdReserva(Integer idReserva) {this.idReserva = idReserva;}

    public void setHospede(String hospede) {this.hospede = hospede;}

    public void setQuantHospedes(int quantHospedes) {this.quantHospedes = quantHospedes;}

    public void setCheckIn(Date checkIn) {this.checkIn = checkIn;}

    public void setCheckOut(Date checkOut) {this.checkOut = checkOut;}

    public void setStatus(StatusReserva status) {this.status = status;}

}
