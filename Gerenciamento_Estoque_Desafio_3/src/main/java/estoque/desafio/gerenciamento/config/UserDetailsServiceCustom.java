package estoque.desafio.gerenciamento.config;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import estoque.desafio.gerenciamento.entities.Usuario;
import estoque.desafio.gerenciamento.services.UsuarioService;

@Service
public class UserDetailsServiceCustom implements UserDetailsService {

	private final UsuarioService usuarioService;

	public UserDetailsServiceCustom(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = usuarioService.getUsuarioAutenticacao(username);
		if(!usuario.isPresent())
			new UsernameNotFoundException("Usuário não encontrado!");
		
		return new UserDetailsCustom(usuario);
	}
	
}
