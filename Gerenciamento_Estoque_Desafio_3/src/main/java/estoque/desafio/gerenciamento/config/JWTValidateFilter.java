package estoque.desafio.gerenciamento.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTValidateFilter extends BasicAuthenticationFilter {

	public static final String PREFIX = "Bearer";
	
	public JWTValidateFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}
	
	@Override
	protected void  doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		String atributo = request.getHeader("Authorization");
		
		if(!Optional.ofNullable(atributo).isPresent()) {
			chain.doFilter(request, response);
			return;
		}
		
		if(!atributo.startsWith(PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		
		String token = atributo.replace(PREFIX, "");
		
		UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(token);
		
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		DecodedJWT decodeJWT = JWT
				.require(Algorithm.HMAC256(JWTAuthenticationFilter.SECRET_JWT))
				.build().verify(token);
		return null;
	}
	
}
