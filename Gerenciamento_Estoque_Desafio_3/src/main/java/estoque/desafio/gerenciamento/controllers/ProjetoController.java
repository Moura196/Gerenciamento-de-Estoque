package estoque.desafio.gerenciamento.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import estoque.desafio.gerenciamento.entities.Projeto;
import estoque.desafio.gerenciamento.entities.Usuario;
import estoque.desafio.gerenciamento.services.ProjetoService;
import estoque.desafio.gerenciamento.repositories.ProjetoRepository;
import estoque.desafio.gerenciamento.repositories.UsuarioRepository;
import estoque.desafio.gerenciamento.entities.dtos.ProjetoRequest;
import estoque.desafio.gerenciamento.repositories.CompraRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RestController
@RequestMapping("/projeto")
@Tag(name = "projeto")
@CrossOrigin(origins = "*")
public class ProjetoController {

	private ProjetoService projetoService;
	private ProjetoRepository projetoRepository;
	private UsuarioRepository usuarioRepository;
	private CompraRepository compraRepository;

	public ProjetoController(ProjetoService projetoService, UsuarioRepository usuarioRepository, CompraRepository compraRepository, ProjetoRepository projetoRepository) {
		this.projetoService = projetoService;
		this.usuarioRepository = usuarioRepository;
		this.compraRepository = compraRepository;
		this.projetoRepository = projetoRepository;
	}

	@Operation(summary = "Adiciona um novo projeto:")
	@PostMapping("/adicionar")
	public ResponseEntity<?> criarProjeto(@RequestBody @Valid ProjetoRequest request) {
		log.info("Iniciando criação de projeto. Request: {}", request); // Log inicial

		try {
			if (projetoRepository.existsByIdProjeto(request.getIdProjeto())) {
            	return ResponseEntity.badRequest()
                   .body("Já existe um projeto com este ID");
        	}

			log.debug("Buscando usuário com ID: {}", request.getUsuarioId());
			Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
					.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

			log.debug("Criando novo projeto...");
			Projeto projeto = new Projeto();
			projeto.setIdProjeto(request.getIdProjeto());
			projeto.setNome(request.getNome());
			projeto.setApelidoProjeto(request.getApelidoProjeto());
			projeto.setUsuario(usuario);

			log.debug("Salvando projeto...");
			Projeto projetoSalvo = projetoRepository.save(projeto);

			if (request.getComprasIds() != null && !request.getComprasIds().isEmpty()) {
				log.debug("Atualizando {} compras para o projeto...", request.getComprasIds().size());
				compraRepository.findAllById(request.getComprasIds()).forEach(compra -> {
					log.trace("Atualizando compra ID: {}", compra.getCodigo());
					compra.setProjeto(projetoSalvo);
					compraRepository.save(compra);
				});
			}

			log.info("Projeto criado com sucesso. ID: {}", projetoSalvo.getCodigo());
			return ResponseEntity.ok(projetoSalvo);

		} catch (Exception e) {
			log.error("ERRO ao criar projeto: ", e); // Log completo do erro
			return ResponseEntity.internalServerError()
					.body("Erro ao criar projeto: " + e.getMessage());
		}
	}

	@Operation(summary = "Retorna todos os projetos:")
	@GetMapping("/buscar")

	public ResponseEntity<List<Projeto>> listarProjetos() {
		System.out.println("Recebida requisição GET /projeto/buscar");
		try {
			List<Projeto> projetos = projetoService.listarProjetos();
			System.out.println("Saiu para o service!");
			return ResponseEntity.ok().body(projetos);
		} catch (Exception e) {
			System.err.println("Erro em /projeto/buscar: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
		}
	}

	@PatchMapping("/alterar/{codigo}")
	public ResponseEntity<?> editarProjeto(
			@PathVariable Long codigo,
			@RequestBody @Valid Projeto projetoAtualizado) {

		try {
			Projeto projetoEditado = projetoService.editarProjeto(codigo, projetoAtualizado);
			return ResponseEntity.ok(projetoEditado);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao editar projeto: " + e.getMessage());
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
