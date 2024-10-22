package estoque.desafio.gerenciamento.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import estoque.desafio.gerenciamento.entities.Fornecedor;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

}
