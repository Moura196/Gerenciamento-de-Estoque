package estoque.desafio.gerenciamento.entities.dtos;

public class ItemRelatorioDTO {
	
	private Long codigo;
	private String descricao;
	private String tipo;
	private double valorUnitario;
	private int quantComprada;
	private double valorTotalItem;
	
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
	
	public double getValorUnitario() {
		return valorUnitario;
	}
	
	public void setValorUnitario(double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	
	public int getQuantComprada() {
		return quantComprada;
	}
	
	public void setQuantComprada(int quantComprada) {
		this.quantComprada = quantComprada;
	}
	
	public double getValorTotalItem() {
		return valorTotalItem;
	}
	
	public void setValorTotalItem(double valorTotalItem) {
		this.valorTotalItem = valorTotalItem;
	}
	
}
