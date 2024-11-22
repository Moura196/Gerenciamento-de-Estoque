package estoque.desafio.gerenciamento.controllers;

import estoque.desafio.gerenciamento.entities.Armazenamento;
import estoque.desafio.gerenciamento.services.ArmazenamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/armazenamento")
public class ArmazenamentoController {

    private ArmazenamentoService armazenamentoService;

    public ArmazenamentoController(ArmazenamentoService armazenamentoService) {
        this.armazenamentoService = armazenamentoService;
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> listarArmazenamentos() {
        try {
            List<Armazenamento> armazenamentos = armazenamentoService.listarArmazenamento();
            return ResponseEntity.ok(armazenamentos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Erro ao buscar os armazenamentos");
        }
    }

    @GetMapping("/buscar/{codigo}")
    public ResponseEntity<?> buscarArmazenamentoPorCodigo(@PathVariable Long codigo) {
        try {
            Optional<Armazenamento> armazenamento = armazenamentoService.findById(codigo);
            if (armazenamento.isPresent()) {
                return ResponseEntity.ok(armazenamento.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Armazenamento não encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Erro ao buscar o armazenamento");
        }
    }

    @PostMapping("/adicionar")
    public ResponseEntity<?> criarArmazenamento(@RequestBody Armazenamento armazenamento) {
        try {
            Armazenamento novoArmazenamento = armazenamentoService.salvar(armazenamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoArmazenamento);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Erro ao criar o armazenamento");
        }
    }

    @PutMapping("/atualizar/{codigo}")
    public ResponseEntity<?> atualizarArmazenamento(@PathVariable Long codigo, @RequestBody Armazenamento armazenamento) {
        try {
            if (armazenamentoService.findById(codigo).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Armazenamento não encontrado");
            }
            armazenamento.setCodigo(codigo);
            Armazenamento armazenamentoAtualizado = armazenamentoService.salvar(armazenamento);
            return ResponseEntity.ok(armazenamentoAtualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Erro ao atualizar o armazenamento");
        }
    }

    @DeleteMapping("/excluir/{codigo}")
    public ResponseEntity<?> excluirArmazenamento(@PathVariable Long codigo) {
        try {
            if (armazenamentoService.findById(codigo).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Armazenamento não encontrado");
            }
            armazenamentoService.deleteById(codigo);
            return ResponseEntity.ok("Armazenamento excluído com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Erro ao excluir o armazenamento");
        }
    }

    @DeleteMapping("/excluir-item/{armazenamentoCodigo}/{itemCodigo}")
    public ResponseEntity<?> removerItemDeArmazenamento(@PathVariable Long armazenamentoCodigo, @PathVariable Long itemCodigo) {
        try {
            boolean removido = armazenamentoService.removeItemDeArmazenamento(armazenamentoCodigo, itemCodigo);
            if (removido) {
                return ResponseEntity.ok("Item removido do armazenamento com sucesso");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item ou armazenamento não encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Erro ao remover o item do armazenamento");
        }
    }
}
