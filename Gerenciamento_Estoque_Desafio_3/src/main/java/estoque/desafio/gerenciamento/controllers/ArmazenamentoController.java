package estoque.desafio.gerenciamento.controllers;

import estoque.desafio.gerenciamento.entities.Armazenamento;
import estoque.desafio.gerenciamento.services.ArmazenamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RestController
@RequestMapping("/armazenamento")
@Tag(name = "armazenamento")
public class ArmazenamentoController {

    private ArmazenamentoService armazenamentoService;

    public ArmazenamentoController(ArmazenamentoService armazenamentoService) {
        this.armazenamentoService = armazenamentoService;
    }

    @Operation(summary = "Adiciona um novo armazenamento:")
    @PostMapping("/adicionar")
    public ResponseEntity<?> criarArmazenamento(@RequestBody @Valid Armazenamento armazenamento) {
        try {
            Armazenamento armazenamentoCriado = armazenamentoService.criarArmazenamento(armazenamento);
            return ResponseEntity.ok(armazenamentoCriado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                    .body("Erro ao criar armazenamento: " + e.getMessage());
        }
    }

    @Operation(summary = "Retorna um armazenamento por ID:")
    @GetMapping("/buscar/id/{codigo}")
    public ResponseEntity<?> buscarArmazenamentoPorId(@PathVariable Long codigo) {
        try {
            Optional<Armazenamento> armazenamento = armazenamentoService.buscarArmazenamentoPorId(codigo);
            return armazenamento.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao buscar armazenamento: " + e.getMessage());
        }
    }

    @Operation(summary = "Retorna todos os armazenamentos:")
    @GetMapping("/buscar")
    public ResponseEntity<?> listarArmazenamentos() {
        try {
            List<Armazenamento> armazenamentos = armazenamentoService.listarArmazenamentos();
            return ResponseEntity.ok(armazenamentos);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro de consulta", HttpStatusCode.valueOf(504));
        }
    }

    @Operation(summary = "Retorna um armazenamento por sala e armário:")
    @GetMapping("/buscar/sala_armario")
    public ResponseEntity<?> buscarArmazenamentoPorSalaAndArmario(@RequestParam String sala,
            @RequestParam String armario) {
        try {
            Optional<Armazenamento> armazenamento = armazenamentoService.buscarArmazenamentoPorSalaAndArmario(sala,
                    armario);
            return ResponseEntity.ok(armazenamento);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro de consulta", HttpStatusCode.valueOf(504));
        }
    }

    @Operation(summary = "Edita um armazenamento:")
    @PatchMapping("/atualizar/{codigo}")
    public ResponseEntity<?> editarArmazenamento(@PathVariable Long codigo, @RequestBody Armazenamento armazenamento) {
        try {
            Armazenamento armazenamentoEditado = armazenamentoService.editarArmazenamento(codigo, armazenamento);
            return ResponseEntity.ok(armazenamentoEditado);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao atualizar armazenamento: ", HttpStatusCode.valueOf(500));
        }
    }

    @Operation(summary = "Exclui um armazenamento:")
    @DeleteMapping("/excluir/{codigo}")
    public ResponseEntity<?> excluirArmazenamento(@PathVariable Long codigo) {
        try {
            armazenamentoService.excluirArmazenamento(codigo);
            return ResponseEntity.ok("Armazenamento excluido com sucesso");
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao excluir armazenamento: ", HttpStatusCode.valueOf(504));
        }
    }

}
