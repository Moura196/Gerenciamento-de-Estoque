package estoque.desafio.gerenciamento.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@SuppressWarnings("unused")
    private final OpenApiConfig openApiConfig;

    SecurityConfig(OpenApiConfig openApiConfig) {
        this.openApiConfig = openApiConfig;
    }
	
	@Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {
        return httpSecurity
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/",
                    "/static/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/index",
                    "/index.html",
                    "/login",
                    "/login.html"
                ).permitAll()
                .requestMatchers("/usuario/**").hasRole("GP")
                .anyRequest().authenticated()
            )
            .addFilter(new JWTAuthenticationFilter(authenticationManager))
            .addFilter(new JWTValidateFilter(authenticationManager))
            .build();
    }
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
