package estoque.desafio.gerenciamento.services;

import estoque.desafio.gerenciamento.entities.Armazenamento;
import estoque.desafio.gerenciamento.repositories.ArmazenamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArmazenamentoService {

	private ArmazenamentoRepository armazenamentoRepository;

	public ArmazenamentoService(ArmazenamentoRepository armazenamentoRepository) {
		this.armazenamentoRepository = armazenamentoRepository;
	}
	
	public Armazenamento criarArmazenamento(Armazenamento armazenamentoRequest) {
		Armazenamento armazenamento = new Armazenamento();
		armazenamento.setSala(armazenamentoRequest.getSala());
		armazenamento.setArmario(armazenamentoRequest.getArmario());
		
		armazenamentoRepository.save(armazenamento);
		return armazenamento;
	}
    
	public List<Armazenamento> listarArmazenamentos() {
		return armazenamentoRepository.findAll();
	}
	
	public Optional<Armazenamento> buscarArmazenamentoPorSalaAndArmario(String sala, String armario) {
		return armazenamentoRepository.findBySalaAndArmario(sala, armario);
	}
	
	public void excluirArmazenamento(Long codigo) {
		armazenamentoRepository.deleteById(codigo);
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
}

