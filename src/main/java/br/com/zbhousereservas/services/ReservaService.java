package br.com.zbhousereservas.services;

import br.com.zbhousereservas.dto.ListarReservaDTO;
import br.com.zbhousereservas.dto.ListarReservaResponse;
import br.com.zbhousereservas.dto.ReservaDTO;
import br.com.zbhousereservas.exceptions.*;
import br.com.zbhousereservas.entities.Reserva;
import br.com.zbhousereservas.repositories.ReservaRepository;
import br.com.zbhousereservas.validations.ValidarPagamentos;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private ValidarPagamentos validarPagamentos;

    @Value("${diaria.reserva.zbhouse}")
    private double diaria;

    public static @NotNull ArrayList<LocalDateTime> intervaloCheckinChekout(LocalDateTime checkin, LocalDateTime checkout) {
        ArrayList<LocalDateTime> datas = new ArrayList<>();

        LocalDateTime dataAtual = checkin;

        while (!dataAtual.isAfter(checkout)) {
            datas.add(dataAtual);
            dataAtual = dataAtual.plusDays(1);
        }

        return datas;
    }

    public boolean validarDatas(LocalDateTime checkin, LocalDateTime checkout) {
        boolean novaReserva = true;

        for (LocalDateTime dia : intervaloCheckinChekout(checkin, checkout)) {

            boolean checkinExiste = this.reservaRepository.findByCheckin(dia).isPresent();
            boolean checkoutExiste = this.reservaRepository.findByCheckout(dia).isPresent();

            if (checkinExiste || checkoutExiste) {
                throw new ReservaExistenteException();
            }

        }
        return novaReserva;
    }

    public List<LocalDateTime> listarDatasDisponiveis() {
        List<LocalDateTime> datasDisponiveis = new ArrayList<>();

        Set<LocalDateTime> datasReservadas = new HashSet<>();
        this.reservaRepository.findAll().forEach(reserva -> {
            for (LocalDateTime dia : intervaloCheckinChekout(reserva.getCheckin(), reserva.getCheckout())) {
                datasReservadas.add(dia.toLocalDate().atStartOfDay());
            }
        });

        LocalDateTime hoje = LocalDateTime.now();

        for (LocalDateTime dia = hoje.toLocalDate().atStartOfDay(); dia.getYear() == hoje.getYear(); dia = dia.plusDays(1)) {
            if (!datasReservadas.contains(dia)) {
                datasDisponiveis.add(dia);
            }
        }

        return datasDisponiveis;
    }

    public boolean validarPrimeiraParcela(@NotNull Reserva reserva) {
        boolean primeiraParcela = true;
//        Double valorPagamento = reserva.getPagamentos().getFirst().getValor_pagamento();
        validarPagamentos.validarPrimeiraParcela(reserva);

//        if (reserva.getPagamentos().getFirst().getData_pagamento().isAfter(reserva.getCheckin())) {
//            throw new PrimeiraParcelaMaiorQueCheckinException();
//        }
//
//        if (valorPagamento == null || valorPagamento <= 0) {
//            throw new ValorParcelaException("Valor da parcela não pode ser menor que 0 ou nulo!");
//        }

        reserva.getPagamentos().getFirst().setParcela(1);
        return primeiraParcela;
    }

    public void valorReserva(@NotNull Reserva reserva) {
        int dias = intervaloCheckinChekout(reserva.getCheckin(), reserva.getCheckout()).size();
        double valorReserva = dias * diaria * (1 - reserva.getDesconto() / 100);
        if (valorReserva < reserva.getPagamentos().getFirst().getValor_pagamento()) {
            throw new ValorParcelaException("Valor da Parcela está superior ao valor da reserva");
        }
        reserva.setValor_reserva(valorReserva);
    }

    public ReservaDTO salvarReserva(@NotNull Reserva reserva) {
        if (reserva.getCheckin().isAfter(reserva.getCheckout())) {
            throw new CheuckoutMenorQueCheckinException();
        }

        if (!validarDatas(reserva.getCheckin(), reserva.getCheckout())) {
            throw new ReservaExistenteException();
        }

        if (!validarPrimeiraParcela(reserva)) {
            throw new PrimeiraParcelaMaiorQueCheckinException();
        }

        try {
            valorReserva(reserva);
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
        return this.reservaRepository.findAll(pageable).map(ListarReservaDTO::new);
    }

    public ListarReservaResponse listarReservaResponse (Pageable pageable){
        Page<ListarReservaDTO> result = listarTodasReservas(pageable);

        ListarReservaResponse response = new ListarReservaResponse();

        response.setContent(result.getContent());
        response.setTotalPages(result.getTotalPages());
        response.setTotalElements(result.getTotalElements());
        return  response;
    }

}
