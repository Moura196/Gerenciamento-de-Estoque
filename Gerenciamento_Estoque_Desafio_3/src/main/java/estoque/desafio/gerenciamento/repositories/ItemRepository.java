package estoque.desafio.gerenciamento.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import estoque.desafio.gerenciamento.entities.Item;

public interface ItemRepository extends JpaRepository<Item, Long>{

}