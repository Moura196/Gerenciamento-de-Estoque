package estoque.desafio.gerenciamento.services;



@Service
public class InvoiceService {
    //public Invoice criarInvoice(Invoice invoice){
    //      return invoiceRepository.save(invoice);
    //}

    //public Invoice editarInvoice(Invoice invoice){
    //    return invoiceRepository.save(invoice);
    //}

    //public Invoice getInvoice (Long id){
    //    return invoiceRepository.findByInvoice(id).orElse(null); 
    //}

    //public void excluirInvoice(Long id) {
    //    invoiceRepository.deleteById(id);
    //}
//Duvida*****

    public Codigo criarCodigo(Codigo codigo){
        return invoiceRepository.save(codigo);
    }

    public Codigo editarCodigo(Codigo codigo){
        return invoiceRepository.save(codigo);
    }

    public Codigo getCodigo(Codigo codigo){
        return invoiceRepository.findByCodigo(codigo);
    }

    public void excluirCodigo(Codigo codigo){
        return invoiceRepository.deleteByCodigo(codigo);
    }

    public DataEmissao criarDataEmissao(DataEmissao dataEmissao){
        return invoiceRepository.save(dataEmissao);
    }

    public void excluirDataEmissao(DataEmissao dataEmissao){
        return invoiceRepository.deleteByDataEmissao(dataEmissao);
    }

    public DataEmissao getDataEmissao(DataEmissao dataEmissao){
        return invoiceRepository.findByDataEmissao(dataEmissao);
    }


    public ValorTotalInvoice criarValorTotalInvoice(ValorTotalInvoice valorTotalInvoice){
        return invoiceRepository.save(valorTotalInvoice);
    }

    public ValorTotalInvoice editarValorTotalInvoice(ValorTotalInvoice valorTotalInvoice){
        return invoiceRepository.save(valorTotalInvoice);
    }

    public ValorTotalInvoice getValorTotalInvoice(ValorTotalInvoice valorTotalInvoice){
        return invoiceRepository.findByValorTotalInvoice(valorTotalInvoice);
    }

    public void excluirValorTotalInvoice (ValorTotalInvoice valorTotalInvoice){
        return invoiceRepository.deleteByValorTotalInvoice(valorTotalInvoice);
    }
}