package estoque.desafio.gerenciamento.controllers;

import java.util.List;

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

import estoque.desafio.gerenciamento.entities.Compra;
import estoque.desafio.gerenciamento.entities.Projeto;
import estoque.desafio.gerenciamento.services.CompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/compra")
@Tag(name = "compra")
public class CompraController {
	
	private CompraService compraService;

	public CompraController(CompraService compraService) {
		this.compraService = compraService;
	}
	
	@Operation(summary = "Adiciona uma nova compra:")
	@PostMapping("/adicionar")
	public ResponseEntity<?> criarCompra(@RequestBody Compra compra) {
		try {
			Compra compraCriada = compraService.criarCompra(compra);
			return ResponseEntity.ok(compraCriada);
		} catch (Exception e) {
			return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}
	
	@Operation(summary = "Retorna todas as compras:")
	@GetMapping("/buscar")
	public ResponseEntity<?> listarCompras() {
		try {
			List<Compra> compras = compraService.listarCompras();
			return ResponseEntity.ok(compras);
		} catch (Exception e) {
			return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}
	
	@Operation(summary = "Deleta uma compra:")
	@DeleteMapping("/excluir/{codigo}")
	public ResponseEntity<?> excluirCompra(@PathVariable Long codigo) {
		try {
			compraService.excluirCompra(codigo);
			return ResponseEntity.ok("Excluido com Sucesso");
		} catch (Exception e) {
			return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}
	
	@Operation(summary = "Edita uma  compra:")
	@PatchMapping("/alterar/{codigo}")
	public ResponseEntity<?> editarCompra(@PathVariable Long codigo, @RequestBody Compra compra) {
		try {
			Compra compraEditada = compraService.editarCompra(codigo, compra);
			return ResponseEntity.ok(compraEditada);
		} catch (Exception e) {
			return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}

}
