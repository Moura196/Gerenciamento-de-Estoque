package estoque.desafio.gerenciamento.controllers;

import estoque.desafio.gerenciamento.entities.Armazenamento;
import estoque.desafio.gerenciamento.services.ArmazenamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/armazenamento")
@Tag(name = "armazenamento")
public class ArmazenamentoController {

    private ArmazenamentoService armazenamentoService;

    public ArmazenamentoController(ArmazenamentoService armazenamentoService) {
        this.armazenamentoService = armazenamentoService;
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
    public ResponseEntity<?> buscarArmazenamentoPorSalaAndArmario(@RequestParam String sala, @RequestParam String armario) {
        try {
            Optional<Armazenamento> armazenamento = armazenamentoService.buscarArmazenamentoPorSalaAndArmario(sala, armario);
            return ResponseEntity.ok(armazenamento);
        } catch (Exception e) {
        	return new ResponseEntity<>("Erro de consulta", HttpStatusCode.valueOf(504));
        }
    }
    
    @Operation(summary = "Adiciona um novo armazenamento:")
    @PostMapping("/adicionar")
    public ResponseEntity<?> criarArmazenamento(@RequestBody Armazenamento armazenamento) {
        try {
            Armazenamento armazenamentoCriado = armazenamentoService.criarArmazenamento(armazenamento);
            return ResponseEntity.ok(armazenamentoCriado);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao criar armazenamento", HttpStatusCode.valueOf(504));
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
    	try{
            armazenamentoService.excluirArmazenamento(codigo);
            return ResponseEntity.ok("Armazenamento excluido com sucesso");
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao excluir armazenamento: ", HttpStatusCode.valueOf(504));
        }
    }

}
