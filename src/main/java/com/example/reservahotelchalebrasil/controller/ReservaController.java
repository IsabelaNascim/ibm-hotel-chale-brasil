package com.example.reservahotelchalebrasil.controller;

import com.example.reservahotelchalebrasil.domain.Reserva;
import com.example.reservahotelchalebrasil.domain.StatusReserva;
import com.example.reservahotelchalebrasil.service.ReservaService;
import com.example.reservahotelchalebrasil.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaServ;

    @PostMapping
    public ResponseEntity<Void> createReserva(@RequestBody Reserva reserva){
        Reserva res = reservaServ.createReserva(reserva);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{idReserva}")
                .buildAndExpand(res.getIdReserva())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Reserva>> findAllReservas(){
        List<Reserva> listaDeReservas = reservaServ.findAllReservas();
        return ResponseEntity.ok(listaDeReservas);
    }

    @GetMapping("/{idReserva}")
    public ResponseEntity<Reserva> findById(@PathVariable Integer idReserva){
        if(idReserva == null || idReserva <= 0 ){
            throw new IllegalArgumentException("O ID da reserva é inválido.");
        }
        Reserva reserva = reservaServ.findById(idReserva);
        return ResponseEntity.ok(reserva);
    }

    @PutMapping("/{idReserva}")
    public ResponseEntity<Reserva> updateReserva(@RequestBody Reserva reserva, @PathVariable Integer idReserva){
        try {
            Reserva novaReserva = reservaServ.updateReserva(reserva, idReserva);
            return ResponseEntity.ok(novaReserva);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("cancelar/{idReserva}")
    public ResponseEntity<String> atualizaStatusById(@PathVariable Integer idReserva) {
        //Reserva reservaCancelada = reservaServ.fromDto(resDto); -- @RequestBody ReservaDto resDto,
                String novoStatus = String.valueOf(StatusReserva.CANCELADA);
        return reservaServ.atualizaStatusById(idReserva, novoStatus);
    }


}
