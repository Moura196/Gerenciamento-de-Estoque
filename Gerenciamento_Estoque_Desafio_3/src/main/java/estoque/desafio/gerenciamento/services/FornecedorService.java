package estoque.desafio.gerenciamento.services;

import estoque.desafio.gerenciamento.entities.Fornecedor;
import estoque.desafio.gerenciamento.entities.dtos.FornecedorDTO;
import estoque.desafio.gerenciamento.repositories.FornecedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FornecedorService {

    FornecedorRepository fornecedorRepository;

    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    public Fornecedor CriarFornecedor(FornecedorDTO fornecedorDTO) throws Exception{
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(fornecedorDTO.getNome());
        fornecedor.setEmail(fornecedorDTO.getEmail());
        fornecedor.setTelefone(fornecedorDTO.getTelefone());
        fornecedor.setCnpj(fornecedorDTO.getCnpj());
        fornecedor.setEndereco(fornecedorDTO.getEndereco());

        fornecedorRepository.save(fornecedor);
        return fornecedor;
    }

    public List<Fornecedor> listarFornecedores(){
        return fornecedorRepository.findAll();
    }

    public Optional<Fornecedor> buscarFornecedorPorId(Long id){
        return fornecedorRepository.findById(id);
    }

    public void excluirFornecedor(Long id){
        fornecedorRepository.deleteById(id);
    }

    public Fornecedor atualizarFornecedor(Long id, FornecedorDTO fornecedorDTO) throws Exception {
        Optional<Fornecedor> fornecedorOptional = fornecedorRepository.findById(id);
        if (fornecedorOptional.isPresent()) {
            Fornecedor fornecedor = fornecedorOptional.get();
            fornecedor.setNome(fornecedorDTO.getNome());
            fornecedor.setEmail(fornecedorDTO.getEmail());
            fornecedor.setTelefone(fornecedorDTO.getTelefone());
            fornecedor.setCnpj(fornecedorDTO.getCnpj());
            fornecedor.setEndereco(fornecedorDTO.getEndereco());
            return fornecedorRepository.save(fornecedor);
        } else {
            throw new Exception("Fornecedor não encontrado");
        }
    }
}
