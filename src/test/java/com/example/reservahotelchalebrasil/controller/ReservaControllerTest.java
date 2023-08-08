package com.example.reservahotelchalebrasil.controller;

import com.example.reservahotelchalebrasil.domain.Reserva;
import com.example.reservahotelchalebrasil.domain.StatusReserva;
import com.example.reservahotelchalebrasil.dto.ReservaDto;
import com.example.reservahotelchalebrasil.service.ReservaService;
import com.example.reservahotelchalebrasil.service.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.reservahotelchalebrasil.domain.StatusReserva.CONFIRMADA;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservaController.class)
public class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReservaService reservaService;

    @InjectMocks
    private ReservaController reservaController;
    private ReservaDto reservaDto;

    public ReservaControllerTest() throws ParseException {
    }

    @BeforeEach
    public void setup() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        reservaDto = new ReservaDto(
                1,
                "Maria José Pereira",
                2,
                sdf.parse("2023/07/05"),
                sdf.parse("2023/07/06"),
                CONFIRMADA);
    }

    @Test
    public void testeInsertReserva() throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Reserva reservaInserida = new Reserva(
                1,
                "Maria José Pereira",
                2,
                sdf.parse("2023/07/05"),
                sdf.parse("2023/07/06"),
                CONFIRMADA);

        when(reservaService.createReserva(any(Reserva.class)))
                .thenReturn(reservaInserida);

        mockMvc.perform(MockMvcRequestBuilders.post("/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"hospede\":\"Maria José Pereira\",\"quantHospedes\":2,\"checkin\":\"2023/07/05\",\"checkout\":\"2023/07/06\",\"status\":\"CONFIRMADA\"}"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/reservas/1"));
    }

    @Test
    public void testeFindAllReservas() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        List<Reserva> listaReservas = new ArrayList<>();
        listaReservas.add(new Reserva(
                1,
                "Maria José Pereira",
                2,
                sdf.parse("2023/07/05"),
                sdf.parse("2023/07/06"),
                StatusReserva.CONFIRMADA));
        listaReservas.add(new Reserva(
                2,
                "João da Silva",
                1,
                sdf.parse("2023/08/10"),
                sdf.parse("2023/08/15"),
                StatusReserva.PENDENTE));

        when(reservaService.findAllReservas()).thenReturn(listaReservas);

        mockMvc.perform(MockMvcRequestBuilders.get("/reservas/all"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2))) // Verifica se a lista contém 2 elementos
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].idReserva").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].hospede").value("Maria José Pereira"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantHospedes").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].checkIn").value(sdf.format(sdf.parse("2023/07/05"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].checkOut").value(sdf.format(sdf.parse("2023/07/06"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value(StatusReserva.CONFIRMADA.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].idReserva").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].hospede").value("João da Silva"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].quantHospedes").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].checkIn").value(sdf.format(sdf.parse("2023/08/10"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].checkOut").value(sdf.format(sdf.parse("2023/08/15"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].status").value(StatusReserva.PENDENTE.toString()));
    }

    @Test
    public void testeFindReservaByIdExistente() throws Exception{
        Integer idReserva = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Reserva reservaEncontrada = new Reserva(
                idReserva,
                "Maria José Pereira",
                2,
                sdf.parse("2023/07/05"),
                sdf.parse("2023/07/06"),
                StatusReserva.CONFIRMADA);

        when(reservaService.findById(idReserva)).thenReturn(reservaEncontrada);

        mockMvc.perform(MockMvcRequestBuilders.get("/reservas/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idReserva").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hospede").value("Maria José Pereira"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantHospedes").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.checkIn").value( sdf.format(sdf.parse("2023/07/05"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.checkOut").value( sdf.format(sdf.parse("2023/07/06"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(StatusReserva.CONFIRMADA.toString()));

    }

    @Test
    public void testeFindByIdReservaNula() throws Exception {
        Integer idReservaInexistente = null;
        when(reservaService.findById(idReservaInexistente)).thenReturn(null);

       mockMvc.perform(MockMvcRequestBuilders.get("/reservas/{idReservaInexistente}", idReservaInexistente))
             .andExpect(status().isNotFound());
    }

    @Test
    public void testeUpdateReservaPorId() throws Exception {
        Integer idReserva = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        Reserva reservaAtualizada = new Reserva(
                idReserva,
                "Maria José da Silva",
                3,
                sdf.parse("2023/08/10"),
                sdf.parse("2023/08/15"),
                StatusReserva.PENDENTE);

        when(reservaService.updateReserva(any(), any())).thenReturn(reservaAtualizada);

        mockMvc.perform(MockMvcRequestBuilders.put("/reservas/{idReserva}", idReserva)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"hospede\":\"Maria José da Silva\",\"quantHospedes\":3,\"checkIn\":\"2023/08/10\",\"checkOut\":\"2023/08/15\",\"status\":\"PENDENTE\"}"))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.hospede").value("Maria José da Silva"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.quantHospedes").value(3))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.checkIn").value(sdf.format(sdf.parse("2023/08/10"))))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.checkOut").value(sdf.format(sdf.parse("2023/08/15"))))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(StatusReserva.PENDENTE.toString()));
    }

    @Test
    public void testeUpdateReservaPorIdCasoReservaNaoEncontrada() throws Exception {
        Integer idReserva = 10000;

        when(reservaService.updateReserva(any(), any())).thenThrow(ObjectNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/reservas/{idReserva}", idReserva)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"hospede\":\"Maria José da Silva\",\"quantHospedes\":3,\"checkIn\":\"2023/08/10\",\"checkOut\":\"2023/08/15\",\"status\":\"PENDENTE\"}"))
                        .andExpect(status().isNotFound());
    }

    @Test
    public void testeAtualizaStatusById() throws Exception {
        Integer idReserva = 1;
        ReservaDto resDto = new ReservaDto();
        resDto.setStatus(StatusReserva.valueOf("CANCELADA"));
        ResponseEntity<String> responseEntity = ResponseEntity.ok("Reserva cancelada com sucesso");

        when(reservaService.atualizaStatusById(any(), any())).thenReturn(responseEntity);

        mockMvc.perform(MockMvcRequestBuilders.put("/reservas/cancelar/{idReserva}", idReserva)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"CANCELADA\"}"))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.content().string("Reserva cancelada com sucesso"));
    }

    @Test
    public void testeAtualizaStatusByIdCasoReservaNaoEncontrada() throws Exception {
        Integer idReserva = 10000;
        ReservaDto resDto = new ReservaDto();
        resDto.setStatus(StatusReserva.valueOf("CANCELADA"));

        when(reservaService.atualizaStatusById(any(), any())).thenReturn(
                new ResponseEntity<>("Número de reserva não existe", HttpStatus.NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.put("/reservas/cancelar/{idReserva}", idReserva)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"CANCELADA\"}"))
                        .andExpect(status().isNotFound())
                        .andExpect(MockMvcResultMatchers.content().string("Número de reserva não existe"));

    }

}
