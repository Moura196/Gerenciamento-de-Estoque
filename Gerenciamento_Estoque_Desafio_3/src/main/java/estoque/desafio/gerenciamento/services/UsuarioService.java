package estoque.desafio.gerenciamento.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import estoque.desafio.gerenciamento.entities.Usuario;
import estoque.desafio.gerenciamento.entities.dtos.usuario.AtualizarSenhaDTO;
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
	
	public Optional<Usuario> buscarUsuarioPorCodigo(Long codigo) {
		return usuarioRepository.findById(codigo);
	}
	
	public Optional<Usuario> buscarUsuarioPorMatricula(String matricula) {
		return usuarioRepository.findByMatricula(matricula);
	}

}