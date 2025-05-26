package estoque.desafio.gerenciamento.config;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTValidateFilter extends BasicAuthenticationFilter {

    public static final String PREFIX = "Bearer ";
    private static final Set<String> PUBLIC_PATHS = Set.of(
        "/", "/home", "/home.html", "/login", "/login.html",
        "/static/", "/css/", "/js/", "/images/",
        "/swagger-ui/", "/v3/api-docs/"
    );
    
    public JWTValidateFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain chain) 
            throws IOException, ServletException {
        
        String path = request.getServletPath();
        
        // Ignora rotas públicas
        if (PUBLIC_PATHS.stream().anyMatch(path::startsWith)) {
            chain.doFilter(request, response);
            return;
        }
        
        String atributo = request.getHeader("Authorization");
        
        if(atributo == null || !atributo.startsWith(PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        
        String token = atributo.replace(PREFIX, "");
        
        try {
            UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token inválido");
            return;
        }
        
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        DecodedJWT decodedJWT = JWT
                .require(Algorithm.HMAC256(JWTAuthenticationFilter.SECRET_JWT))
                .build().verify(token);
        
        String usuario = decodedJWT.getSubject();
        
        String permissao = decodedJWT.getClaim("permissao").asString();
        permissao = "ROLE_"+permissao;
        
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permissao);
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(authority);
        
        if (usuario != null)
            return new UsernamePasswordAuthenticationToken(usuario, null, authorities);
        
        return null;
    }
}