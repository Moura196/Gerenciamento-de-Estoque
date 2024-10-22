package estoque.desafio.gerenciamento.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import estoque.desafio.gerenciamento.entities.Complemento;

public interface ComplementoRepository extends JpaRepository<Complemento, Long> {

}
