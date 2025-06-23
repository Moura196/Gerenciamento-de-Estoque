package estoque.desafio.gerenciamento.entities.dtos;

import java.math.BigDecimal;

public class ItemRelatorioDTO {
	
	private Long codigo;
	private String descricao;
	private String tipo;
	private BigDecimal valorUnitario;
	private int quantComprada;
	private BigDecimal valorTotalItem;
	
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
	
	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}
	
	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	
	public int getQuantComprada() {
		return quantComprada;
	}
	
	public void setQuantComprada(int quantComprada) {
		this.quantComprada = quantComprada;
	}
	
	public BigDecimal getValorTotalItem() {
		return valorTotalItem;
	}
	
	public void setValorTotalItem(BigDecimal valorTotalItem) {
		this.valorTotalItem = valorTotalItem;
	}
	
}
