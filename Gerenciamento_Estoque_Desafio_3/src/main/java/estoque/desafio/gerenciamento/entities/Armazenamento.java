package estoque.desafio.gerenciamento.entities;

import java.util.Set;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Armazenamento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	private String sala;
	private String armario;
	
	@OneToMany(mappedBy = "armazenamento")
	@JsonIgnoreProperties("armazenamento")
	private Set<Item> itens;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getSala() {
		return sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public String getArmario() {
		return armario;
	}

	public void setArmario(String armario) {
		this.armario = armario;
	}

	public Set<Item> getItens() {
		return itens;
	}

	public void setItens(Set<Item> itens) {
		this.itens = itens;
	}
	@Transient
	@JsonInclude
	private String nome; // Não persiste no banco
    public String getNome() {
        return "Sala " + this.sala + " - Armário " + this.armario;
    }

}
