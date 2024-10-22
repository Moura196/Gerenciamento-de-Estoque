package estoque.desafio.gerenciamento.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Complemento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	private int idProjeto;
	private String apelidoProjeto;
	private String observacao;
	private boolean userConferencia;
	
	@OneToMany
	@JoinColumn(name = "compra", nullable = false)
	@JsonIgnoreProperties("complemento")
	private Compra compra;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public int getIdProjeto() {
		return idProjeto;
	}

	public void setIdProjeto(int idProjeto) {
		this.idProjeto = idProjeto;
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
