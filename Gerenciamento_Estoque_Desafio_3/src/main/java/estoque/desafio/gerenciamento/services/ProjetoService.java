package estoque.desafio.gerenciamento.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import estoque.desafio.gerenciamento.entities.Projeto;
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
				.orElseThrow(() -> new RuntimeException(
						"Usuário com a matrícula " + projetoRequest.getUsuario().getMatricula() + " não encontrado."));
	}

	public List<Projeto> listarProjetos() {
		return projetoRepository.findAll();
	}

	public Optional<Projeto> findById(Long codigo) {
		return projetoRepository.findById(codigo);
	}

	public Projeto editarProjeto(Long codigo, Projeto projetoAtualizado) {
        return projetoRepository.findById(codigo)
            .map(projetoExistente -> {
                // Atualiza apenas os campos não nulos
                if (projetoAtualizado.getNome() != null) {
                    projetoExistente.setNome(projetoAtualizado.getNome());
                }
                if (projetoAtualizado.getApelidoProjeto() != null) {
                    projetoExistente.setApelidoProjeto(projetoAtualizado.getApelidoProjeto());
                }
                if (projetoAtualizado.getIdProjeto() != null) {
                    projetoExistente.setIdProjeto(projetoAtualizado.getIdProjeto());
                }
                if (projetoAtualizado.getUsuario() != null) {
                    usuarioRepository.findById(projetoAtualizado.getUsuario().getCodigo())
                        .ifPresent(projetoExistente::setUsuario);
                }
                return projetoRepository.save(projetoExistente);
            })
            .orElseThrow(() -> new RuntimeException("Projeto não encontrado com código: " + codigo));
    }

	public void excluirProjeto(Long codigo) {
		projetoRepository.deleteById(codigo);
	}

}
