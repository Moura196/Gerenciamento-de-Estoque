package estoque.desafio.gerenciamento.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import estoque.desafio.gerenciamento.entities.Compra;
import java.util.Optional;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    
    @EntityGraph(value = "Compra.comItens", type = EntityGraphType.FETCH)
    Optional<Compra> findByCodigo(Long codigo);
}