package entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

public class Complemento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idSecProjeto;
	private String apelidoProjeto;
	private String observacao;
	private boolean userConferencia;
	
	@OneToMany
	@JoinColumn(name = "compra", nullable = false)
	@JsonIgnoreProperties("complemento")
	private Compra compra;

	public Long getIdSecProjeto() {
		return idSecProjeto;
	}

	public void setIdSecProjeto(Long idSecProjeto) {
		this.idSecProjeto = idSecProjeto;
	}

	public String getApelidoProjeto() {
		return apelidoProjeto;
	}

	public void setApelidoProjeto(String apelidoProjeto) {
		this.apelidoProjeto = apelidoProjeto;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public boolean isUserConferencia() {
		return userConferencia;
	}

	public void setUserConferencia(boolean userConferencia) {
		this.userConferencia = userConferencia;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}
	
}
