package estoque.desafio.gerenciamento.controllers;

import estoque.desafio.gerenciamento.entities.Armazenamento;
import estoque.desafio.gerenciamento.services.ArmazenamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/armazenamentos")
public class ArmazenamentoController {

    @Autowired
    private ArmazenamentoService armazenamentoService;


    @GetMapping
    public ResponseEntity<List<Armazenamento>> getAll() {
        List<Armazenamento> armazenamentos = armazenamentoService.findAll();
        return ResponseEntity.ok(armazenamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Armazenamento> getById(@PathVariable Long id) {
        Optional<Armazenamento> armazenamento = armazenamentoService.findById(id);
        return armazenamento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Armazenamento> create(@RequestBody Armazenamento armazenamento) {
        Armazenamento novoArmazenamento = armazenamentoService.save(armazenamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoArmazenamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Armazenamento> update(@PathVariable Long id, @RequestBody Armazenamento armazenamento) {
        if (!armazenamentoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        armazenamento.setCodigo(id);
        Armazenamento armazenamentoAtualizado = armazenamentoService.save(armazenamento);
        return ResponseEntity.ok(armazenamentoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!armazenamentoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        armazenamentoService.deleteByID(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{armazenamentoId}/itens/{itemId}")
    public ResponseEntity<Void> removeItemDeArmazenamento(Long armazenamentoId, Long itemId){

        boolean removed = armazenamentoService.removeItemDeArmazenamento(armazenamentoId, itemId);

        if (removed){
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.notFound().build();
        }

    }

}
