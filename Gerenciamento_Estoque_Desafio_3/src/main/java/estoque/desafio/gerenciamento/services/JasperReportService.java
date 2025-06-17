package estoque.desafio.gerenciamento.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import estoque.desafio.gerenciamento.entities.Compra;
import estoque.desafio.gerenciamento.entities.dtos.ItemRelatorioDTO;
import estoque.desafio.gerenciamento.entities.dtos.RelatorioDadosDTO;
import estoque.desafio.gerenciamento.repositories.CompraRepository;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class JasperReportService {

	private static final Logger LOGGER = LoggerFactory.getLogger(JasperReportService.class);
	// private static final String REPORT_PATH = "jasper/relatorios/Gerenciamento_Estoque.jasper";
	private static final String DESTINOPDF = "C:\\jasper-report\\";

	private final CompraRepository compraRepository;

	public JasperReportService(CompraRepository compraRepository) {
		this.compraRepository = compraRepository;
	}

	public byte[] gerarRelatorio(RelatorioDadosDTO dadosRelatorioRequest) {
		Optional<Compra> compraOptional = compraRepository.findById(dadosRelatorioRequest.getCodigoCompra());
		if (compraOptional.isEmpty()) {
			throw new RuntimeException(
					"Compra com o código " + dadosRelatorioRequest.getCodigoCompra() + " não encontrado.");
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

		JRBeanCollectionDataSource itensDataSource = new JRBeanCollectionDataSource(itensList);
		params.put("itensDataSource", itensDataSource);

		try {
			InputStream jasperStream = getClass()
					.getResourceAsStream("/jasper/relatorios/Gerenciamento_Estoque.jasper");
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

			JasperPrint print = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(print, outputStream);

			String folderDiretorio = getDiretorioSave("relatorios-salvos");
			JasperExportManager.exportReportToPdfFile(print, folderDiretorio);
			LOGGER.info("PDF Exportado para: {}", folderDiretorio);

			return outputStream.toByteArray();

		} catch (Exception e) {
			LOGGER.error("Erro ao gerar relatório", e);
			throw new RuntimeException("Falha na geração do relatório: " + e.getMessage());
		}
	}

	private String getDiretorioSave(String nome) {
		this.createDiretorio(DESTINOPDF);
		return DESTINOPDF + nome.concat(".pdf");
	}

	private void createDiretorio(String nome) {
		File dir = new File(nome);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
}