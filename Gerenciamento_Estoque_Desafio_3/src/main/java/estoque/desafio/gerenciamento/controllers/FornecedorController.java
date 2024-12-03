package estoque.desafio.gerenciamento.controllers;

import estoque.desafio.gerenciamento.entities.Fornecedor;
import estoque.desafio.gerenciamento.entities.dtos.FornecedorDTO;
import estoque.desafio.gerenciamento.services.FornecedorService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

    private FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarFornecedores() {
        try {
            List<Fornecedor> fornecedores = fornecedorService.listarFornecedores();
            return ResponseEntity.ok(fornecedores);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro de consulta", HttpStatusCode.valueOf(504));
        }
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarFornecedorPorId(@PathVariable Long id) {
        try{
            Optional<Fornecedor> fornecedor = fornecedorService.buscarFornecedorPorId(id);
            return ResponseEntity.ok(fornecedor);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro de consulta", HttpStatusCode.valueOf(504));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> criarFornecedor(@RequestBody FornecedorDTO fornecedor) {
        try {
            Fornecedor fornecedorCriado = fornecedorService.CriarFornecedor(fornecedor);
            return ResponseEntity.ok(fornecedorCriado);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao criar fornecedor", HttpStatusCode.valueOf(504));
        }
    }

    @PutMapping("/alterar")
    public ResponseEntity<?> atualizarFornecedor(@RequestBody Fornecedor fornecedor) {
        try {
            FornecedorDTO fornecedorDTO = new FornecedorDTO();
            fornecedorDTO.setNome(fornecedor.getNome());
            fornecedorDTO.setEmail(fornecedor.getEmail());
            fornecedorDTO.setTelefone(fornecedor.getTelefone());
            fornecedorDTO.setCnpj(fornecedor.getCnpj());
            fornecedorDTO.setEndereco(fornecedor.getEndereco());

            Fornecedor fornecedorAtualizado = fornecedorService.atualizarFornecedor(fornecedor.getCodigo(), fornecedorDTO);
            return ResponseEntity.ok(fornecedorAtualizado);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao atualizar fornecedor: " + e.getMessage(), HttpStatusCode.valueOf(500));
        }
    }

    @DeleteMapping("/excluir")
    public ResponseEntity<?> excluirFornecedor(@PathVariable Long id) {
        try{
            fornecedorService.excluirFornecedor(id);
            return ResponseEntity.ok("Fornecedor excluido com sucesso");
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao excluir fornecedor: " + e.getMessage(), HttpStatusCode.valueOf(504));
        }
    }
}
