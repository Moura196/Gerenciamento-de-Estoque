package estoque.desafio.gerenciamento.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import estoque.desafio.gerenciamento.entities.Projeto;
import estoque.desafio.gerenciamento.entities.Usuario;
import estoque.desafio.gerenciamento.repositories.ProjetoRepository;
import estoque.desafio.gerenciamento.repositories.UsuarioRepository;

@Service
public class ProjetoService {
	
	private ProjetoRepository projetoRepository;
	private UsuarioRepository usuarioRepository;
	
	public ProjetoService(ProjetoRepository projetoRepository, UsuarioRepository usuarioRepository) {
		this.projetoRepository = projetoRepository;
		this.usuarioRepository = usuarioRepository;
	}
	
	public Projeto criarProjeto(Projeto projetoRequest) {
		return usuarioRepository.findByMatricula(projetoRequest.getUsuario().getMatricula())
				.map(usuario -> {
					Projeto projeto = new Projeto();
					projeto.setIdProjeto(projetoRequest.getIdProjeto());
					projeto.setApelidoProjeto(projetoRequest.getApelidoProjeto());
					projeto.setUsuario(usuario);
					return projetoRepository.save(projeto);
				})
				.orElseThrow(() -> new RuntimeException("Usuário com a matrícula " + projetoRequest.getUsuario().getMatricula() + " não encontrado."));
	}
	
	public List<Projeto> listarProjetos() {
		return projetoRepository.findAll();
	}
	
	public Projeto editarProjeto(int idProjeto, Projeto projetoRequest) {
		Optional<Projeto> projetoExistenteOptional = projetoRepository.findByIdProjeto(idProjeto);

		return projetoExistenteOptional.map(projeto -> {
			if (projetoRequest.getIdProjeto() != 0 && projetoRequest.getIdProjeto() != projeto.getIdProjeto()) {
				if (projetoRepository.findByIdProjeto(projetoRequest.getIdProjeto()).isPresent()) {
					throw new RuntimeException("Já existe um projeto com o idProjeto " + projetoRequest.getIdProjeto() + ".");
				}
				projeto.setIdProjeto(projetoRequest.getIdProjeto());
			}

			if (projetoRequest.getApelidoProjeto() != null) {
				projeto.setApelidoProjeto(projetoRequest.getApelidoProjeto());
			}

			if (projetoRequest.getUsuario() != null && projetoRequest.getUsuario().getMatricula() != null) {
				Usuario novoUsuario = usuarioRepository.findByMatricula(projetoRequest.getUsuario().getMatricula())
						.orElseThrow(() -> new RuntimeException("Usuário com a matrícula " + projetoRequest.getUsuario().getMatricula() + " não encontrado."));
				projeto.setUsuario(novoUsuario);
			}

			return projetoRepository.save(projeto);
		}).orElseThrow(() -> new RuntimeException("Projeto com o idProjeto " + idProjeto + " não encontrado."));
	}
	
	public void excluirProjeto(Long codigo) {
		projetoRepository.deleteById(codigo);
	}
	
}
