package estoque.desafio.gerenciamento.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import estoque.desafio.gerenciamento.entities.Usuario;
import estoque.desafio.gerenciamento.entities.dtos.usuario.AtualizarSenhaDTO;
import estoque.desafio.gerenciamento.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/usuario")
@Tag(name = "usuario")
public class UsuarioController {
	
	private UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@Operation(summary = "Retorna todos os usuários:")
	@GetMapping("/buscar")
	public ResponseEntity<?> listarUsuarios() {
		try {
			List<Usuario> usuarios = usuarioService.listarUsuarios();
			return ResponseEntity.ok(usuarios);
		} catch (Exception e) {
			return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}
	
	@Operation(summary = "Adiciona um novo usuário:")
	@PostMapping("/adicionar")
	public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) {
		try {
			Usuario usuarioCriado = usuarioService.criarUsuario(usuario);
			return ResponseEntity.ok(usuarioCriado);
		} catch (Exception e) {
			return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}
	
	@Operation(summary = "Atualiza a senha do usuário:")
	@PatchMapping("/alterar/senha")
	public ResponseEntity<?> atualizarSenha(@RequestBody AtualizarSenhaDTO atualizarSenhaDTO) {
		try {
			Usuario usuario = usuarioService.atualizarSenha(atualizarSenhaDTO);
			return ResponseEntity.ok(usuario);
		} catch (Exception e) {
			return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}
	
	@Operation(summary = "Deleta um usuário por código:")
	@DeleteMapping("/excluir/{codigo}")
	public ResponseEntity<?> excluirUsuario(@PathVariable Long codigo) {
		try {
			usuarioService.excluirUsuario(codigo);
			return ResponseEntity.ok("Excluido com Sucesso");
		} catch (Exception e) {
			return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}
	
	@Operation(summary = "Busca um usuário por código:")
	@GetMapping("/buscar/{codigo}")
	public ResponseEntity<?> buscarUsuarioPorCodigo(@PathVariable Long codigo) {
		try {
			Optional<Usuario> usuario = usuarioService.buscarUsuarioPorCodigo(codigo);
			return ResponseEntity.ok(usuario);
		} catch (Exception e) {
			return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}
	
	@Operation(summary = "Busca um usuário por matrícula:")
	@GetMapping("/buscar/matricula/{matricula}")
	public ResponseEntity<?> buscarUsuarioPorMatricula(@PathVariable String matricula) {
		try {
			Optional<Usuario> usuario = usuarioService.buscarUsuarioPorMatricula(matricula);
			return ResponseEntity.ok(usuario);
		} catch (Exception e) {
			return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}
	
}
