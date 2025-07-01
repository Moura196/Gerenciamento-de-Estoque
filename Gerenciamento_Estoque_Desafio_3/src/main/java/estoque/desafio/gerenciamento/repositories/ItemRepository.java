package estoque.desafio.gerenciamento.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import estoque.desafio.gerenciamento.entities.Item;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    //Método para buscar itens por código da compra
    List<Item> findByCompraCodigo(Long compraCodigo);
    //Método para buscar item por patrimônio
    Optional<Item> findByPatrimonio(String patrimonio);
    //Busca por descrição
    List<Item> findByDescricaoContainingIgnoreCase(String descricao);
    //Busca itens não relacionados a uma compra
    List<Item> findByCompraIsNull();

    Optional<Item> findByCodigoAndCompraIsNull(Long codigo);

    List<Item> findByDescricaoContainingIgnoreCaseAndCompraIsNull(String descricao);
}   