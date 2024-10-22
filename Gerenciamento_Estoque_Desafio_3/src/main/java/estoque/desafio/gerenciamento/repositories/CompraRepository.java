package estoque.desafio.gerenciamento.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import estoque.desafio.gerenciamento.entities.Compra;

public interface CompraRepository extends JpaRepository<Compra, Long> {

}
