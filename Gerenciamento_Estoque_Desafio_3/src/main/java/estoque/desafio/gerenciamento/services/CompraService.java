package estoque.desafio.gerenciamento.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import estoque.desafio.gerenciamento.entities.Compra;
import estoque.desafio.gerenciamento.entities.Projeto;
import estoque.desafio.gerenciamento.repositories.CompraRepository;
import estoque.desafio.gerenciamento.repositories.ProjetoRepository;

@Service
public class CompraService {
	
	private CompraRepository compraRepository;
	private ProjetoRepository projetoRepository;
	
	public CompraService(CompraRepository compraRepository, ProjetoRepository projetoRepository) {
		this.compraRepository = compraRepository;
		this.projetoRepository = projetoRepository;
	}
	
	public Compra criarCompra(Compra compraRequest) {
		return projetoRepository.findByIdProjeto(compraRequest.getProjeto().getIdProjeto())
				.map(projeto -> {
					Compra compra = new Compra();
					compra.setDataCompra(compraRequest.getDataCompra());
					compra.setDataEnvio(compraRequest.getDataEnvio());
					compra.setDataEmissaoInvoice(compraRequest.getDataEmissaoInvoice());
					compra.setObservacao(compraRequest.getObservacao());
					compra.setProjeto(projeto);
					return compraRepository.save(compra);
				})
				.orElseThrow(() -> new RuntimeException("Projeto com o ID " + compraRequest.getProjeto().getIdProjeto() + " não encontrado."));
	}
	
	public List<Compra> listarCompras() {
		return compraRepository.findAll();
	}
	
	public Compra editarCompra(Long codigo, Compra compraRequest) {
		Optional<Compra> compraExistenteOptional = compraRepository.findById(codigo);

		return compraExistenteOptional.map(compra -> {
			if (compraRequest.getDataCompra() != null) {
				compra.setDataCompra(compraRequest.getDataCompra());
			}

			if (compraRequest.getDataEnvio() != null) {
				compra.setDataEnvio(compraRequest.getDataEnvio());
			}
			
			if (compraRequest.getDataEmissaoInvoice() != null) {
				compra.setDataEmissaoInvoice(compraRequest.getDataEmissaoInvoice());
			}
			
			if (compraRequest.getValorTotalInvoice() > 0.0) {
				compra.setValorTotalInvoice(compraRequest.getValorTotalInvoice());
			}
			
			if (compraRequest.getObservacao() != null) {
				compra.setObservacao(compraRequest.getObservacao());
			}
			
			if (compraRequest.getProjeto() != null) {
				Integer projetoId = compraRequest.getProjeto().getIdProjeto();
				if (projetoId != null) {
					Projeto novoProjeto = projetoRepository.findByIdProjeto(projetoId)
						.orElseThrow(() -> new RuntimeException("Projeto com o idProjeto " + projetoId + " não encontrado."));
					compra.setProjeto(novoProjeto);
				}
			}
			
			return compraRepository.save(compra);
		}).orElseThrow(() -> new RuntimeException("Compra com o código " + codigo + " não encontrado."));
	}
	
	public void excluirCompra(Long codigo) {
		compraRepository.deleteById(codigo);
	}

}
