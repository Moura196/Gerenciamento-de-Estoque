package estoque.desafio.gerenciamento.entities;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "projeto")
public class Projeto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	private int idProjeto;
	private String apelidoProjeto;
	
	@ManyToOne
	@JoinColumn(name = "usuario", nullable = false)
	@JsonIgnoreProperties("projetos")
	private Usuario usuario;
	
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Set<Compra> getCompras() {
		return compras;
	}

	public void setCompras(Set<Compra> compras) {
		this.compras = compras;
	}

}
