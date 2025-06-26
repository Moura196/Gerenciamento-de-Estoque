package estoque.desafio.gerenciamento.entities.dtos;

import java.math.BigDecimal;

public class ItemRelatorioDTO {
	
	private Long codigo;
	private String descricao;
	private String tipo;
	private BigDecimal valorUnitario;
	private int quantComprada;
	private BigDecimal valorTotalItem;
	private long compra;
	private long id_armazenamento;
	private String armario;
	private String sala;
	private long id_fornecedor;
	private String cnpj;
	private String email;
	private String endereco;
	private String nome;
	private String telefone;
	
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

	public long getCompra() {
		return compra;
	}

	public void setCompra(long compra) {
		this.compra = compra;
	}

	public long getId_armazenamento() {
		return id_armazenamento;
	}

	public void setId_armazenamento(long id_armazenamento) {
		this.id_armazenamento = id_armazenamento;
	}

	public String getArmario() {
		return armario;
	}

	public void setArmario(String armario) {
		this.armario = armario;
	}

	public String getSala() {
		return sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public long getId_fornecedor() {
		return id_fornecedor;
	}

	public void setId_fornecedor(long id_fornecedor) {
		this.id_fornecedor = id_fornecedor;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
}
