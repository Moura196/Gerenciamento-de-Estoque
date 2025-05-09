package estoque.desafio.gerenciamento.controllers;

import java.util.List;
import java.util.Optional;

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
import estoque.desafio.gerenciamento.services.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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
            Item itemCriado = itemService.criarItem(item);
            return ResponseEntity.ok(itemCriado);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao criar armazenamento", HttpStatusCode.valueOf(504));
        }
    }
	
	@Operation(summary = "Retorna todos os itens:")
    @GetMapping("/buscar")
    public ResponseEntity<?> listarItens() {
        try {
            List<Item> itens = itemService.listarItens();
            return ResponseEntity.ok(itens);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro de consulta", HttpStatusCode.valueOf(504));
        }
    }
	
	@Operation(summary = "Retorna um item por patrimonio:")
    @GetMapping("/buscar/{patrimonio}")
    public ResponseEntity<?> buscarItemPorPatromonio(@PathVariable String patrimonio) {
        try {
            Optional<Item> item = itemService.buscarItemPorPatromonio(patrimonio);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
        	return new ResponseEntity<>("Erro de consulta", HttpStatusCode.valueOf(504));
        }
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
	
	@Operation(summary = "Exclui um item:")
    @DeleteMapping("/excluir/{codigo}")
    public ResponseEntity<?> excluirItem(@PathVariable Long codigo) {
    	try{
            itemService.excluirItem(codigo);
            return ResponseEntity.ok("Item excluído com sucesso");
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao excluir armazenamento: ", HttpStatusCode.valueOf(504));
        }
    }

}
