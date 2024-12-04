package estoque.desafio.gerenciamento.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import estoque.desafio.gerenciamento.entities.Projeto;

public interface ProjetoRepository extends JpaRepository<Projeto, Long>{

}
