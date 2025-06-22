package estoque.desafio.gerenciamento.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;

@Entity
@NamedEntityGraph(
    name = "Compra.comItens",
    attributeNodes = @NamedAttributeNode("itens")
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Compra {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	@Column(name = "data_compra", nullable = false)
    private LocalDate dataCompra;
	@Column(name = "data_envio")
    private LocalDate dataEnvio; // Data que a mercadoria saiu do fornecedor
	@Column(name = "data_emissao_invoice")
    private LocalDate dataEmissaoInvoice;
    @Column(name = "valor_total_invoice", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorTotalInvoice;
	private String observacao;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projeto", nullable = false)
    private Projeto projeto;
	
	@OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Item> itens = new HashSet<>();
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public LocalDate getDataCompra() {
		return dataCompra;
	}

	public void setDataCompra(LocalDate dataCompra) {
		this.dataCompra = dataCompra;
	}

	public LocalDate getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(LocalDate dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public Set<Item> getItens() {
        return itens;
    }

    public void setItens(Set<Item> itens) {
        this.itens = itens;
    }

    public void adicionarItem(Item item) {
        itens.add(item);
        item.setCompra(this);
    }

	public LocalDate getDataEmissaoInvoice() {
		return dataEmissaoInvoice;
	}

	public void setDataEmissaoInvoice(LocalDate dataEmissaoInvoice) {
		this.dataEmissaoInvoice = dataEmissaoInvoice;
	}

	public BigDecimal getValorTotalInvoice() {
		return valorTotalInvoice;
	}

	public void setValorTotalInvoice(BigDecimal valorTotalInvoice) {
		this.valorTotalInvoice = valorTotalInvoice;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

}
