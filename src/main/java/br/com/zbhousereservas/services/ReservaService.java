package br.com.zbhousereservas.services;

import br.com.zbhousereservas.dto.ListarReservaDTO;
import br.com.zbhousereservas.dto.ListarReservaResponse;
import br.com.zbhousereservas.dto.ReservaDTO;
import br.com.zbhousereservas.exceptions.*;
import br.com.zbhousereservas.entities.Reserva;
import br.com.zbhousereservas.repositories.ReservaRepository;
import br.com.zbhousereservas.validations.ValidarObjetos;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@NoArgsConstructor
@AllArgsConstructor
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private ValidarObjetos validarObjetos;

    @Value("${diaria.reserva.zbhouse}")
    private double diaria;

    public List<LocalDate> listarDatasDisponiveis() {
        List<LocalDate> datasDisponiveis = new ArrayList<>();
        Set<LocalDate> datasReservadas = new HashSet<>();
        this.reservaRepository.findAll().forEach(reserva -> {
            for (LocalDate dia : validarObjetos.intervaloCheckinChekout(reserva.getCheckin(), reserva.getCheckout())) {
                if (reserva.isAtivo()) {
                    datasReservadas.add(LocalDate.from(dia.atStartOfDay()));
                }
            }
        });

        LocalDate hoje = LocalDate.now();

        for (LocalDate dia = LocalDate.from(hoje.atStartOfDay()); dia.getYear() == hoje.getYear(); dia = dia.plusDays(1)) {
            if (!datasReservadas.contains(dia)) {
                datasDisponiveis.add(dia);
            }
        }

        return datasDisponiveis;
    }

    public void valorReserva(@NotNull Reserva reserva) {
        int dias = validarObjetos.intervaloCheckinChekout(reserva.getCheckin(), reserva.getCheckout()).size();
        double valorReserva;
        if (reserva.getDiaria() > 0) {
            valorReserva = dias * reserva.getDiaria() * (1 - reserva.getDesconto() / 100);
            double valorParcela = reserva.getPagamentos().get(0).getValor_pagamento();
            ValidarObjetos.validarValorParcela(valorReserva, valorParcela);
            reserva.setValor_reserva(valorReserva);
        } else {
            throw new ValorDiariaException("Valor da diária não pode ser menor que zero");
        }
    }

    public ReservaDTO salvarReserva(@NotNull Reserva reserva) {

        validarObjetos.validarReserva(reserva);

        try {
            valorReserva(reserva);
            reserva.setAtivo(true);
            this.reservaRepository.save(reserva);
            return new ReservaDTO(reserva);
        } catch (DataAccessException e) {
            throw new ErroAoSalvarException();
        }
    }

    public Reserva listarReservaPorId(Long id) {
        return this.reservaRepository.findById(id).orElseThrow(ReservaNaoExistenteException::new);
    }

    public Page<ListarReservaDTO> listarTodasReservas(Pageable pageable) {
        return this.reservaRepository.findAllByAtivoTrue(pageable).map(ListarReservaDTO::new);
    }

    public ListarReservaResponse listarReservaResponse(Pageable pageable) {
        Page<ListarReservaDTO> result = listarTodasReservas(pageable);

        ListarReservaResponse response = new ListarReservaResponse();

        response.setContent(result.getContent());
        response.setTotalPages(result.getTotalPages());
        response.setTotalElements(result.getTotalElements());
        return response;
    }

    public List<Reserva> buscarPorNome(String nome) {
        List<Reserva> reservas;

        reservas = this.reservaRepository.findAllByNomeContainingIgnoreCase(nome);

        if (!reservas.isEmpty()) {
            return reservas;
        }
        throw new ReservaNaoExistenteException();
    }

    public String excluirReserva(Long id) {
        try {
            Reserva reserva = listarReservaPorId(id);
            reserva.setAtivo(false);
            this.reservaRepository.save(reserva);
//            this.reservaRepository.deleteById(id);
            return reserva.getNome();
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível excluir a reserva");
        }
    }
}
