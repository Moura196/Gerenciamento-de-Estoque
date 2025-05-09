package estoque.desafio.gerenciamento.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import estoque.desafio.gerenciamento.entities.Armazenamento;

public interface ArmazenamentoRepository extends JpaRepository<Armazenamento, Long> {

	Optional<Armazenamento> findBySalaAndArmario(String sala, String armario);


}
