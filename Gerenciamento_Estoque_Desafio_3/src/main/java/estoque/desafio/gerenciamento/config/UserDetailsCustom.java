package estoque.desafio.gerenciamento.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import estoque.desafio.gerenciamento.entities.Usuario;

public class UserDetailsCustom implements UserDetails {
	
	private final Optional<Usuario> usuario;

	public UserDetailsCustom(Optional<Usuario> usuario) {
		this.usuario = usuario;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

    	Set<GrantedAuthority> authorities = new HashSet<>();
    	String funcao = usuario.orElse(new Usuario()).getFuncao();
    	if (funcao != null && !funcao.isEmpty()) {
        	String role = funcao.startsWith("ROLE_") ? funcao : "ROLE_" + funcao;
        	authorities.add(new SimpleGrantedAuthority(role));
    	}
    	return authorities;
	}
	
	@Override
	public  String getPassword() {
		return usuario.orElse(new Usuario()).getSenha();
	}
	
	@Override
	public  String getUsername() {
		return usuario.orElse(new Usuario()).getMatricula();
	}
	
	@Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
