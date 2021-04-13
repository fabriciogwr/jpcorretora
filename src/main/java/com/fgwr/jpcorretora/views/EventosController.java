package com.fgwr.jpcorretora.views;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fgwr.jpcorretora.FrontApp;
import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.domain.Duplicata;
import com.fgwr.jpcorretora.domain.Recibo;
import com.fgwr.jpcorretora.enums.EstadoPagamento;
import com.fgwr.jpcorretora.repositories.ContratoRepository;
import com.fgwr.jpcorretora.repositories.DuplicataRepository;
import com.fgwr.jpcorretora.repositories.ReciboRepository;
import com.fgwr.jpcorretora.utils.FileUtils;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.StageStyle;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class EventosController {

	@FXML
	private TableView<Duplicata> pagamentosTable;
	@FXML
	private TableColumn<Duplicata, String> clientePColumn;
	@FXML
	private TableColumn<Duplicata, String> contratoPColumn;
	@FXML
	private TableColumn<Duplicata, String> parcelaPColumn;
	@FXML
	private TableColumn<Duplicata, String> vencimentoPColumn;
	@FXML
	private TableColumn<Duplicata, String> valorPColumn;
	@FXML
	private TableView<Contrato> contratosTable;
	@FXML
	private TableColumn<Contrato, String> numeroCColumn;
	@FXML
	private TableColumn<Contrato, String> clienteCColumn;
	@FXML
	private TableColumn<Contrato, String> vigenciaCColumn;
	@FXML
	private TableColumn<Contrato, String> imovelCColumn;
	@FXML
	private TableColumn<Contrato, String> fimCColumn;

	FrontApp frontApp = new FrontApp();
	ApplicationContext context = SpringContext.getAppContext();

	List<Contrato> filterContratos = new ArrayList<>();
	List<Duplicata> filterDups = new ArrayList<>();

	private List<Duplicata> getDuplicataData() {
		DuplicataRepository dupRepo = (DuplicataRepository) context.getBean("duplicataRepository");

		List<Duplicata> dups = dupRepo.findAll();
		return dups;
	}

	private List<Contrato> getContratoData() {
		ContratoRepository contRepo = (ContratoRepository) context.getBean("contratoRepository");
		List<Contrato> contratos = contRepo.findAll();

		return contratos;
	}

	public void initialize() {
		Calendar cal = new GregorianCalendar();
		cal = Calendar.getInstance();
		Date now = cal.getTime();

		cal.add(Calendar.DAY_OF_MONTH, 7);
		Date vencimentoEnd = cal.getTime();

		List<Duplicata> dups = getDuplicataData();
		if (!dups.isEmpty()) {
			for (Duplicata duplicata : dups) {
				if (duplicata.getDataVencimento().before(now) && duplicata.getEstado() == EstadoPagamento.PENDENTE) {
					filterDups.add(duplicata);
				} else if (duplicata.getDataVencimento().before(vencimentoEnd) && duplicata.getEstado() == EstadoPagamento.PENDENTE) {
					filterDups.add(duplicata);
				}

			}
		}

		List<Contrato> contratos = getContratoData();

		LocalDate ldNow = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		if (!contratos.isEmpty()) {
		for (Contrato contrato : contratos) {
			cal.setTime(contrato.getData());
			cal.add(Calendar.MONTH, contrato.getQtdParcelas() - 1);
			LocalDate ldEnd = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			Integer count = Math.toIntExact(ChronoUnit.DAYS.between(ldNow, ldEnd));

			if (count < 61) {
				filterContratos.add(contrato);

			}
		}
		}

		pagamentosTable.setItems(FXCollections.observableArrayList(filterDups));

		clientePColumn.setCellValueFactory(cellData -> cellData.getValue().cliente());
		parcelaPColumn.setCellValueFactory(cellData -> cellData.getValue().parcela());
		vencimentoPColumn.setCellValueFactory(cellData -> cellData.getValue().vencimento());
		valorPColumn.setCellValueFactory(cellData -> cellData.getValue().valor());
		contratoPColumn.setCellValueFactory(cellData -> cellData.getValue().contrato());

		contratosTable.setItems(FXCollections.observableArrayList(filterContratos));
		clienteCColumn.setCellValueFactory(cellData -> cellData.getValue().cliente());
		numeroCColumn.setCellValueFactory(cellData -> cellData.getValue().num());
		vigenciaCColumn.setCellValueFactory(cellData -> cellData.getValue().vigencia());
		imovelCColumn.setCellValueFactory(cellData -> cellData.getValue().imovel());
		fimCColumn.setCellValueFactory(cellData -> cellData.getValue().dataFim());
		contratosTable.getSortOrder().add(fimCColumn);

	}

	public void setMainApp(FrontApp frontApp) {
		this.frontApp = frontApp;
	}
	
	@Transactional
	@FXML
	private void handlePagamento() throws IOException {
		Duplicata selectedDuplicata = pagamentosTable.getSelectionModel().getSelectedItem();
		ReciboRepository recRepo = (ReciboRepository) context.getBean("reciboRepository");

		if (selectedDuplicata != null && selectedDuplicata.getEstado() == EstadoPagamento.PENDENTE) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initStyle(StageStyle.UNIFIED);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(FileUtils.fileToString(new File("css/alerts.css")));
			alert.setTitle("Confirmar pagamento");
			alert.setHeaderText("Confirmar pagamento da mensalidade selecionada?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				
				boolean okClicked = frontApp.showConfiguraPagamento(selectedDuplicata);
				if (okClicked) {
					selectedDuplicata.setEstado(EstadoPagamento.QUITADO);
					
					Recibo rec = recRepo.findByDuplicata(selectedDuplicata);
					selectedDuplicata.setRecibo(rec);
					Alert alert2 = new Alert(AlertType.CONFIRMATION);
					alert2.initStyle(StageStyle.UNIFIED);
					DialogPane dialogPane2 = alert2.getDialogPane();
					dialogPane2.getStylesheets().add(FileUtils.fileToString(new File("css/alerts.css")));
					alert2.setTitle("Recibo");
					alert2.setHeaderText("Visualizar o recibo do pagamento?");
					Optional<ButtonType> result2 = alert2.showAndWait();
					if (result2.get() == ButtonType.OK) {
						visualizaRecibo(selectedDuplicata.getRecibo());

					} else {
						alert.close();
					}

				}

			}

		} else {
			Alert alert3 = new Alert(AlertType.WARNING);
			alert3.initStyle(StageStyle.UNIFIED);
			DialogPane dialogPane3 = alert3.getDialogPane();
			dialogPane3.getStylesheets().add(FileUtils.fileToString(new File("css/alerts.css")));
			alert3.setTitle("Mensalidade Já Paga");
			alert3.setHeaderText("A mensalidade selecionada já está paga.");
			alert3.showAndWait();
		}
	}
	
	public void visualizaRecibo(Recibo rec) throws IOException {
		File file = new File(FileUtils.pathRecibos(rec));
		Desktop desktop = Desktop.getDesktop();
		desktop.open(file);
	}

}
