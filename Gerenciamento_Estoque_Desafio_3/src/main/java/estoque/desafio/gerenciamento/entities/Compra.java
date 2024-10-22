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
	
	@ManyToOne
	@JoinColumn(name = "Gerente de Projeto", nullable = false)
	@JsonIgnoreProperties("compras")
	private Usuario usuarioGP;
	
	@ManyToOne
	@JoinColumn(name = "Responsável Técnico", nullable = false)
	@JsonIgnoreProperties("compras")
	private Usuario usuarioRT;
	
	@OneToMany(mappedBy = "compra")
	@JsonIgnoreProperties("compra")
	private Set<Item> itens;
	
	@OneToOne(mappedBy = "compra")
	@JsonIgnoreProperties("compra")
	private Complemento complemento;
	
	@OneToOne(mappedBy = "compra")
	@JsonIgnoreProperties("compra")
	private Invoice invoice;

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

	public Usuario getUsuarioGP() {
		return usuarioGP;
	}

	public void setUsuarioGP(Usuario usuarioGP) {
		this.usuarioGP = usuarioGP;
	}

	public Usuario getUsuarioRT() {
		return usuarioRT;
	}

	public void setUsuarioRT(Usuario usuarioRT) {
		this.usuarioRT = usuarioRT;
	}

	public Set<Item> getItens() {
		return itens;
	}

	public void setItens(Set<Item> itens) {
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
