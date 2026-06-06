package br.com.zbhousereservas.controllers;

import br.com.zbhousereservas.dto.DetalhamentoPagamento;
import br.com.zbhousereservas.dto.PagamentoDTO;
import br.com.zbhousereservas.dto.SegundaParcela;
import br.com.zbhousereservas.dto.ValorDetalhado;
import br.com.zbhousereservas.services.PagamentosService;
import br.com.zbhousereservas.services.ReservaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping("/pagamentos")
@SecurityRequirement(name = "bearer-key")
public class PagamentosController {

    @Autowired
    private PagamentosService pagamentosService;
    @Autowired
    private ReservaService reservaService;

    @Autowired
    public PagamentosController(PagamentosService pagamentosService, ReservaService reservaService) {
        this.pagamentosService = pagamentosService;
        this.reservaService = reservaService;
    }


    @GetMapping("/reserva/{id}")
    public ResponseEntity<Object> buscarPagamentosPorReserva(@PathVariable Long id) {

        var result = this.pagamentosService.buscaPagamentoPorReservaList(id).stream().map(pagamento -> new PagamentoDTO(pagamento, id)).toList();
        log.info("Pagamento da reserva {} encontrado", id);
        return ResponseEntity.ok().body(result);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> detalharPagamento(@PathVariable Long id) {

        var result = this.pagamentosService.buscarPagamento(id);
        var nome = this.reservaService.listarReservaPorId(result.getReserva().getId()).getNome();
        log.info("Pagamentos da reserva {} detalhado", id);
        return ResponseEntity.ok().body(new DetalhamentoPagamento(new PagamentoDTO(result, id), nome));

    }

    @PostMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> pagarParcela(@Valid @PathVariable Long id, @RequestBody SegundaParcela segundaParcela, UriComponentsBuilder uriComponentsBuilder) {
        var result = this.pagamentosService.inserirPagamento(id, segundaParcela.valor(), segundaParcela.data());
        long reservaId = result.getReserva().getId();
        var nome = this.reservaService.listarReservaPorId(reservaId).getNome();
        var uri = uriComponentsBuilder.path("pagamentos/{id}").buildAndExpand(reservaId).toUri();
        log.info("Parcela da reserva {} paga", id);
        return ResponseEntity.created(uri).body(new DetalhamentoPagamento(nome, new PagamentoDTO(result, reservaId)));
    }

    @GetMapping("/recebidos")
    public ResponseEntity<Object> somarRecebidos() {
        Double somaRecebidos = this.pagamentosService.somaRecebidos();
        log.info("Pagamentos recebidos calculados com sucesso");
        return ResponseEntity.ok().body(new ValorDetalhado(somaRecebidos));
    }

    @GetMapping("/aReceber")
    public ResponseEntity<Object> somarAReceber() {
        double somaAReceber = this.pagamentosService.somaAReceber();
        log.info("Pagamentos a receber calculados com sucesso");
        return ResponseEntity.ok().body(new ValorDetalhado(somaAReceber));
    }
}
