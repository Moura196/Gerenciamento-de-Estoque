package estoque.desafio.gerenciamento.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import estoque.desafio.gerenciamento.entities.Usuario;
import estoque.desafio.gerenciamento.entities.dtos.AtualizarSenhaDTO;
import estoque.desafio.gerenciamento.entities.dtos.LoginDTO;
import estoque.desafio.gerenciamento.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	private UsuarioRepository usuarioRepository;
	private PasswordEncoder passwordEncoder;

	public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public Usuario criarUsuario(Usuario usuario) {
		String pass = passwordEncoder.encode(usuario.getSenha());
		usuario.setSenha(pass);
		usuarioRepository.save(usuario);
		return usuario;
	}

	public List<Usuario> listarUsuarios() {
		return usuarioRepository.findAll();
	}

	public Usuario atualizarSenha(AtualizarSenhaDTO senhaUsuarioDTO) {
		Optional<Usuario> usuario = usuarioRepository.findById(senhaUsuarioDTO.getCodigo());
		String pass = passwordEncoder.encode(senhaUsuarioDTO.getSenha());
		usuario.get().setSenha(pass);
		usuarioRepository.save(usuario.get());
		return usuario.get();
	}
	
	public void excluirUsuario(Long codigo) {
		usuarioRepository.deleteById(codigo);
	}
	
	public Optional<Usuario> getUsuarioAutenticacao(String username) {
		return usuarioRepository.findByMatricula(username);
	}
	
	public boolean isAuthenticated(LoginDTO loginDTO) {
		Optional<Usuario> usuario = usuarioRepository.findByMatricula(loginDTO.getMatricula());
		if(Optional.ofNullable(usuario).isPresent() && usuario.get().getSenha().equals(loginDTO.getSenha()))
			return true;
		return false;
	}
	
//	public String isAuthenticated(LoginDTO loginDTO) {
//		Optional<Usuario> usuario = usuarioRepository.findByMatricula(loginDTO.getMatricula());
//		if(Optional.ofNullable(usuario).isPresent() && usuario.get().getSenha().equals(loginDTO.getSenha())) {
//			UUID uuid = UUID.randomUUID();
//			String myRandom = uuid.toString();
//			return myRandom;
//		}
//		return "";
//	}

}