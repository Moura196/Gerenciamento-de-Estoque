package estoque.desafio.gerenciamento.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import estoque.desafio.gerenciamento.entities.Projeto;
import estoque.desafio.gerenciamento.entities.Usuario;
import estoque.desafio.gerenciamento.entities.dtos.projeto.CriarProjetoDTO;
import estoque.desafio.gerenciamento.repositories.ProjetoRepository;

@Service
public class ProjetoService {
	
	private ProjetoRepository projetoRepository;
	private UsuarioService usuarioService;

	public ProjetoService(ProjetoRepository projetoRepository) {
		this.projetoRepository = projetoRepository;
	}
	
	public Projeto criarProjeto(CriarProjetoDTO projetoDTO) throws Exception {
		Projeto novoProjeto = new Projeto();
		novoProjeto.setIdProjeto(projetoDTO.getIdProjeto());
		novoProjeto.setApelidoProjeto(projetoDTO.getApelidoProjeto());
		
		Optional<Usuario> vincularUsuario = usuarioService.buscarUsuarioPorCodigo(projetoDTO.getCodigoUsuario());
		if (Optional.ofNullable(vincularUsuario).isPresent()) {
			novoProjeto.setUsuario(vincularUsuario.get());
			projetoRepository.save(novoProjeto);
		} else {
			throw new Exception("Usuário não encontrado");
		}
		return novoProjeto;
	}

}
