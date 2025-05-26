package estoque.desafio.gerenciamento.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;

@Configuration
//@EnableWebSecurity
public class SecurityConfig {
    public final OpenApiConfig openApiConfig;

    public SecurityConfig(OpenApiConfig openApiConfig) {
        this.openApiConfig = openApiConfig;
    }
    @Profile("!dev")//excluir quando for para produção
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, 
            AuthenticationManager authenticationManager) throws Exception {
        return httpSecurity
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("*"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                config.setAllowedHeaders(List.of("*"));
                return config;
            }))
            .authorizeHttpRequests(auth -> auth
            .anyRequest().permitAll()) //para produção e testes comentar essa linha e descomentar o resto. 
            //Desabilitei enquanto em desenvolvimento as validações de login.
            //     .requestMatchers(
            //         "/",
            //         "/home",
            //         "/home.html",
            //         "/index.html",
            //         "/login",
            //         "/login.html",
            //         "/static/**",
            //         "/css/**",
            //         "/js/**",
            //         "/images/**",
            //         "/swagger-ui/**",
            //         "/v3/api-docs/**"
            //     ).permitAll()
            //     .requestMatchers("/usuario/**").hasRole("GP")
            //     .anyRequest().authenticated()
            // )
            // .addFilterBefore(new JWTAuthenticationFilter(authenticationManager), 
            //     UsernamePasswordAuthenticationFilter.class)
            // .addFilterBefore(new JWTValidateFilter(authenticationManager), 
            //     UsernamePasswordAuthenticationFilter.class)
            .build();
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}