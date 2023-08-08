package com.example.reservahotelchalebrasil.service;

import com.example.reservahotelchalebrasil.domain.Reserva;
import com.example.reservahotelchalebrasil.domain.StatusReserva;
import com.example.reservahotelchalebrasil.dto.ReservaDto;
import com.example.reservahotelchalebrasil.repository.ReservaRepository;
import com.example.reservahotelchalebrasil.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepo;

    public Reserva createReserva(Reserva res){
        validacaoEntradasReserva(res);
        if(res.getCheckOut().before(res.getCheckIn())){
            throw new IllegalArgumentException("A data de checkOut deve ser posterior à data de checkIn.");
        }
        res.setIdReserva(null);
        res.setStatus(StatusReserva.valueOf(String.valueOf(StatusReserva.CONFIRMADA)));
        res = reservaRepo.save(res);
        return res;
    }

    public List<Reserva> findAllReservas(){
        List<Reserva> listaDeReservas = reservaRepo.findAll();
        return listaDeReservas;
    }

    public Reserva findById(Integer idReserva){
        Optional<Reserva> reserva = reservaRepo.findById(idReserva);
        return reserva.orElseThrow(() -> new ObjectNotFoundException("Reserva não encontrada para o ID: " + idReserva));
    }

    public Reserva updateReserva(Reserva res, Integer idReserva) throws ParseException {
        validacaoEntradasReserva(res);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Reserva novaRes = reservaRepo.findById(idReserva)
                .orElseThrow(()-> new ObjectNotFoundException("Reserva com Id " +idReserva+ " não existente."));
        if(novaRes.getStatus() == StatusReserva.CANCELADA) {
            throw new IllegalArgumentException("Não é permitido atualizar uma reserva com status CANCELADA para status PENDENTE.");
        }
        if(res.getCheckOut().before(res.getCheckIn())){
            throw new IllegalArgumentException("A data de checkOut deve ser posterior à data de checkIn.");
        }
        novaRes.setHospede(res.getHospede());
        novaRes.setCheckIn(res.getCheckIn());
        novaRes.setCheckOut(res.getCheckOut());
        novaRes.setQuantHospedes(res.getQuantHospedes());
        novaRes.setStatus(StatusReserva.valueOf(String.valueOf(StatusReserva.PENDENTE)));
        return reservaRepo.save(novaRes);
    }

    public ResponseEntity<String> atualizaStatusById(Integer idReserva, String novoStatus){
        Optional<Reserva> optReserva = reservaRepo.findById(idReserva);
        if(optReserva.isPresent()) {
            Reserva resCancelada = optReserva.get();
            StatusReserva statusReserva = StatusReserva.valueOf(novoStatus);
            resCancelada.setStatus(statusReserva);
            reservaRepo.save(resCancelada);
            return new ResponseEntity<>("Reserva cancelada com sucesso", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Número de reserva não existe", HttpStatus.NOT_FOUND);
        }
    }


    public Reserva fromDto(ReservaDto resDto){
        StatusReserva status = StatusReserva.valueOf(String.valueOf(ReservaDto.getStatus()));
        return new Reserva(
                resDto.getIdReserva(),
                resDto.getHospede(),
                resDto.getQuantHospedes(),
                resDto.getCheckIn(),
                resDto.getCheckOut(),
                status
        );
    }

    void validacaoEntradasReserva(Reserva reserva){
        if(Objects.isNull(reserva.getHospede())){
            throw new IllegalArgumentException("O campo 'hospede' é obrigatório.");
        }
        if(Objects.isNull(reserva.getCheckIn())){
            throw new IllegalArgumentException("O campo 'checkIn' é obrigatório.");
        }
        if(Objects.isNull(reserva.getCheckOut())){
            throw new IllegalArgumentException("O campo 'checkOut' é obrigatório.");
        }
        if (reserva.getQuantHospedes() <= 0 || reserva.getQuantHospedes() >= 6) {
            throw new IllegalArgumentException("A quantidade de hóspedes não compatível.");
        }
    }
}
