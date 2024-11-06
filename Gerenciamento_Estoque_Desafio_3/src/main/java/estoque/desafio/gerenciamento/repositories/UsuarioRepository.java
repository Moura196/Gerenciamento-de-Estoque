package estoque.desafio.gerenciamento.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import estoque.desafio.gerenciamento.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByMatricula(String matricula);

}
