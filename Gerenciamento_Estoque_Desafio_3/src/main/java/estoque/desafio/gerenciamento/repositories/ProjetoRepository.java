package estoque.desafio.gerenciamento.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import estoque.desafio.gerenciamento.entities.Projeto;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

	Optional<Projeto> findByIdProjeto(Long idProjeto);
	boolean existsByIdProjeto(Long idProjeto);

}
