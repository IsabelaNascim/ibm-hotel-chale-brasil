package com.example.reservahotelchalebrasil.service;

import com.example.reservahotelchalebrasil.domain.Reserva;
import com.example.reservahotelchalebrasil.domain.StatusReserva;
import com.example.reservahotelchalebrasil.dto.ReservaDto;
import com.example.reservahotelchalebrasil.repository.ReservaRepository;
import com.example.reservahotelchalebrasil.service.exception.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.reservahotelchalebrasil.domain.StatusReserva.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    private ReservaDto reservaDto;

    @Test
    public void testeCreateReserva() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Reserva reservaInserida = new Reserva(
                1,
                "Maria José Pereira",
                2,
                sdf.parse("2023/07/05"),
                sdf.parse("2023/07/06"),
                CONFIRMADA);

        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaInserida);

        Reserva createdReserva = reservaService.createReserva(reservaInserida);

        assertNotNull(createdReserva);
        assertEquals(StatusReserva.CONFIRMADA, createdReserva.getStatus());
    }

    @Test
    public void testeCreateReservaDataCheckoutAnteriorAoCheckin() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Reserva reservaInserida = new Reserva(
                1,
                "Maria José Pereira",
                2,
                sdf.parse("2023/07/05"),
                sdf.parse("2023/07/04"),
                CONFIRMADA);

        assertThrows(IllegalArgumentException.class, () -> {
            reservaService.createReserva(reservaInserida);
        });
    }

    @Test
    void testeValidacaoEntradasReserva_QuantHospedesZerada() throws ParseException {
        ReservaService reservaService = new ReservaService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Reserva reservaZerada = new Reserva(
                1,
                "Maria José Pereira",
                0,
                sdf.parse("2023/07/05"),
                sdf.parse("2023/07/06"),
                CONFIRMADA);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reservaService.validacaoEntradasReserva(reservaZerada));
        assertEquals("A quantidade de hóspedes não compatível.", exception.getMessage());
    }

    @Test
    void testeValidacaoEntradasReserva_QuantHospedesAcimaDoPermitido() throws ParseException {
        ReservaService reservaService = new ReservaService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Reserva reservaQtdHospAcima = new Reserva(
                1,
                "Maria José Pereira",
                6,
                sdf.parse("2023/07/05"),
                sdf.parse("2023/07/06"),
                CONFIRMADA);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reservaService.validacaoEntradasReserva(reservaQtdHospAcima));
        assertEquals("A quantidade de hóspedes não compatível.", exception.getMessage());
    }

    @Test
    void testeValidacaoEntradasReserva_HospedeNulo() throws ParseException {
        ReservaService reservaService = new ReservaService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Reserva reservaHospNulo = new Reserva();
        reservaHospNulo.setHospede(null);
        reservaHospNulo.setQuantHospedes(2);
        reservaHospNulo.setCheckIn(sdf.parse("2023/07/05"));
        reservaHospNulo.setCheckOut(sdf.parse("2023/07/06"));


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reservaService.validacaoEntradasReserva(reservaHospNulo));
        assertEquals("O campo 'hospede' é obrigatório.", exception.getMessage());
    }

    @Test
    void testeValidacaoEntradasReserva_CheckInNulo() throws ParseException {
        ReservaService reservaService = new ReservaService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Reserva reservaHospNulo = new Reserva();
        reservaHospNulo.setHospede("Maria José Pereira");
        reservaHospNulo.setQuantHospedes(2);
        reservaHospNulo.setCheckOut(sdf.parse("2023/07/06"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reservaService.validacaoEntradasReserva(reservaHospNulo));
        assertEquals("O campo 'checkIn' é obrigatório.", exception.getMessage());
    }

    @Test
    void testeValidacaoEntradasReserva_CheckOutNulo() throws ParseException {
        ReservaService reservaService = new ReservaService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Reserva reservaHospNulo = new Reserva();
        reservaHospNulo.setHospede("Maria José Pereira");
        reservaHospNulo.setQuantHospedes(2);
        reservaHospNulo.setCheckIn(sdf.parse("2023/07/05"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reservaService.validacaoEntradasReserva(reservaHospNulo));
        assertEquals("O campo 'checkOut' é obrigatório.", exception.getMessage());
    }

    @Test
    public void testeFindAllReservas() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        List<Reserva> reservasLista = new ArrayList<>();
        reservasLista.add(new Reserva(1, "Maria José Pereira", 2, sdf.parse("2023/07/05"), sdf.parse("2023/07/06"), CONFIRMADA));
        reservasLista.add(new Reserva(2, "Fabiana da Silva Moreira", 5, sdf.parse("2023/08/15"), sdf.parse("2023/08/16"), CANCELADA));

        when(reservaRepository.findAll()).thenReturn(reservasLista);

        List<Reserva> resultado = reservaService.findAllReservas();

        verify(reservaRepository, times(1)).findAll();
        assertEquals(reservasLista.size(), resultado.size());
        assertEquals(reservasLista.get(0), resultado.get(0));
        assertEquals(reservasLista.get(1), resultado.get(1));
    }

    @Test
    public void testeFindReservaById() throws ParseException {
        Integer reservaId = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Reserva reserva = new Reserva(
                reservaId,
                "Maria José Pereira",
                2,
                sdf.parse("2023/07/05"),
                sdf.parse("2023/07/06"),
                CONFIRMADA);

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));

        Reserva reservaEncontrada = reservaService.findById(reservaId);

        assertNotNull(reservaEncontrada);
        assertEquals(reserva.getIdReserva(), reservaEncontrada.getIdReserva());
        assertEquals(reserva.getHospede(), reservaEncontrada.getHospede());
        assertEquals(reserva.getQuantHospedes(), reservaEncontrada.getQuantHospedes());
        assertEquals(reserva.getCheckIn(), reservaEncontrada.getCheckIn());
        assertEquals(reserva.getCheckOut(), reservaEncontrada.getCheckOut());
        assertEquals(reserva.getStatus(), reservaEncontrada.getStatus());

        verify(reservaRepository, times(1)).findById(reservaId);

    }

    @Test
    public void testFindReservaByIdNaoExistente() {
        Integer reservaId = 99;

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> reservaService.findById(reservaId));
        verify(reservaRepository, times(1)).findById(reservaId);
    }

    @Test
    public void testeUpdateReserva() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Integer idReserva = 1;
        Reserva reservaAtualizada = new Reserva(
                idReserva,
                "Maria José Pereira",
                2,
                sdf.parse("2023/07/05"),
                sdf.parse("2023/07/06"),
                CONFIRMADA
        );

        Reserva reservaExistente = new Reserva(
                idReserva,
                "Maria José Pereira",
                4,
                sdf.parse("2023/07/05"),
                sdf.parse("2023/07/06"),
                PENDENTE
        );
        reservaExistente.setIdReserva(idReserva);

        when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reservaExistente));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaExistente);

        Reserva resultado = reservaService.updateReserva(reservaAtualizada, idReserva);

        verify(reservaRepository, times(1)).findById(idReserva);
        verify(reservaRepository, times(1)).save(eq(reservaExistente));

        assertEquals(reservaAtualizada.getHospede(), resultado.getHospede());
        assertEquals(reservaAtualizada.getCheckIn(), resultado.getCheckIn());
        assertEquals(reservaAtualizada.getCheckOut(), resultado.getCheckOut());
        assertEquals(reservaAtualizada.getQuantHospedes(), resultado.getQuantHospedes());
        assertEquals(StatusReserva.PENDENTE, resultado.getStatus());
    }

    @Test
    public void testeUpdateReservaIdInexistente() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Integer idResInexistente = 10000;
        Reserva reservaAtualizada = new Reserva(
                idResInexistente,
                "Maria José Pereira",
                2,
                sdf.parse("2023/07/05"),
                sdf.parse("2023/07/06"),
                CONFIRMADA);

        when(reservaRepository.findById(idResInexistente)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () ->
                reservaService.updateReserva(reservaAtualizada, idResInexistente)
        );
        verify(reservaRepository, times(1)).findById(idResInexistente);
    }

    @Test
    public void testeUpdateReservaStatusCancelada() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Integer idReserva = 1;
        Reserva reservaExistente = new Reserva(
                idReserva,
                "Maria José Pereira",
                4,
                sdf.parse("2023/07/05"),
                sdf.parse("2023/07/06"),
                CANCELADA);

        Reserva reservaAtualizada = new Reserva(
                idReserva,
                "Maria José Pereira",
                3,
                sdf.parse("2023/07/05"),
                sdf.parse("2023/07/06"),
                PENDENTE);

        when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reservaExistente));

        try {
            reservaService.updateReserva(reservaAtualizada, idReserva);
            fail("Deveria ter lançado uma exceção IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Não é permitido atualizar uma reserva com status CANCELADA para status PENDENTE.", e.getMessage());
        }

        verify(reservaRepository, times(1)).findById(idReserva);
    }

    @Test
    public void testeAtualizaStatusById() {
        Integer idReserva = 1;
        String novoStatus = "CANCELADA";

        Reserva reservaExistente = new Reserva();
        when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reservaExistente));

        ResponseEntity<String> response = reservaService.atualizaStatusById(idReserva, novoStatus);

        assertEquals("Reserva cancelada com sucesso", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(reservaRepository, times(1)).findById(idReserva);
        verify(reservaRepository, times(1)).save(reservaExistente);
    }

    @Test
    public void testeAtualizaStatusByIdParaIdNaoExistente() {
        Integer idReserva = 1000;
        String novoStatus = "CANCELADA";

        when(reservaRepository.findById(idReserva)).thenReturn(Optional.empty());

        ResponseEntity<String> response = reservaService.atualizaStatusById(idReserva, novoStatus);

        assertEquals("Número de reserva não existe", response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(reservaRepository, times(1)).findById(idReserva);
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

}
