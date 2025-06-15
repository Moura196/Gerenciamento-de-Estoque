package estoque.desafio.gerenciamento.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Reutilize a mesma chave secreta do JWTAuthenticationFilter
    private static final String SECRET_JWT = "6c3c9f41-9fc3-461c-a60b-a8adfd03f698";
    private static final String PREFIX = "Bearer ";

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        try {
            String atributo = request.getHeader("Authorization");
            
            // Verificação inicial do token
            if(atributo == null || !atributo.startsWith(PREFIX)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            String token = atributo.replace(PREFIX, "");
            
            // Validação do token (reutilizando a lógica do JWTValidateFilter)
            JWT.require(Algorithm.HMAC256(SECRET_JWT))
               .build()
               .verify(token);
            
            // Se chegou aqui, o token é válido
            return ResponseEntity.ok().build();
            
        } catch (JWTVerificationException e) {
            // Token inválido ou expirado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            // Outros erros
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}