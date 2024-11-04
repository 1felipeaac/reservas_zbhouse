package br.com.zbhousereservas.controllers;

import br.com.zbhousereservas.dto.DetalhamentoPagamento;
import br.com.zbhousereservas.dto.Parcelas;
import br.com.zbhousereservas.dto.SegundaParcela;
import br.com.zbhousereservas.dto.ValorDetalhado;
import br.com.zbhousereservas.services.PagamentosService;
import br.com.zbhousereservas.services.ReservaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/pagamentos")
@SecurityRequirement(name = "bearer-key")
public class PagamentosController {

    @Autowired
    private PagamentosService pagamentosService;
    @Autowired
    private ReservaService reservaService;

    @Autowired
    public PagamentosController(PagamentosService pagamentosService, ReservaService reservaService){
        this.pagamentosService = pagamentosService;
        this.reservaService = reservaService;
    }


    @GetMapping("/reserva/{id}")
    public ResponseEntity<Object> buscarPagamentosPorReserva(@PathVariable Long id) {

        try {
            var result = this.pagamentosService.buscaPagamentoPorReservaList(id).stream().map(Parcelas::new).toList();
            return ResponseEntity.ok().body(result);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> detalharPagamento(@PathVariable Long id) {

        try {
            var result = this.pagamentosService.buscarPagamento(id);
            var nome = this.reservaService.listarReservaPorId(result.getReservaId()).getNome();
            return ResponseEntity.ok().body(new DetalhamentoPagamento(new Parcelas(result), nome));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> pagarParcela(@Valid @PathVariable Long id, @RequestBody SegundaParcela segundaParcela, UriComponentsBuilder uriComponentsBuilder) {
        try {
            var result = this.pagamentosService.inserirPagamento(id, segundaParcela.valor());
            var nome = this.reservaService.listarReservaPorId(result.getReservaId()).getNome();
            var uri = uriComponentsBuilder.path("pagamentos/{id}").buildAndExpand(result.getReservaId()).toUri();
            return ResponseEntity.created(uri).body(new DetalhamentoPagamento(nome, new Parcelas(result)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/recebidos")
    public ResponseEntity<Object> somarRecebidos() {
        try {
            Double somaRecebidos = this.pagamentosService.somaRecebidos();
            return ResponseEntity.ok().body(new ValorDetalhado(somaRecebidos));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/aReceber")
    public ResponseEntity<Object> somarAReceber(){
        try{
            double somaAReceber = this.pagamentosService.somaAReceber();
            return ResponseEntity.ok().body(new ValorDetalhado(somaAReceber));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
