package estoque.desafio.gerenciamento.services;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import estoque.desafio.gerenciamento.entities.Item;
import estoque.desafio.gerenciamento.entities.Compra;
import estoque.desafio.gerenciamento.entities.Projeto;
import estoque.desafio.gerenciamento.entities.dtos.CompraInputDTO;
import estoque.desafio.gerenciamento.repositories.CompraRepository;
import estoque.desafio.gerenciamento.repositories.ItemRepository;
import estoque.desafio.gerenciamento.repositories.ProjetoRepository;

@Service
public class CompraService {

	@Autowired
	private CompraRepository compraRepository;

	@Autowired
	private ProjetoRepository projetoRepository;

	@Autowired
	private ItemRepository itemRepository;

	public CompraService(CompraRepository compraRepository,
			ProjetoRepository projetoRepository,
			ItemRepository itemRepository) {
		this.compraRepository = compraRepository;
		this.projetoRepository = projetoRepository;
		this.itemRepository = itemRepository;
	}

	@Transactional
	public Compra criarCompra(CompraInputDTO compraDTO) {
		// Buscar o projeto pelo ID
		Projeto projeto = projetoRepository.findById(compraDTO.getProjeto())
				.orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

		// Criar nova entidade Compra
		Compra compra = new Compra();
		compra.setDataCompra(compraDTO.getDataCompra());
		compra.setDataEnvio(compraDTO.getDataEnvio());
		compra.setDataEmissaoInvoice(compraDTO.getDataEmissaoInvoice());
		compra.setValorTotalInvoice(compraDTO.getValorTotalInvoice());
		compra.setObservacao(compraDTO.getObservacao());
		compra.setProjeto(projeto);

		// Processar itens
		Set<Item> itens = new HashSet<>();
		for (Long itemCodigo : compraDTO.getItensCodigos()) {
			Item item = itemRepository.findById(itemCodigo)
					.orElseThrow(() -> new RuntimeException("Item não encontrado: " + itemCodigo));

			item.setCompra(compra);
			itens.add(item);
		}

		compra.setItens(itens);
		return compraRepository.save(compra);
	}



	public List<Compra> listarCompras() {
		return compraRepository.findAll();
	}

	public Optional<Compra> buscarCompraPorCodigo(Long codigo) {
		return compraRepository.findByCodigo(codigo);
	}

	public Compra editarCompra(Long codigo, Compra compraRequest) {
		return compraRepository.findById(codigo)
				.map(compraExistente -> {
					updateCompraFields(compraExistente, compraRequest);
					return compraRepository.save(compraExistente);
				})
				.orElseThrow(() -> new RuntimeException("Compra com o código " + codigo + " não encontrado."));
	}

	private void updateCompraFields(Compra compraExistente, Compra compraRequest) {
		if (compraRequest.getDataCompra() != null) {
			compraExistente.setDataCompra(compraRequest.getDataCompra());
		}

		if (compraRequest.getDataEnvio() != null) {
			compraExistente.setDataEnvio(compraRequest.getDataEnvio());
		}

		if (compraRequest.getDataEmissaoInvoice() != null) {
			compraExistente.setDataEmissaoInvoice(compraRequest.getDataEmissaoInvoice());
		}

		if (compraRequest.getValorTotalInvoice() != null &&
				compraRequest.getValorTotalInvoice().compareTo(BigDecimal.ZERO) > 0) {
			compraExistente.setValorTotalInvoice(compraRequest.getValorTotalInvoice());
		}

		if (compraRequest.getObservacao() != null) {
			compraExistente.setObservacao(compraRequest.getObservacao());
		}

		updateProjetoIfNeeded(compraExistente, compraRequest);
	}

	private void updateProjetoIfNeeded(Compra compraExistente, Compra compraRequest) {
		if (compraRequest.getProjeto() != null && compraRequest.getProjeto().getIdProjeto() != null) {
			Long projetoId = compraRequest.getProjeto().getIdProjeto();
			Projeto novoProjeto = projetoRepository.findById(projetoId)
					.orElseThrow(() -> new RuntimeException(
							"Projeto com o id " + projetoId + " não encontrado."));
			compraExistente.setProjeto(novoProjeto);
		}
	}

	public void excluirCompra(Long codigo) {
		if (!compraRepository.existsById(codigo)) {
			throw new RuntimeException("Compra com o código " + codigo + " não encontrado.");
		}
		compraRepository.deleteById(codigo);
	}
}
