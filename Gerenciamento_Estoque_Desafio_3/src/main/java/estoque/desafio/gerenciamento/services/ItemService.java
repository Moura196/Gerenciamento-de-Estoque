package estoque.desafio.gerenciamento.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Collections;

import org.springframework.stereotype.Service;

import estoque.desafio.gerenciamento.entities.dtos.ItemDTO;
import estoque.desafio.gerenciamento.entities.Armazenamento;
import estoque.desafio.gerenciamento.entities.Compra;
import estoque.desafio.gerenciamento.entities.Fornecedor;
import estoque.desafio.gerenciamento.entities.Item;
import estoque.desafio.gerenciamento.repositories.ArmazenamentoRepository;
import estoque.desafio.gerenciamento.repositories.CompraRepository;
import estoque.desafio.gerenciamento.repositories.FornecedorRepository;
import estoque.desafio.gerenciamento.repositories.ItemRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {

	private ItemRepository itemRepository;
	private ArmazenamentoRepository armazenamentoRepository;
	private FornecedorRepository fornecedorRepository;
	private CompraRepository compraRepository;

	public ItemService(ItemRepository itemRepository, ArmazenamentoRepository armazenamentoRepository,
			FornecedorRepository fornecedorRepository, CompraRepository compraRepository) {
		this.itemRepository = itemRepository;
		this.armazenamentoRepository = armazenamentoRepository;
		this.fornecedorRepository = fornecedorRepository;
		this.compraRepository = compraRepository;
	}

	@Transactional
	public Item criarItem(ItemDTO itemDTO) {
		// Busca as entidades relacionadas
		Fornecedor fornecedor = fornecedorRepository.findById(itemDTO.getFornecedorCodigo())
				.orElseThrow(() -> new RuntimeException(
						"Fornecedor não encontrado com ID: " + itemDTO.getFornecedorCodigo()));

		Armazenamento armazenamento = armazenamentoRepository.findById(itemDTO.getArmazenamentoCodigo())
				.orElseThrow(() -> new RuntimeException(
						"Armazenamento não encontrado com ID: " + itemDTO.getArmazenamentoCodigo()));

		Compra compra = null;
    		if (itemDTO.getCompraCodigo() != null) {
        		compra = compraRepository.findById(itemDTO.getCompraCodigo())
                	.orElseThrow(() -> new RuntimeException("Compra não encontrada"));
    		}
		// Cria novo item
		Item item = new Item();
		item.setPatrimonio(itemDTO.getPatrimonio());
		item.setDescricao(itemDTO.getDescricao());
		item.setTipo(itemDTO.getTipo());
		item.setValorUnitario(itemDTO.getValorUnitario());
		item.setQuantComprada(itemDTO.getQuantComprada());

		// Calcula valor total se necessário
		if (itemDTO.getValorUnitario() != null && itemDTO.getQuantComprada() != null) {
			item.setValorTotalItem(itemDTO.getValorUnitario()
					.multiply(BigDecimal.valueOf(itemDTO.getQuantComprada())));
		}

		// Associa as entidades
		item.setFornecedor(fornecedor);
		item.setArmazenamento(armazenamento);
		item.setCompra(compra);

		Item itemSalvo = itemRepository.save(item);
		return itemSalvo;
	}

	public List<Item> listarItens() {
		return itemRepository.findAll();
	}

	public Optional<Item> buscarPorId(Long id) {
		Optional<Item> item = itemRepository.findById(id);
		return item;
	}

	public List<Item> buscarItensSemCompra(String termo) {
    	if (termo == null || termo.isEmpty()) {
        	return itemRepository.findByCompraIsNull();
    	} 
        try {
            Long codigo= Long.parseLong(termo); 
            return itemRepository.findByCodigoAndCompraIsNull(codigo)
                    .map(List::of)
                    .orElse(Collections.emptyList());
        } catch (NumberFormatException e) {
            // Se não for ID, busca por nome ou patrimônio
            return itemRepository.findByDescricaoContainingIgnoreCaseAndCompraIsNull(termo);
        }
    }
	

	public List<Item> buscarPorNomeContendo(String nome) {
		return itemRepository.findByDescricaoContainingIgnoreCase(nome);
	}

	public List<Item> listarTodos() {
		return itemRepository.findAll();
	}

	public Optional<Item> buscarItemPorPatromonio(String patrimonio) {
		return itemRepository.findByPatrimonio(patrimonio);
	}

	public Item editarItem(String patrimonio, Item itemRequest) {
		Optional<Item> itemExistenteOptional = itemRepository.findByPatrimonio(patrimonio);

		return itemExistenteOptional.map(item -> {
			if (itemRequest.getPatrimonio() != null) {
				item.setPatrimonio(itemRequest.getPatrimonio());
			}

			if (itemRequest.getDescricao() != null) {
				item.setDescricao(itemRequest.getDescricao());
			}

			if (itemRequest.getTipo() != null) {
				item.setTipo(itemRequest.getTipo());
			}

			if (itemRequest.getValorUnitario().compareTo(BigDecimal.ZERO) > 0) {
				item.setValorUnitario(itemRequest.getValorUnitario());
			}

			if (itemRequest.getQuantComprada() > 0) {
				item.setQuantComprada(itemRequest.getQuantComprada());
			}

			if (itemRequest.getCompra() != null) {
				Long codigoCompra = itemRequest.getCompra().getCodigo();
				if (codigoCompra != null) {
					Compra novaCompra = compraRepository.findById(codigoCompra)
							.orElseThrow(() -> new RuntimeException(
									"Compra com o código " + codigoCompra + " não encontrado."));
					item.setCompra(novaCompra);
				}
			}

			if (itemRequest.getFornecedor() != null) {
				String nomeFornecedor = itemRequest.getFornecedor().getNome();
				if (nomeFornecedor != null) {
					Fornecedor novoFornecedor = fornecedorRepository.findByNome(nomeFornecedor)
							.orElseThrow(() -> new RuntimeException(
									"Fornecedor com o nome " + nomeFornecedor + " não encontrado."));
					item.setFornecedor(novoFornecedor);
				}
			}

			if (itemRequest.getArmazenamento() != null) {
				String salaArmazenamento = itemRequest.getArmazenamento().getSala();
				String armarioArmazenamento = itemRequest.getArmazenamento().getArmario();
				if (salaArmazenamento != null && armarioArmazenamento != null) {
					Armazenamento novoArmazenamento = armazenamentoRepository
							.findBySalaAndArmario(salaArmazenamento, armarioArmazenamento)
							.orElseThrow(() -> new RuntimeException("Armazenamento com a sala " + salaArmazenamento
									+ " e armário " + armarioArmazenamento + " não encontrado."));
					item.setArmazenamento(novoArmazenamento);
				}
			}

			Item itemSalvo = itemRepository.save(item);
			atualizarValorTotalInvoice(itemSalvo.getCompra().getCodigo());
			return itemSalvo;
		}).orElseThrow(() -> new RuntimeException("Item com o patromônio " + patrimonio + " não encontrado."));
	}

	private void atualizarValorTotalInvoice(Long codigo) {
		compraRepository.findById(codigo).ifPresent(compra -> {
			Set<Item> itensDaCompra = compra.getItens();
			BigDecimal valorTotal = BigDecimal.ZERO;

			if (itensDaCompra != null && !itensDaCompra.isEmpty()) {
				valorTotal = itensDaCompra.stream()
						.map(item -> item.getValorTotalItem())
						.filter(Objects::nonNull)
						.reduce(BigDecimal.ZERO, BigDecimal::add);
			}

			compra.setValorTotalInvoice(valorTotal);
			compraRepository.save(compra);
		});
	}

	public void excluirItem(Long codigo) {
		itemRepository.deleteById(codigo);
	}

	public Item atualizarItem(ItemDTO itemDTO) {
		Item item = itemRepository.findById(itemDTO.getCodigo())
				.orElseThrow(() -> new RuntimeException("Item não encontrado"));

		item.setPatrimonio(itemDTO.getPatrimonio());
		item.setDescricao(itemDTO.getDescricao());
		item.setTipo(itemDTO.getTipo());
		item.setValorUnitario(itemDTO.getValorUnitario());
		item.setQuantComprada(itemDTO.getQuantComprada());

		// Atualize as relações conforme necessário
		if (itemDTO.getFornecedorCodigo() != null) {
			Fornecedor fornecedor = fornecedorRepository.findById(itemDTO.getFornecedorCodigo())
					.orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
			item.setFornecedor(fornecedor);
		}
		if (itemDTO.getArmazenamentoCodigo() != null) {
			Armazenamento armazenamento = armazenamentoRepository.findById(itemDTO.getArmazenamentoCodigo())
					.orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
			item.setArmazenamento(armazenamento);
		}
		if (itemDTO.getCompraCodigo() != null) {
			Compra compra = compraRepository.findById(itemDTO.getCompraCodigo())
					.orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
			item.setCompra(compra);
		}
		// Repita para armazenamento e compra

		return itemRepository.save(item);
	}
}
