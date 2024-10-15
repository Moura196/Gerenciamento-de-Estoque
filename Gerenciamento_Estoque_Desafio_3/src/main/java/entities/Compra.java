package entities;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

public class Compra {
	
	private LocalDateTime dataCompra;
	private LocalDateTime dataEnvio; // Data que a mercadoria saiu do fornecedor
	
	@ManyToOne
	@JoinColumn(name = "usuario", nullable = false)
	@JsonIgnoreProperties("compras")
	private Usuario usuario;
	
	@OneToMany(mappedBy = "compra")
	@JsonIgnoreProperties("compra")
	private List<Item> itens;
	
	@OneToOne(mappedBy = "compra")
	private Complemento complemento;
	
	@OneToOne(mappedBy = "compra")
	private Invoice invoice;

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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Item> getItens() {
		return itens;
	}

	public void setItens(List<Item> itens) {
		this.itens = itens;
	}

	public Complemento getComplemento() {
		return complemento;
	}

	public void setComplemento(Complemento complemento) {
		this.complemento = complemento;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

}
