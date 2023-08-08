package com.example.reservahotelchalebrasil.dto;

import com.example.reservahotelchalebrasil.domain.Reserva;
import com.example.reservahotelchalebrasil.domain.StatusReserva;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class ReservaDto implements Serializable {

    private Integer idReserva;
    private String hospede;
    private int quantHospedes;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date checkIn;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date checkOut;
    private static StatusReserva status;

    public ReservaDto(Reserva reserva){
        idReserva = reserva.getIdReserva();
        quantHospedes = reserva.getQuantHospedes();
        hospede = reserva.getHospede();
        checkIn = reserva.getCheckIn();
        checkOut = reserva.getCheckOut();
        status = reserva.getStatus();
    };

    public ReservaDto() {};

    public ReservaDto(Integer idReserva, String hospede, int quantHospedes, Date checkIn, Date checkOut , StatusReserva status) {
        this.idReserva = idReserva;
        this.hospede = hospede;
        this.quantHospedes = quantHospedes;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
    }

    public ReservaDto(int i, String maria_josé_pereira, int i1, Date parse, Date parse1, StatusReserva CONFIRMADA) {
    }

    public Integer getIdReserva() {return idReserva;}

    public String getHospede() {return hospede;}

    public int getQuantHospedes() {return quantHospedes;}

    public Date getCheckIn() {return checkIn; }

    public Date getCheckOut() {return checkOut;}

    public static StatusReserva getStatus() {return status;}

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public void setQuantHospedes(int quantHospedes) {
        this.quantHospedes = quantHospedes;
    }

    public void setHospede(String hospede) {
        this.hospede = hospede;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public void setStatus(StatusReserva status) {
        this.status = status;
    }
}
