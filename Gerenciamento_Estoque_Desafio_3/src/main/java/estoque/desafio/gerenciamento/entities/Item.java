package estoque.desafio.gerenciamento.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
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
	private String descricao;
	private String tipo;
	private double valoUnitario;
	private String quantComprada;
	private double valorTotalItem;
	
	@ManyToOne
	@JoinColumn(name = "fornecedor", nullable = false)
	@JsonIgnoreProperties("itens")
	private Fornecedor fornecedor;
	
	@ManyToOne
	@JoinColumn(name = "armazenamento", nullable = false)
	@JsonIgnoreProperties("itens")
	private Armazenamento armazenamento;
	
	@ManyToOne
	@JoinColumn(name = "compra", nullable = false)
	@JsonIgnoreProperties("itens")
	private Compra compra;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
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

	public double getValoUnitario() {
		return valoUnitario;
	}

	public void setValoUnitario(double valoUnitario) {
		this.valoUnitario = valoUnitario;
	}

	public String getQuantComprada() {
		return quantComprada;
	}

	public void setQuantComprada(String quantComprada) {
		this.quantComprada = quantComprada;
	}

	public double getValorTotalItem() {
		return valorTotalItem;
	}

	public void setValorTotalItem(double valorTotalItem) {
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
