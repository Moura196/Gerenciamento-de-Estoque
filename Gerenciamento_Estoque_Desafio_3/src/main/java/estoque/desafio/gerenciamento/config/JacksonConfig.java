package estoque.desafio.gerenciamento.config;

import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public Hibernate6Module hibernateModule() {
        return new Hibernate6Module()
            .configure(Hibernate6Module.Feature.FORCE_LAZY_LOADING, false)
            .disable(Hibernate6Module.Feature.USE_TRANSIENT_ANNOTATION);
    }
}