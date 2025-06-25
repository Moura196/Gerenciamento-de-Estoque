package estoque.desafio.gerenciamento.entities.dtos;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ProjetoRequest {
    @NotNull
    private Long idProjeto;

    @NotBlank
    private String nome;
    
    private String apelidoProjeto;

    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;

    private List<Long> comprasIds;

    public Long getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(Long idProjeto) {
        this.idProjeto = idProjeto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelidoProjeto() {
        return apelidoProjeto;
    }

    public void setApelidoProjeto(String apelidoProjeto) {
        this.apelidoProjeto = apelidoProjeto;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<Long> getComprasIds() {
        return comprasIds;
    }

    public void setComprasIds(List<Long> comprasIds) {
        this.comprasIds = comprasIds;
    }
}