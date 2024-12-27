package br.com.zbhousereservas.controllers;

import br.com.zbhousereservas.dto.*;
import br.com.zbhousereservas.entities.Reserva;
import br.com.zbhousereservas.services.ReservaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Slf4j
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
            log.info("Reserva de {} criada com sucesso", reserva.getNome());
            return ResponseEntity.created(uri).body(result);
        } catch (Exception e) {
            log.error("Não foi possível criar a reserva {}. {}", reserva.getNome(), e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> listarReservaPorId(@PathVariable Long id) {
        try {
            var result = this.reservaService.listarReservaPorId(id);
            log.info("Reserva id {}, nome {} listada", id, result.getNome());
            return ResponseEntity.ok().body(new ListarReservaDTO(result));
        } catch (Exception e) {
            log.error("Não foi possível listar a reserva id {}. {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    @GetMapping("/todos")
    public ResponseEntity<Object> listarTodasReservas(@PageableDefault(value = 6, sort = {"checkin"}) Pageable pageable, @RequestParam(required = false) List<String> sort) {
        try {

            ListarReservaResponse response = this.reservaService.listarReservaResponse(pageable);
            log.info("Reservas listadas com sucesso");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            log.error("Não foi possível listar as reservas. {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/busca/{nome}")
    public ResponseEntity<Object> buscaPorNome(@PathVariable String nome) {
        try {
            var result = this.reservaService.buscarPorNome(nome);
            log.info("Reserva de {} encontrada", nome);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            log.error("Não foi possível fazer a busca pelo nome {}. {}", nome, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/data/{data}")
    public ResponseEntity<BuscaDataDTO> buscaPorData(@PathVariable String data) {
        String result;
        BuscaDataDTO dto;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dataFormatada = LocalDate.parse(data, formatter);
            String dataExibida = dataFormatada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            List<LocalDate> datasDisponiveis = this.reservaService.listarDatasDisponiveis();
            var dataConsulta = datasDisponiveis.contains(dataFormatada);
            if (dataConsulta) {
                result = "Data " + dataExibida + " disponível!";
                dto = new BuscaDataDTO(result, 1);
                log.info("Data {} disponível!", dataExibida);
            } else {
                result = "Data " + dataExibida + " indisponível!";
                dto = new BuscaDataDTO(result, 0);
                log.info("Data {} indisponível!", dataExibida);
            }
            return ResponseEntity.ok().body(dto);
        } catch (Exception e) {
            log.error("Não foi possível fazer a busca pela data {}. {}", data, e.getMessage());
            return ResponseEntity.badRequest().body(new BuscaDataDTO(e.getMessage(), 400));
        }
    }


    @GetMapping("/datas")
    public ResponseEntity<Object> datasDisponiveis() {
        try {
            var result = this.reservaService.listarDatasDisponiveis();
            log.info("Datas disponíveis listadas");
            return ResponseEntity.ok().body(new DatasDisponiveis(result));
        } catch (Exception e) {
            log.error("Não foi possível listar as datas disponíveis. {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarReserva(@PathVariable Long id) {
        try {
            String nome = this.reservaService.excluirReserva(id);
            log.info("Reserva de {} excluída com sucesso!", nome);
            return ResponseEntity.ok().body("Reserva de " + nome + "Excluida com sucesso");

        } catch (Exception e) {
            log.error("Não foi possível excluir a reserva id {}. {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
