package estoque.desafio.gerenciamento.services;

import estoque.desafio.gerenciamento.entities.Armazenamento;
import estoque.desafio.gerenciamento.repositories.ArmazenamentoRepository;
import estoque.desafio.gerenciamento.entities.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
public class ArmazenamentoService {


    private ArmazenamentoRepository armazenamentoRepository;
    private ItemRepository itemRepository;

    public List<Armazenamento> findAll(){
        return armazenamentoRepository.findAll();
    }

    public Optional<Armazenamento> findByID(Long id){
        return armazenamentoRepository.findById(Long id);
    }

    public Armazenamento save(Armazenamento armazenamento){
        return armazenamentoRepository.save(armazenamento);
    }

    public void deleteByID(Long id){
        armazenamentoRepository.deleteById(Long id);
    }
    @Transactional
    public boolean removeItemDeArmazenamento(Long armazenamentoId, Long itemId){
        Optional<Armazenamento> armazenamentoOpt = armazenamentoRepository.findById(ArmazenamentoId);
        Optional<Item> itemOpt = itemRepository.findById(itemId);

        if (armazenamentoOpt.isPresent() && itemOpt.isPresent()){
            Armazenamento armazenamento = armazenamentoOpt.get();
            Item item = itemOpt.get();

            if (armazenamento.getItens().contains(item)){
                armazenamento.getItens().remove(item);
                item.setArmazenamento(null);
                armazenamentoRepository.save(armazenamento);
                itemRepository.delete(item);
                return true;
            }
        }
        return false;
    }


}
