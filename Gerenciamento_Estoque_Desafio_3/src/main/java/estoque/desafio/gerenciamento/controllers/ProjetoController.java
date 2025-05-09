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

import estoque.desafio.gerenciamento.entities.Projeto;
import estoque.desafio.gerenciamento.services.ProjetoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/projeto")
@Tag(name = "projeto")
public class ProjetoController {

	private ProjetoService projetoService;

	public ProjetoController(ProjetoService projetoService) {
		this.projetoService = projetoService;
	}
	
	@Operation(summary = "Adiciona um novo projeto:")
	@PostMapping("/adicionar")
	public ResponseEntity<?> criarProjeto(@RequestBody Projeto projeto) {
		try {
			Projeto projetoCriado = projetoService.criarProjeto(projeto);
			return ResponseEntity.ok(projetoCriado);
		} catch (Exception e) {
			return new ResponseEntity<>("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}
	
	@Operation(summary = "Retorna todos os projetos:")
	@GetMapping("/buscar")
	public ResponseEntity<?> listarProjetos() {
		try {
			List<Projeto> projetos = projetoService.listarProjetos();
			return ResponseEntity.ok(projetos);
		} catch (Exception e) {
			return new ResponseEntity<>("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}
	
	@Operation(summary = "Edita um projeto:")
	@PatchMapping("/alterar/{idProjeto}")
	public ResponseEntity<?> editarProjeto(@PathVariable int idProjeto, @RequestBody Projeto projeto) {
		try {
			Projeto projetoEditado = projetoService.editarProjeto(idProjeto, projeto);
			return ResponseEntity.ok(projetoEditado);
		} catch (Exception e) {
			return new ResponseEntity<>("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}
	
	@Operation(summary = "Deleta um projeto:")
	@DeleteMapping("/excluir/{codigo}")
	public ResponseEntity<?> excluirProjeto(@PathVariable Long codigo) {
		try {
			projetoService.excluirProjeto(codigo);
			return ResponseEntity.ok("Excluido com Sucesso");
		} catch (Exception e) {
			return new ResponseEntity<>("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}

}
