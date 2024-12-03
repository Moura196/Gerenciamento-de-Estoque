package estoque.desafio.gerenciamento.entities.dtos;

public class FornecedorDTO {

    private Long codigo;
    private String nome;
    private String email;
    private String telefone;
    private String cnpj;
    private String endereco;

    public String getNome() {
        return nome;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public void setNome(String nome) {

        this.nome = nome;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getTelefone() {

        return telefone;
    }

    public void setTelefone(String telefone) {

        this.telefone = telefone;
    }

    public String getCnpj() {

        return cnpj;
    }

    public void setCnpj(String cnpj) {

        this.cnpj = cnpj;
    }

    public String getEndereco() {

        return endereco;
    }

    public void setEndereco(String endereco) {

        this.endereco = endereco;
    }
}