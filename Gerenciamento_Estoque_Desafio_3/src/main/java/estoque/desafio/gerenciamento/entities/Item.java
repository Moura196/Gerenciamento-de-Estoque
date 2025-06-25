package estoque.desafio.gerenciamento.entities;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Item {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	private String patrimonio;
	private String descricao;
	private String tipo;
	private BigDecimal valorUnitario;
	private int quantComprada;
	private BigDecimal valorTotalItem;
	
	@ManyToOne
	@JoinColumn(name = "fornecedor", nullable = false)
	@JsonIgnoreProperties("itens")
	private Fornecedor fornecedor;
	
	@ManyToOne
	@JoinColumn(name = "armazenamento", nullable = false)
	@JsonIgnoreProperties("itens")
	private Armazenamento armazenamento;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compra", nullable = false)
    @JsonIgnoreProperties("itens")
    private Compra compra;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getPatrimonio() {
		return patrimonio;
	}

	public void setPatrimonio(String patrimonio) {
		this.patrimonio = patrimonio;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valoUnitario) {
		this.valorUnitario = valoUnitario;
	}

	public int getQuantComprada() {
		return quantComprada;
	}

	public void setQuantComprada(int quantComprada) {
		this.quantComprada = quantComprada;
	}

	public BigDecimal getValorTotalItem() {
		return this.valorTotalItem;
	}

	public void setValorTotalItem(BigDecimal valorTotalItem) {
		this.valorTotalItem = valorTotalItem;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Armazenamento getArmazenamento() {
		return armazenamento;
	}

	public void setArmazenamento(Armazenamento armazenamento) {
		this.armazenamento = armazenamento;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

}
