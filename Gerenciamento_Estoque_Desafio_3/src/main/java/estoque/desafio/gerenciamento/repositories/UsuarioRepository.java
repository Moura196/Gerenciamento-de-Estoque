package estoque.desafio.gerenciamento.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import estoque.desafio.gerenciamento.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
