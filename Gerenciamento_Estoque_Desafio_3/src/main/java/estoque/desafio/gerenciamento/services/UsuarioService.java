package estoque.desafio.gerenciamento.services;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import estoque.desafio.gerenciamento.entities.Usuario;
import estoque.desafio.gerenciamento.entities.dtos.AtualizarSenhaDTO;
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
   
    	String matriculaGerada = gerarMatriculaUnica();
    	usuario.setMatricula(matriculaGerada);
    
    	String pass = passwordEncoder.encode(usuario.getSenha());
    	usuario.setSenha(pass);
    
    	return usuarioRepository.save(usuario);
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

	@Transactional
	public Usuario atualizarUsuario(Usuario usuario) {
    	if (usuario.getCodigo() == null) {
        	throw new IllegalArgumentException("Código do usuário não pode ser nulo");
    	}
    
    	System.out.println("Buscando usuário com ID: " + usuario.toString());
    	Optional<Usuario> usuarioExistente = usuarioRepository.findById(usuario.getCodigo());
    
    	if (usuarioExistente.isPresent()) {
        	Usuario usuarioAtual = usuarioExistente.get();
        	System.out.println("Usuário encontrado: " + usuarioAtual.toString());
        
        	
        	if (usuario.getNome() != null) {
            	usuarioAtual.setNome(usuario.getNome());
        	}
        
        	if (usuario.getFuncao() != null) {
            	usuarioAtual.setFuncao(usuario.getFuncao());
        	}
			System.out.println("Usuário após atualização: " + usuarioAtual.toString());
        	return usuarioRepository.save(usuarioAtual);
			
    	} else {
        	String msg = "Usuário não encontrado com o código: " + usuario.getCodigo();
        	System.err.println(msg);
        	throw new RuntimeException(msg);
    	}
	}
	//Gera matricula automaticamente
	private String gerarMatriculaUnica() {
    	String matricula;
    	do {
        	int numero = ThreadLocalRandom.current().nextInt(100000, 1000000);
       	 	matricula = String.valueOf(numero);
    	} while (usuarioRepository.existsByMatricula(matricula)); 
    
    	return matricula;
	}
}