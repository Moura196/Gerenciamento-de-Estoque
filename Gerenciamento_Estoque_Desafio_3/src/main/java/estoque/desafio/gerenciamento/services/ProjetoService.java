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
		return usuarioRepository.findById(projetoRequest.getUsuario().getCodigo())
				.map(usuario -> {
					Projeto projeto = new Projeto();
					projeto.setIdProjeto(projetoRequest.getIdProjeto());
					projeto.setApelidoProjeto(projetoRequest.getApelidoProjeto());
					projeto.setUsuario(usuario);
					return projetoRepository.save(projeto);
				})
				.orElseThrow(() -> new RuntimeException("Usuário com o código " + projetoRequest.getUsuario().getCodigo() + " não encontrado."));
	}
	
	public List<Projeto> listarProjetos() {
		return projetoRepository.findAll();
	}
	
	public void excluirProjeto(Long codigo) {
		projetoRepository.deleteById(codigo);
	}
	
	public Projeto editarProjeto(int idProjeto, Projeto projetoRequest) {
		Optional<Projeto> projetoExistenteOptional = projetoRepository.findByIdProjeto(idProjeto);

		return projetoExistenteOptional.map(projeto -> {
			if (projetoRequest.getIdProjeto() != 0 && projetoRequest.getIdProjeto() != projeto.getIdProjeto()) {
				if (projetoRepository.findByIdProjeto(projetoRequest.getIdProjeto()).isPresent()) {
					throw new RuntimeException("Já existe um projeto com o ID " + projetoRequest.getIdProjeto() + ".");
				}
				projeto.setIdProjeto(projetoRequest.getIdProjeto());
			}

			if (projetoRequest.getApelidoProjeto() != null) {
				projeto.setApelidoProjeto(projetoRequest.getApelidoProjeto());
			}

			if (projetoRequest.getUsuario() != null && projetoRequest.getUsuario().getCodigo() != null) {
				Usuario novoUsuario = usuarioRepository.findById(projetoRequest.getUsuario().getCodigo())
						.orElseThrow(() -> new RuntimeException("Usuário com o código " + projetoRequest.getUsuario().getCodigo() + " não encontrado."));
				projeto.setUsuario(novoUsuario);
			}

			return projetoRepository.save(projeto);
		}).orElseThrow(() -> new RuntimeException("Projeto com o ID " + idProjeto + " não encontrado."));
	}
	
}
