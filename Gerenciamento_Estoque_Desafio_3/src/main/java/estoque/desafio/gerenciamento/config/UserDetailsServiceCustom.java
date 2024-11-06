package estoque.desafio.gerenciamento.config;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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
		
		String role = usuario.get().getFuncao();
		if(!role.startsWith("ROLE_"))
			role = "ROLE_"+role;
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
		Set<GrantedAuthority> authorities = new HashSet();
		authorities.add(authority);
		
		User user = new User(usuario.get().getMatricula(), usuario.get().getSenha(), authorities);
		
		return user;
	}
	
}
