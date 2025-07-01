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
    public ResponseEntity<?> criarItem(@Valid @RequestBody ItemDTO itemDTO) {
        try {
            Item item = itemService.criarItem(itemDTO);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Erro ao criar item: " + e.getMessage());
        }
    }

    @Operation(summary = "Busca todos")
    @GetMapping("/buscar")
    public ResponseEntity<List<Item>> listarTodosItens() {
        return ResponseEntity.ok(itemService.listarTodos());
    }

    @Operation(summary = "Busca itens sem compra vinculada")
    @GetMapping("/disponiveis")
    public ResponseEntity<List<Item>> buscarItensSemCompra(@RequestParam String termo) {
        return ResponseEntity.ok(itemService.buscarItensSemCompra(termo));
    }

    @GetMapping("/buscar/id/{codigo}")
    public ResponseEntity<ItemDTO> buscarItemPorId(@PathVariable Long codigo) {
        return itemService.buscarPorId(codigo)
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
        List<Item> itens = itemService.buscarPorNomeContendo(nome);
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
}
