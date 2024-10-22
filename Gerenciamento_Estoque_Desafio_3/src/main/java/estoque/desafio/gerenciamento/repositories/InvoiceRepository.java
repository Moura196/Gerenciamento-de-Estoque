package estoque.desafio.gerenciamento.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import estoque.desafio.gerenciamento.entities.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}
