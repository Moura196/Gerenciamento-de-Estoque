package estoque.desafio.gerenciamento.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Invoice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	private LocalDateTime dataEmissao;
	private double valorTotalInvoice;
	
	@OneToOne
	@JoinColumn(name = "compra", nullable = false)
	@JsonIgnoreProperties("invoice")
	private Compra compra;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public LocalDateTime getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(LocalDateTime dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public double getValorTotalInvoice() {
		return valorTotalInvoice;
	}

	public void setValorTotalInvoice(double valorTotalInvoice) {
		this.valorTotalInvoice = valorTotalInvoice;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

}
