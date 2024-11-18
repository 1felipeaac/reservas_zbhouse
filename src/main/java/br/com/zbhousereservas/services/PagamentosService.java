package br.com.zbhousereservas.services;

import br.com.zbhousereservas.exceptions.*;
import br.com.zbhousereservas.entities.Pagamento;
import br.com.zbhousereservas.entities.Reserva;
import br.com.zbhousereservas.repositories.PagamentosRepository;
import br.com.zbhousereservas.repositories.ReservaRepository;
import br.com.zbhousereservas.validations.ValidarObjetos;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class PagamentosService {
    @Autowired
    private PagamentosRepository pagamentosRepository;
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private ReservaService reservaService;
    @Autowired
    private ValidarObjetos validarObjetos;

    public List<Pagamento> buscaPagamentoPorReservaList(Long reservaId) {

        var pagamento = this.pagamentosRepository.findByReservaId(reservaId).orElseThrow(ReservaNaoExistenteException::new);

        if (pagamento.isEmpty()) {
            throw new ReservaNaoExistenteException();
        }

        return pagamento;
    }

    public Pagamento buscaPagamentoPorReservaDistinct(Long reservaId) {

        return this.pagamentosRepository.findFirstByReservaId(reservaId).orElseThrow(ReservaNaoExistenteException::new);
    }

    public Pagamento inserirPagamento(Long reservaId, double valor, LocalDate data) {

        Pagamento pagParcela;

        Pagamento pagamentoDaReserva = buscaPagamentoPorReservaDistinct(reservaId);

        double valorAReceber = pagamentoDaReserva.getValor_pagamento() + valor;
        validarObjetos.validarPagamento(valorAReceber, reservaId, data);
        pagParcela = new Pagamento();
        pagParcela.setParcela(pagamentoDaReserva.getParcela() + 1);
        pagParcela.setReservaId(reservaId);
        pagParcela.setData_pagamento(data);
        pagParcela.setValor_pagamento(valor);

        try {
            return this.pagamentosRepository.save(pagParcela);
        } catch (Exception e) {
            throw new ErroAoSalvarException();
        }

    }

    public Double somaRecebidos() {
        List<Double> pagamentos = new ArrayList<>();
        this.pagamentosRepository.findAll().forEach(pagamento -> {
            Reserva reserva = reservaService.listarReservaPorId(pagamento.getReservaId());
            if (reserva.isAtivo()) {
                pagamentos.add(pagamento.getValor_pagamento());
            }
        });
        return pagamentos.stream()
                .mapToDouble(java.lang.Double::doubleValue)
                .sum();
    }

    public Double somaAReceber() {
        List<Double> aReceber = new ArrayList<>();

        this.pagamentosRepository.findAll().forEach(pagamento -> {
            Reserva reserva = reservaService.listarReservaPorId(pagamento.getReservaId());

            if (reserva.isAtivo()) {
                if (reserva.getPagamentos().toArray().length < 2) {
                    double diferenca = reserva.getValor_reserva() - pagamento.getValor_pagamento();
                    aReceber.add(diferenca);
                }
            }

        });

        return aReceber.stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public Pagamento buscarPagamento(Long id) {

        return this.pagamentosRepository.findById(id).orElseThrow();
    }

}
