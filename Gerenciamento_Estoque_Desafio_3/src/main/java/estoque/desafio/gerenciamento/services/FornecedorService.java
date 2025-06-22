package estoque.desafio.gerenciamento.services;

import estoque.desafio.gerenciamento.entities.Fornecedor;
import estoque.desafio.gerenciamento.repositories.FornecedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FornecedorService {

    private FornecedorRepository fornecedorRepository;

    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    public Fornecedor criarFornecedor(Fornecedor fornecedorRequest){
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(fornecedorRequest.getNome());
        fornecedor.setEmail(fornecedorRequest.getEmail());
        fornecedor.setTelefone(fornecedorRequest.getTelefone());
        fornecedor.setCnpj(fornecedorRequest.getCnpj());
        fornecedor.setEndereco(fornecedorRequest.getEndereco());

        fornecedorRepository.save(fornecedor);
        return fornecedor;
    }

    public List<Fornecedor> listarFornecedores(){
        return fornecedorRepository.findAll();
    }

    public Optional<Fornecedor> buscarFornecedorPorNome(String nome){
        return fornecedorRepository.findByNome(nome);
    }

    public Optional<Fornecedor> buscarFornecedorPorId(Long id) {
        return fornecedorRepository.findById(id);
    }

    public Fornecedor editarFornecedor(String nome, Fornecedor fornecedorRequest) {
        Optional<Fornecedor> fornecedorExistenteOptional = fornecedorRepository.findByNome(nome);
        
        return fornecedorExistenteOptional.map(fornecedor -> {
        	if (fornecedorRequest.getNome() != null) {
        		fornecedor.setNome(fornecedorRequest.getNome());
        	}
        	
        	if (fornecedorRequest.getEmail() != null) {
        		fornecedor.setEmail(fornecedorRequest.getEmail());
        	}
        	
        	if (fornecedorRequest.getTelefone() != null) {
        		fornecedor.setTelefone(fornecedorRequest.getTelefone());
        	}
        	
        	if (fornecedorRequest.getCnpj() != null) {
        		fornecedor.setCnpj(fornecedorRequest.getCnpj());
        	}
        	
        	if (fornecedorRequest.getEndereco() != null) {
        		fornecedor.setEndereco(fornecedorRequest.getEndereco());
        	}
        	
        	return fornecedorRepository.save(fornecedor);
        }).orElseThrow(() -> new RuntimeException("Fornecedor com o nome " + nome + " não encontrado."));
    }

    public void excluirFornecedor(Long codigo){
    	fornecedorRepository.deleteById(codigo);
    }

}
