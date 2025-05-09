package estoque.desafio.gerenciamento.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import estoque.desafio.gerenciamento.entities.Item;

public interface ItemRepository extends JpaRepository<Item, Long>{

	Optional<Item> findByPatrimonio(String patrimonio);

}