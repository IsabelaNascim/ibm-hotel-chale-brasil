package com.example.reservahotelchalebrasil.controller.exceptions;

import java.io.Serializable;

public class StandardError implements Serializable {

    private Integer status;
    // Error 404, ... HTTP

    private Long horario;

    private String mensagem;

    public StandardError() { }

    public StandardError(Integer status, Long horario, String mensagem) {
        this.status = status;
        this.horario = horario;
        this.mensagem = mensagem;
    }

    public Integer getStatus() { return status; }

    public Long getHorario() { return horario; }

    public String getMensagem() { return mensagem; }

    public void setStatus(Integer status) { this.status = status; }

    public void setHorario(Long horario) { this.horario = horario; }

    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
}

