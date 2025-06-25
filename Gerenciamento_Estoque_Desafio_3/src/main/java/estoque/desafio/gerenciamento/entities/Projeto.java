package estoque.desafio.gerenciamento.entities;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;

@Entity
@Table(name = "projeto")
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo;

    @Column(name = "id_projeto", unique = true)
    private Long idProjeto;

    @Column(name = "apelido_projeto")
    private String apelidoProjeto;

    @Column(name = "nome")
    private String nome;

    @ManyToOne
    @JoinColumn(name = "usuario", referencedColumnName = "codigo", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("projeto")
    private Set<Compra> compras = new HashSet<>();

    public Projeto() {
    }

    // Getters e Setters
    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Long getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(Long idProjeto) {
        this.idProjeto = idProjeto;
    }

    public String getApelidoProjeto() {
        return apelidoProjeto;
    }

    public void setApelidoProjeto(String apelidoProjeto) {
        this.apelidoProjeto = apelidoProjeto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void adicionarCompra(Compra compra) {
        compras.add(compra);
        compra.setProjeto(this);
    }

    public void removerCompra(Compra compra) {
        compras.remove(compra);
        compra.setProjeto(null);
    }
}
