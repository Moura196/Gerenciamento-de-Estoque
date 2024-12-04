package estoque.desafio.gerenciamento.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {
		return httpSecurity
				.csrf(c -> c.disable())
				.authorizeHttpRequests(
		authorizeConfig -> {
			authorizeConfig.requestMatchers("/usuario/**").hasRole("GP");  //.hasAnyRole("GP", "RT");
			//authorizeConfig.requestMatchers("/projeto/add").hasAnyRole("GP", "RT"); // role vai ser ou GP(Gerente de Projeto) e RT(Responsável Técnico)
			authorizeConfig.requestMatchers("/gerenciamento/swagger-ui/**")
				.permitAll()
				.requestMatchers("/v3/api-docs/**")
				.permitAll();
			authorizeConfig.anyRequest().authenticated();
		}
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
