package br.com.zbhousereservas.controllers;

import br.com.zbhousereservas.dto.*;
import br.com.zbhousereservas.entities.Reserva;
import br.com.zbhousereservas.services.ReservaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/reservas")
@SecurityRequirement(name = "bearer-key")
public class ReservasController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    public ReservasController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping("/")
    @Transactional
    public ResponseEntity<Object> criarReserva(@Valid @RequestBody @NotNull Reserva reserva, UriComponentsBuilder uriComponentsBuilder) {

        try {
            var result = this.reservaService.salvarReserva(reserva);
            var uri = uriComponentsBuilder.path("reservas/{id}").buildAndExpand(result.id()).toUri();
            return ResponseEntity.created(uri).body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> listarReservaPorId(@PathVariable Long id) {
        try {
            var result = this.reservaService.listarReservaPorId(id);
            return ResponseEntity.ok().body(new ListarReservaDTO(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    @GetMapping("/todos")
    public ResponseEntity<Object> listarTodasReservas(@PageableDefault(value = 6, sort = {"checkin"}) Pageable pageable, @RequestParam(required = false) List<String> sort) {
        try {

            ListarReservaResponse response = this.reservaService.listarReservaResponse(pageable);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/busca/{nome}")
    public ResponseEntity<Object> buscaPorNome(@PathVariable String nome){
        try{
            var result = this.reservaService.buscarPorNome(nome);
            return ResponseEntity.ok().body(result);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/datas")
    public ResponseEntity<Object> datasDisponiveis(){
        try {
            var result = this.reservaService.listarDatasDisponiveis();
            return ResponseEntity.ok().body(new DatasDisponiveis(result));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarReserva(@PathVariable Long id){
        try {
            this.reservaService.excluirReserva(id);
            var reserva = this.reservaService.listarReservaPorId(id);
            return ResponseEntity.ok().body("Reserva de excluída!");

        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
