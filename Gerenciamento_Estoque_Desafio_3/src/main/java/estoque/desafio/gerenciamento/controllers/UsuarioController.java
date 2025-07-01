package estoque.desafio.gerenciamento.controllers;

import java.util.List;
import java.util.Optional;
import java.util.Date;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import estoque.desafio.gerenciamento.config.JWTAuthenticationFilter;
import estoque.desafio.gerenciamento.config.UserDetailsCustom;
import estoque.desafio.gerenciamento.entities.Usuario;
import estoque.desafio.gerenciamento.entities.dtos.AtualizarSenhaDTO;
import estoque.desafio.gerenciamento.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import java.util.Collections;


@RestController
@RequestMapping("/usuario")
@Tag(name = "usuario")
public class UsuarioController {

	private UsuarioService usuarioService;
	private final AuthenticationManager authenticationManager;

	public UsuarioController(UsuarioService usuarioService, AuthenticationManager authenticationManager) {
		this.usuarioService = usuarioService;
		this.authenticationManager = authenticationManager;
	}

	@Operation(summary = "Adiciona um novo usuário:")
	@PostMapping("/adicionar")
	public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) {
		try {
			Usuario usuarioCriado = usuarioService.criarUsuario(usuario);
			return ResponseEntity.ok(usuarioCriado);
		} catch (Exception e) {
			return new ResponseEntity<>("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}

	@Operation(summary = "Retorna todos os usuários:")
	@GetMapping("/buscar")
	public ResponseEntity<?> listarUsuarios() {
    	try {
        	List<Usuario> usuarios = usuarioService.listarUsuarios();
        	return ResponseEntity.ok(usuarios);
    	} catch (Exception e) {
        	e.printStackTrace();
        	return new ResponseEntity<>("Erro de Consulta", HttpStatus.INTERNAL_SERVER_ERROR);
    	}
	}

	@Operation(summary = "Busca um usuário pelo código:")
	@GetMapping("/buscar/{codigo}")
	public ResponseEntity<?> buscarUsuarioPorCodigo(@PathVariable Long codigo) {
    	try {
        	Optional<Usuario> usuario = usuarioService.buscarUsuarioPorCodigo(codigo);
        
        	if (usuario.isPresent()) {
            	return ResponseEntity.ok(usuario.get());
        	} else {
            	return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuário não encontrado");
        	}
    	} catch (Exception e) {
        	System.err.println("Erro ao buscar usuário:");
        	e.printStackTrace();
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao buscar usuário: " + e.getMessage());
    	}
	}

	@Operation(summary = "Busca um usuário pela matrícula:")
	@GetMapping("/buscar/matricula/{matricula}")
	public ResponseEntity<?> buscarUsuarioPorMatricula(@PathVariable String matricula) {
		try {
			Optional<Usuario> usuario = usuarioService.buscarUsuarioPorMatricula(matricula);
			return ResponseEntity.ok(usuario);
		} catch (Exception e) {
			return new ResponseEntity<>("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}

	@Operation(summary = "Atualiza a senha de um usuário:")
	@PatchMapping("/alterar/senha")
	public ResponseEntity<?> atualizarSenha(@RequestBody AtualizarSenhaDTO atualizarSenhaDTO) {
		try {
			Usuario usuario = usuarioService.atualizarSenha(atualizarSenhaDTO);
			return ResponseEntity.ok(usuario);
		} catch (Exception e) {
			return new ResponseEntity<>("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}

	@Operation(summary = "Deleta um usuário:")
	@DeleteMapping("/excluir/{codigo}")
	public ResponseEntity<?> excluirUsuario(@PathVariable Long codigo) {
		try {
			usuarioService.excluirUsuario(codigo);
			return ResponseEntity.ok("Excluido com Sucesso");
		} catch (Exception e) {
			return new ResponseEntity<>("Erro de Consulta", HttpStatusCode.valueOf(504));
		}
	}

	@Operation(summary = "Autenticação de usuário:")
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Usuario usuario) {
    try {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(usuario.getMatricula(), usuario.getSenha())
        );

        UserDetailsCustom userDetails = (UserDetailsCustom) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream().findFirst().get().toString();

        String token = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 600000))
                .withClaim("funcao", role)
                .sign(Algorithm.HMAC256(JWTAuthenticationFilter.SECRET_JWT));

        return ResponseEntity.ok(Collections.singletonMap("token", token));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas!");
    }
	}

	@Operation(summary = "Atualiza um usuário:")
	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarUsuario(@RequestBody Usuario usuario) {
    	try {
			System.out.println("Recebendo solicitação para atualizar usuário: " + usuario.getCodigo());
        	System.out.println("Dados recebidos: " + usuario.toString());
			System.out.println("Tipo do código: " + usuario.getCodigo().getClass().getName());
        
        	Usuario usuarioAtualizado = usuarioService.atualizarUsuario(usuario);
        	return ResponseEntity.ok(usuarioAtualizado);
    	} catch (Exception e) {
			System.err.println("Erro ao atualizar usuário:");
        	e.printStackTrace();
        	return new ResponseEntity<>("Erro ao atualizar usuário: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
	}
}