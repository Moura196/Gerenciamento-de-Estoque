package estoque.desafio.gerenciamento.controllers;

import java.util.List;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import estoque.desafio.gerenciamento.entities.Usuario;
import estoque.desafio.gerenciamento.entities.dtos.AtualizarSenhaDTO;
import estoque.desafio.gerenciamento.entities.dtos.LoginDTO;
import estoque.desafio.gerenciamento.services.UsuarioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/usuario")
public class UsuarioController {
	
	private UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@GetMapping("/buscar")
	public ResponseEntity<?> listarUsuarios() {
		try {
			List<Usuario> usuarios = usuarioService.listarUsuarios();
			return ResponseEntity.ok(usuarios);
		} catch (Exception e) {
			return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}
	
	@PostMapping("/adicionar")
	public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) {
		try {
			Usuario usuarioCriado = usuarioService.criarUsuario(usuario);
			return ResponseEntity.ok(usuarioCriado);
		} catch (Exception e) {
			return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}
	
	@PatchMapping("/alterar/senha")
	public ResponseEntity<?> atualizarSenha(@RequestBody AtualizarSenhaDTO atualizarSenhaDTO) {
		try {
			Usuario usuario = usuarioService.atualizarSenha(atualizarSenhaDTO);
			return ResponseEntity.ok(usuario);
		} catch (Exception e) {
			return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}
	
	@DeleteMapping("/excluir/{codigo}")
	public ResponseEntity<?> excluirUsuario(@RequestBody Long codigo) {
		try {
			usuarioService.excluirUsuario(codigo);
			return ResponseEntity.ok("Excluido com Sucesso");
		} catch (Exception e) {
			return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> realizarLogin(@RequestBody LoginDTO loginDTO) {
		try {
			boolean autenticado = usuarioService.isAuthenticated(loginDTO);
			return ResponseEntity.ok(autenticado);
		} catch (Exception e) {
			return new ResponseEntity("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}
	
}
