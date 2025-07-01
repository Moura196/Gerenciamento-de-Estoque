package estoque.desafio.gerenciamento.services;

import estoque.desafio.gerenciamento.entities.Armazenamento;
import estoque.desafio.gerenciamento.repositories.ArmazenamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Service
public class ArmazenamentoService {

	private ArmazenamentoRepository armazenamentoRepository;

	public ArmazenamentoService(ArmazenamentoRepository armazenamentoRepository) {
		this.armazenamentoRepository = armazenamentoRepository;
	}

	public Armazenamento criarArmazenamento(Armazenamento armazenamento) {
		try {
        if (armazenamentoRepository.existsBySalaAndArmario(
            armazenamento.getSala(), 
            armazenamento.getArmario())) {
            throw new RuntimeException("Já existe um armazenamento com esta sala e armário");
        }
        
        return armazenamentoRepository.save(armazenamento);
    } catch (Exception e) {
        throw e;
    }
	}

	public Optional<Armazenamento> buscarArmazenamentoPorId(Long codigo) {
		return armazenamentoRepository.findById(codigo);
	}

	public List<Armazenamento> listarArmazenamentos() {
		return armazenamentoRepository.findAll();
	}

	public Optional<Armazenamento> buscarArmazenamentoPorSalaAndArmario(String sala, String armario) {
		return armazenamentoRepository.findBySalaAndArmario(sala, armario);
	}

	public Armazenamento editarArmazenamento(Long codigo, Armazenamento armazenamentoRequest) {
		Optional<Armazenamento> armazenamentoExistenteOptional = armazenamentoRepository.findById(codigo);

		return armazenamentoExistenteOptional.map(armazenamento -> {
			if (armazenamentoRequest.getSala() != null) {
				armazenamento.setSala(armazenamentoRequest.getSala());
			}

			if (armazenamentoRequest.getArmario() != null) {
				armazenamento.setArmario(armazenamentoRequest.getArmario());
			}

			return armazenamentoRepository.save(armazenamento);
		}).orElseThrow(() -> new RuntimeException("Armazenamento com o código " + codigo + " não encontrado."));
	}

	public void excluirArmazenamento(Long codigo) {
		armazenamentoRepository.deleteById(codigo);
	}

}
