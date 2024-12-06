package estoque.desafio.gerenciamento.controllers;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import estoque.desafio.gerenciamento.entities.Projeto;
import estoque.desafio.gerenciamento.entities.dtos.projeto.CriarProjetoDTO;
import estoque.desafio.gerenciamento.services.ProjetoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/projeto")
@Tag(name = "projeto")
public class ProjetoController {
	
	private ProjetoService projetoService;

	public ProjetoController(ProjetoService projetoService) {
		super();
		this.projetoService = projetoService;
	}
	
//	@Operation(summary = "Adiciona um novo projeto:")
//	@PostMapping("/adicionar")
//	public ResponseEntity<?> criarProjeto(@RequestBody Projeto projeto) {
//		try {
//			Projeto projetoCriado = projetoService.criarProjeto(projeto.get);
//			return ResponseEntity.ok(projetoCriado);
//		} catch (Exception e) {
//			return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
//		}
//	}

}
