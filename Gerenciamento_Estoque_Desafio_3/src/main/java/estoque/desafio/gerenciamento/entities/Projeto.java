package estoque.desafio.gerenciamento.entities;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Projeto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	private int idProjeto;
	private String apelidoProjeto;
	
	@OneToOne(mappedBy = "projetoGP")
	@JsonIgnoreProperties("projetoGP")
	private Usuario usuarioGP;
	
	@OneToOne(mappedBy = "projetoRT")
	@JsonIgnoreProperties("projetoRT")
	private Usuario usuarioRT;
	
	@OneToMany(mappedBy = "projeto")
	@JsonIgnoreProperties("projeto")
	private Set<Compra> compras;

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

	public Set<Compra> getCompras() {
		return compras;
	}

	public void setCompras(Set<Compra> compras) {
		this.compras = compras;
	}

}
