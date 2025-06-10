package estoque.desafio.gerenciamento.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import estoque.desafio.gerenciamento.entities.Compra;
import estoque.desafio.gerenciamento.entities.dtos.ItemRelatorioDTO;
import estoque.desafio.gerenciamento.entities.dtos.RelatorioDadosDTO;
import estoque.desafio.gerenciamento.repositories.CompraRepository;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
public class JasperReportService {
	
	public static final String RELATORIOS = "classpath:jasper/relatorios/";
			//"C:/Users/Cliente/Documents/ws_eclipse/Gerenciamento-de-Estoque/Gerenciamento_Estoque_Desafio_3/src/main/resources/jasper/relatorios/";
	public static final Logger LOGGER = LoggerFactory.getLogger(JasperReportService.class);
	public static final String ARQUIVOJRXML = "Gerenciamento_Estoque.jrxml";
	public static final String DESTINOPDF = "C:\\jasper-report\\";
	
	private CompraRepository compraRepository;
	
	public JasperReportService(CompraRepository compraRepository) {
		this.compraRepository = compraRepository;
	}

	public void gerarRelatorio(RelatorioDadosDTO dadosRelatorioRequest) throws FileNotFoundException {
		Optional<Compra> compraOptional = compraRepository.findById(dadosRelatorioRequest.getCodigoCompra());
		if (compraOptional.isEmpty()) {
			throw new RuntimeException("Compra com o código " + dadosRelatorioRequest.getCodigoCompra() + " não encontrado.");
		}
		
		Compra compraSolicitada = compraOptional.get();
		
		Map<String, Object> params = new HashMap<>();
		params.put("nomeGestor", compraSolicitada.getProjeto().getUsuario().getNome());
		params.put("idProjeto", compraSolicitada.getProjeto().getIdProjeto());
		params.put("codigoCompra", compraSolicitada.getCodigo());
		params.put("dataCompra", compraSolicitada.getDataCompra());
		params.put("dataEnvio", compraSolicitada.getDataEnvio());
		params.put("dataEmissaoInvoice", compraSolicitada.getDataEmissaoInvoice());
		params.put("valorTotalInvoice", compraSolicitada.getValorTotalInvoice());
		params.put("observacao", compraSolicitada.getObservacao());
		
		List<ItemRelatorioDTO> itensList = compraSolicitada.getItens().stream().map(item -> {
			ItemRelatorioDTO itemDTO = new ItemRelatorioDTO();
			itemDTO.setCodigo(item.getCodigo());
			itemDTO.setDescricao(item.getDescricao());
			itemDTO.setTipo(item.getTipo());
			itemDTO.setValorUnitario(item.getValorUnitario());
			itemDTO.setQuantComprada(item.getQuantComprada());
			itemDTO.setValorTotalItem(item.getValorTotalItem());
			return itemDTO;
		}).collect(Collectors.toList());
		
		params.put("itens", itensList);
		
		InputStream jasperStream = getClass().getResourceAsStream("/jasper/relatorios/Gerenciamento_Estoque.jasper");
		if (jasperStream == null) {
		    throw new FileNotFoundException("Arquivo .jasper não encontrado!");
		}
		
		String pathAbsoluto = getAbsolutePath();
		System.out.println("Aqui está o pathAbsoluto" + pathAbsoluto);
		
		try {
			String folderDiretorio = getDiretorioSave("relatorios-salvos");
			JasperReport report = JasperCompileManager.compileReport(pathAbsoluto);
			LOGGER.info("report compilado: {} ", pathAbsoluto);
			JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
			LOGGER.info("jassper print");
			JasperExportManager.exportReportToPdfFile(print, folderDiretorio);
			LOGGER.info("PDF Exportado para: {}", folderDiretorio);
		} catch (JRException e) {
			throw new RuntimeException(e);
		}
		
	}

	private String getDiretorioSave(String nome) {
		this.createDiretorio(DESTINOPDF);
		System.out.println("Aqui está o DESTINOPDF" + DESTINOPDF);
		return DESTINOPDF + nome.concat(".pdf");
	}

	private void createDiretorio(String nome) {
		File dir = new File(nome);
		if (!dir.exists()) {
			dir.mkdir();
		}
	}

	private String getAbsolutePath() throws FileNotFoundException {
		return ResourceUtils.getFile(RELATORIOS+ARQUIVOJRXML).getAbsolutePath();
	}

}
