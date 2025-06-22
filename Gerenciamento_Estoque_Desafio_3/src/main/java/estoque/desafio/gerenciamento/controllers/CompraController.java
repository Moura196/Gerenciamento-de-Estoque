package estoque.desafio.gerenciamento.controllers;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import estoque.desafio.gerenciamento.entities.Item;
import estoque.desafio.gerenciamento.entities.Compra;
import estoque.desafio.gerenciamento.entities.Projeto;
import estoque.desafio.gerenciamento.entities.dtos.CompraInputDTO;
import estoque.desafio.gerenciamento.services.CompraService;
import estoque.desafio.gerenciamento.repositories.ItemRepository;
import estoque.desafio.gerenciamento.repositories.CompraRepository;
import estoque.desafio.gerenciamento.repositories.ProjetoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/compra")
@Tag(name = "compra")
public class CompraController {

    private final CompraService compraService;
    private final CompraRepository compraRepository;
    private final ProjetoRepository projetoRepository;
    private final ItemRepository itemRepository;

    public CompraController(CompraService compraService,
            CompraRepository compraRepository,
            ProjetoRepository projetoRepository,
            ItemRepository itemRepository) {
        this.compraService = compraService;
        this.compraRepository = compraRepository;
        this.projetoRepository = projetoRepository;
        this.itemRepository = itemRepository;
    }

    @Operation(summary = "Adiciona uma nova compra:")
    @PostMapping("/adicionar")
    public ResponseEntity<?> criarCompra(@RequestBody @Valid CompraInputDTO compraRequest) {
        try {
            if (compraRequest.getDataCompra() == null) {
                return ResponseEntity.badRequest().body("Data da compra é obrigatória");
            }
            if (compraRequest.getValorTotalInvoice() == null) {
                return ResponseEntity.badRequest().body("Valor total é obrigatório");
            }

            if (compraRequest.getProjeto() == null) {
                return ResponseEntity.badRequest().body("Projeto é obrigatório");
            }
            // Buscar o projeto
            Projeto projeto = projetoRepository.findById(compraRequest.getProjeto())
                    .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
            // Criar nova compra
            Compra compra = new Compra();
            compra.setDataCompra(compraRequest.getDataCompra());
            compra.setDataEnvio(compraRequest.getDataEnvio());
            compra.setDataEmissaoInvoice(compraRequest.getDataEmissaoInvoice());
            compra.setValorTotalInvoice(compraRequest.getValorTotalInvoice());
            compra.setObservacao(compraRequest.getObservacao());
            compra.setProjeto(projeto);
            // Processar itens
            if (compraRequest.getItensCodigos() != null) {
                for (Long itemCodigo : compraRequest.getItensCodigos()) {
                    Item item = itemRepository.findById(itemCodigo)
                            .orElseThrow(() -> new RuntimeException("Item não encontrado: " + itemCodigo));
                    item.setCompra(compra);
                    compra.getItens().add(item);
                }
            }

            Compra compraSalva = compraRepository.save(compra);
            return ResponseEntity.ok(compraSalva);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao criar compra: " + e.getMessage());
        }
    }

    @Operation(summary = "Retorna todas as compras:")
    @GetMapping("/buscar")
    public ResponseEntity<?> listarCompras() {
        try {
            List<Compra> compras = compraService.listarCompras();
            return ResponseEntity.ok(compras);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro de Consulta", HttpStatusCode.valueOf(504));
        }
    }

    @Operation(summary = "Busca uma compra pelo código")
    @GetMapping("/buscar/{codigo}")
    public ResponseEntity<?> buscarCompraPorCodigo(@PathVariable Long codigo) {
        try {
            return compraService.buscarCompraPorCodigo(codigo)
                    .map(compra -> {
                        Hibernate.initialize(compra.getItens());
                        if (compra.getProjeto() != null) {
                            Hibernate.initialize(compra.getProjeto());
                        }
                        return ResponseEntity.ok(compra);
                    })
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao buscar compra: " + e.getMessage());
        }
    }

    @Operation(summary = "Edita uma compra:")
    @PatchMapping("/alterar/{codigo}")
    public ResponseEntity<?> editarCompra(@PathVariable Long codigo, @RequestBody Compra compra) {
        try {
            Compra compraEditada = compraService.editarCompra(codigo, compra);
            return ResponseEntity.ok(compraEditada);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro de Consulta", HttpStatusCode.valueOf(504));
        }
    }

    @Operation(summary = "Deleta uma compra:")
    @DeleteMapping("/excluir/{codigo}")
    public ResponseEntity<?> excluirCompra(@PathVariable Long codigo) {
        try {
            compraService.excluirCompra(codigo);
            return ResponseEntity.ok("Excluido com Sucesso");
        } catch (Exception e) {
            return new ResponseEntity<>("Erro de Consulta", HttpStatusCode.valueOf(504));
        }
    }

}
