package estoque.desafio.gerenciamento.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import estoque.desafio.gerenciamento.entities.dtos.ItemDTO;
//import estoque.desafio.gerenciamento.entities.Compra;
//import estoque.desafio.gerenciamento.entities.Fornecedor;
import estoque.desafio.gerenciamento.entities.Item;
import estoque.desafio.gerenciamento.services.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/item")
@Tag(name = "item")
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Operation(summary = "Adiciona um novo item:")
    @PostMapping("/adicionar")
    public ResponseEntity<?> criarItem(@RequestBody Item item) {
        try {
            System.out.println("Dados recebidos:");
            System.out.println("Descrição recebida: " + item.getDescricao());
            System.out.println("Patrimônio: " + item.getPatrimonio());
            System.out.println("Fornecedor ID: " + item.getFornecedor().getCodigo());
            System.out.println("Armazenamento ID: " + item.getArmazenamento().getCodigo());
            System.out.println("Compra ID: " + item.getCompra().getCodigo());
            Item itemCriado = itemService.criarItem(item);
            return ResponseEntity.ok(itemCriado);
        } catch (Exception e) {
            System.err.println("Erro ao criar item:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                    .body("Erro ao criar item: " + e.getMessage());
        }
    }

    @Operation(summary = "Busca todos")
    @GetMapping("/buscar")
    public ResponseEntity<List<Item>> listarTodosItens() {
        return ResponseEntity.ok(itemService.listarTodos());
    }

    @GetMapping("/buscar/id/{id}")
    public ResponseEntity<ItemDTO> buscarItemPorId(@PathVariable Long id) {
        return itemService.buscarPorId(id)
                .map(item -> {
                    ItemDTO dto = new ItemDTO();
                    dto.setCodigo(item.getCodigo());
                    dto.setDescricao(item.getDescricao());
                    dto.setValorUnitario(item.getValorUnitario());
                    dto.setPatrimonio(item.getPatrimonio());
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Busca itens por nome (contendo)")
    @GetMapping("/buscar/nome")
    public ResponseEntity<List<Item>> buscarItensPorNome(@RequestParam String nome) {
        System.out.println("Buscando itens por nome: " + nome);
        List<Item> itens = itemService.buscarPorNomeContendo(nome);
        System.out.println("Itens encontrados: " + itens.size());
        return ResponseEntity.ok(itens);
    }

    @Operation(summary = "Busca um item por patrimonio:")
    @GetMapping("/buscar/patrimonio/{patrimonio}")
    public ResponseEntity<Item> buscarItemPorPatrimonio(@PathVariable String patrimonio) {
        return itemService.buscarItemPorPatromonio(patrimonio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Edita um item:")
    @PatchMapping("/atualizar/{patrimonio}")
    public ResponseEntity<?> editarItem(@PathVariable String patrimonio, @RequestBody Item item) {
        try {
            Item itemEditado = itemService.editarItem(patrimonio, item);
            return ResponseEntity.ok(itemEditado);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao atualizar armazenamento: ", HttpStatusCode.valueOf(500));
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarItem(@RequestBody @Valid ItemDTO itemDTO) {
        try {
            Item itemAtualizado = itemService.atualizarItem(itemDTO);
            return ResponseEntity.ok(itemAtualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Erro ao atualizar item: " + e.getMessage());
        }
    }

    @Operation(summary = "Exclui um item:")
    @DeleteMapping("/excluir/{codigo}")
    public ResponseEntity<?> excluirItem(@PathVariable Long codigo) {
        try {
            itemService.excluirItem(codigo);
            return ResponseEntity.ok("Item excluído com sucesso");
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao excluir armazenamento: ", HttpStatusCode.valueOf(504));
        }
    }

    // @Operation(summary = "Edita um item:")
    // @PutMapping("/atualizar/{id}")
    // public ResponseEntity<?> atualizarItem(
    //         @PathVariable Long id,
    //         @RequestBody Item itemAtualizado) {

    //     try {
    //         if (!id.equals(itemAtualizado.getCodigo())) {
    //             return ResponseEntity.badRequest().body("ID do item não corresponde");
    //         }

    //         Item itemSalvo = itemService.atualizarItem(itemAtualizado);
    //         return ResponseEntity.ok(itemSalvo);
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     } catch (Exception e) {
    //         return ResponseEntity.internalServerError().body("Erro ao atualizar item");
    //     }
    // }
}
