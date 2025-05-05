package estoque.desafio.gerenciamento.controllers;

import estoque.desafio.gerenciamento.entities.Fornecedor;
import estoque.desafio.gerenciamento.services.FornecedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fornecedor")
@Tag(name = "fornecedor")
public class FornecedorController {

    private FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @Operation(summary = "Retorna todos os fornecedores:")
    @GetMapping("/buscar")
    public ResponseEntity<?> listarFornecedores() {
        try {
            List<Fornecedor> fornecedores = fornecedorService.listarFornecedores();
            return ResponseEntity.ok(fornecedores);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro de consulta", HttpStatusCode.valueOf(504));
        }
    }

    @Operation(summary = "Busca um fornecedor por nome:")
    @GetMapping("/buscar/{nome}")
    public ResponseEntity<?> buscarFornecedorPorId(@PathVariable String nome) {
        try{
            Optional<Fornecedor> fornecedor = fornecedorService.buscarFornecedorPorNome(nome);
            return ResponseEntity.ok(fornecedor);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro de consulta", HttpStatusCode.valueOf(504));
        }
    }

    @Operation(summary = "Adiciona um novo fornecedor:")
    @PostMapping("/adicionar")
    public ResponseEntity<?> criarFornecedor(@RequestBody Fornecedor fornecedor) {
        try {
            Fornecedor fornecedorCriado = fornecedorService.criarFornecedor(fornecedor);
            return ResponseEntity.ok(fornecedorCriado);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao criar fornecedor", HttpStatusCode.valueOf(504));
        }
    }

    @Operation(summary = "Edita um fornecedor:")
    @PatchMapping("/alterar/{nome}")
    public ResponseEntity<?> editarFornecedor(@PathVariable String nome, @RequestBody Fornecedor fornecedor) {
        try {
            Fornecedor fornecedorEditado = fornecedorService.editarFornecedor(nome, fornecedor);
            return ResponseEntity.ok(fornecedorEditado);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao atualizar fornecedor: ", HttpStatusCode.valueOf(500));
        }
    }

    @Operation(summary = "Exclui um fornecedor:")
    @DeleteMapping("/excluir/{codigo}")
    public ResponseEntity<?> excluirFornecedor(@PathVariable Long codigo) {
        try{
            fornecedorService.excluirFornecedor(codigo);
            return ResponseEntity.ok("Fornecedor excluido com sucesso");
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao excluir fornecedor: ", HttpStatusCode.valueOf(504));
        }
    }
}
