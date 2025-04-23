package estoque.desafio.gerenciamento.entities;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Compra {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	private LocalDateTime dataCompra;
	private LocalDateTime dataEnvio; // Data que a mercadoria saiu do fornecedor
	private LocalDateTime dataEmissaoInvoice;
	private double valorTotalInvoice;
	private String observacao;
	
	@ManyToOne
	@JoinColumn(name = "projeto", nullable = false)
	@JsonIgnoreProperties("compras")
	private Projeto projeto;
	
	@OneToMany(mappedBy = "compra")
	@JsonIgnoreProperties("compra")
	private Set<Item> itens;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public LocalDateTime getDataCompra() {
		return dataCompra;
	}

	public void setDataCompra(LocalDateTime dataCompra) {
		this.dataCompra = dataCompra;
	}

	public LocalDateTime getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(LocalDateTime dataEnvio) {
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

	public LocalDateTime getDataEmissaoInvoice() {
		return dataEmissaoInvoice;
	}

	public void setDataEmissaoInvoice(LocalDateTime dataEmissaoInvoice) {
		this.dataEmissaoInvoice = dataEmissaoInvoice;
	}

	public double getValorTotalInvoice() {
		return valorTotalInvoice;
	}

	public void setValorTotalInvoice(double valorTotalInvoice) {
		this.valorTotalInvoice = valorTotalInvoice;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

}
