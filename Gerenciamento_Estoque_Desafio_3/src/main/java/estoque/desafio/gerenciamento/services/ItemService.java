package estoque.desafio.gerenciamento.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import estoque.desafio.gerenciamento.entities.Armazenamento;
import estoque.desafio.gerenciamento.entities.Compra;
import estoque.desafio.gerenciamento.entities.Fornecedor;
import estoque.desafio.gerenciamento.entities.Item;
import estoque.desafio.gerenciamento.repositories.ArmazenamentoRepository;
import estoque.desafio.gerenciamento.repositories.CompraRepository;
import estoque.desafio.gerenciamento.repositories.FornecedorRepository;
import estoque.desafio.gerenciamento.repositories.ItemRepository;

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

	public Item criarItem(Item itemRequest) {
		return compraRepository.findById(itemRequest.getCompra().getCodigo())
                .map(compra -> {
                    return fornecedorRepository.findByNome(itemRequest.getFornecedor().getNome())
                            .map(fornecedor -> {
                                return armazenamentoRepository.findBySalaAndArmario(itemRequest.getArmazenamento().getSala(), itemRequest.getArmazenamento().getArmario())
                                        .map(armazenamento -> {
                                            Item item = new Item();
                                            item.setPatrimonio(itemRequest.getPatrimonio());
                                            item.setDescricao(itemRequest.getDescricao());
                                            item.setTipo(itemRequest.getTipo());
                                            item.setValorUnitario(itemRequest.getValorUnitario());
                                            item.setQuantComprada(itemRequest.getQuantComprada());
                                            item.setValorTotalItem(itemRequest.getValorUnitario() * itemRequest.getQuantComprada());
                                            item.setCompra(compra);
                                            item.setFornecedor(fornecedor);
                                            item.setArmazenamento(armazenamento);
                                            Item itemSalvo = itemRepository.save(item);
                                            atualizarValorTotalInvoice(compra.getCodigo());
                                            return itemSalvo;
                                        })
                                        .orElseThrow(() -> new RuntimeException("Armazenamento com a sala " + itemRequest.getArmazenamento().getSala() + " e " + itemRequest.getArmazenamento().getArmario() + " não encontrado."));
                            })
                            .orElseThrow(() -> new RuntimeException("Fornecedor com o nome " + itemRequest.getFornecedor().getNome() + " não encontrado."));
                })
                .orElseThrow(() -> new RuntimeException("Compra com o código " + itemRequest.getCompra().getCodigo() + " não encontrada."));
	}
	
	public List<Item> listarItens() {
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
			
			if (itemRequest.getValorUnitario() > 0) {
				item.setValorUnitario(itemRequest.getValorUnitario());
			}
			
			if (itemRequest.getQuantComprada() > 0) {
				item.setQuantComprada(itemRequest.getQuantComprada());
			}
			
			if (itemRequest.getCompra() != null) {
				Long codigoCompra = itemRequest.getCompra().getCodigo();
				if (codigoCompra != null) {
					Compra novaCompra = compraRepository.findById(codigoCompra)
							.orElseThrow(() -> new RuntimeException("Compra com o código " + codigoCompra + " não encontrado."));
					item.setCompra(novaCompra);
				}
			}
			
			if (itemRequest.getFornecedor() != null) {
				String nomeFornecedor = itemRequest.getFornecedor().getNome();
				if (nomeFornecedor != null) {
					Fornecedor novoFornecedor = fornecedorRepository.findByNome(nomeFornecedor)
							.orElseThrow(() -> new RuntimeException("Fornecedor com o nome " + nomeFornecedor + " não encontrado."));
					item.setFornecedor(novoFornecedor);
				}
			}
			
			if (itemRequest.getArmazenamento() != null) {
				String salaArmazenamento = itemRequest.getArmazenamento().getSala();
				String armarioArmazenamento = itemRequest.getArmazenamento().getArmario();
				if (salaArmazenamento != null && armarioArmazenamento != null) {
					Armazenamento novoArmazenamento = armazenamentoRepository.findBySalaAndArmario(salaArmazenamento, armarioArmazenamento) 
							.orElseThrow(() -> new RuntimeException("Armazenamento com a sala " + salaArmazenamento + " e armário " + armarioArmazenamento + " não encontrado."));
					item.setArmazenamento(novoArmazenamento);
				}
			}
			
			Item itemSalvo = itemRepository.save(item);
			atualizarValorTotalInvoice(itemSalvo.getCompra().getCodigo());
			return itemSalvo;
		}).orElseThrow(() -> new RuntimeException("Item com o patromônio " + patrimonio + " não encontrado."));
	}

	private void atualizarValorTotalInvoice(Long codigo) {
		Optional<Compra> compraExistenteOptional = compraRepository.findById(codigo);
		
		compraExistenteOptional.ifPresent(compra -> {
			Set<Item> itensDaCompra = compra.getItens();
			double valorTotal = 0.0;
			if (itensDaCompra != null) {
				valorTotal = itensDaCompra.stream()
						.mapToDouble(item -> item.getValorTotalItem()).sum();
			}
			compra.setValorTotalInvoice(valorTotal);
			compraRepository.save(compra);
		});
	}

	public void excluirItem(Long codigo) {
		itemRepository.deleteById(codigo);
	}

}
