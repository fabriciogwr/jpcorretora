package com.fgwr.jpcorretora.views;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.FrontApp;
import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.domain.Duplicata;
import com.fgwr.jpcorretora.domain.Imovel;
import com.fgwr.jpcorretora.enums.EstadoPagamento;
import com.fgwr.jpcorretora.repositories.ClienteRepository;
import com.fgwr.jpcorretora.repositories.ContratoRepository;
import com.fgwr.jpcorretora.repositories.DuplicataRepository;
import com.fgwr.jpcorretora.repositories.ImovelRepository;
import com.fgwr.jpcorretora.services.ClienteService;
import com.fgwr.jpcorretora.services.ImovelService;
import com.fgwr.jpcorretora.utils.FileUtils;
import com.fgwr.jpcorretora.utils.StringsUtils;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ContratoController {

	FrontApp frontApp = new FrontApp();
	ApplicationContext context = SpringContext.getAppContext();

	@FXML
	private TableView<Contrato> contratoTable;
	@FXML
	private TableColumn<Contrato, String> numColumn;
	@FXML
	private TableColumn<Contrato, String> periodoColumn;
	@FXML
	private TableColumn<Contrato, String> clienteColumn;
	@FXML
	private TableColumn<Contrato, String> proprietarioColumn;
	@FXML
	private TableColumn<Contrato, String> inicioColumn;
	@FXML
	private TableColumn<Contrato, String> fimColumn;
	@FXML
	private TableColumn<Contrato, String> mensalidadesColumn;

	private List<Contrato> getContratoData() {
		ContratoRepository contRepo = (ContratoRepository) context.getBean("contratoRepository");
		List<Contrato> contratos = contRepo.findByActive(Boolean.TRUE);

		return contratos;
	}

	@FXML
	private void initialize() {

		contratoTable.setItems(FXCollections.observableArrayList(getContratoData()));
		numColumn.setCellValueFactory(cellData -> cellData.getValue().num());
		clienteColumn.setCellValueFactory(cellData -> cellData.getValue().cliente());
		periodoColumn.setCellValueFactory(cellData -> cellData.getValue().vigencia());
		proprietarioColumn.setCellValueFactory(cellData -> cellData.getValue().proprietario());
		inicioColumn.setCellValueFactory(cellData -> cellData.getValue().dataInicio());
		fimColumn.setCellValueFactory(cellData -> cellData.getValue().dataFim());
		mensalidadesColumn.setCellValueFactory(cellData -> cellData.getValue().mensalidade());

	}

	@FXML
	private void handleCancelaContrato() {

		Calendar cal = Calendar.getInstance();
		ContratoRepository contRepo = (ContratoRepository) context.getBean("contratoRepository");
		ImovelRepository imvRepo = (ImovelRepository) context.getBean("imovelRepository");
		ClienteRepository cliRepo = (ClienteRepository) context.getBean("clienteRepository");
		ClienteService cs = (ClienteService) context.getBean("clienteService");
		ImovelService is = (ImovelService) context.getBean("imovelService");
		DuplicataRepository dupRepo = (DuplicataRepository) context.getBean("duplicataRepository");
		Contrato selectedContrato = contratoTable.getSelectionModel().getSelectedItem();

		if (selectedContrato != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(FileUtils.fileToString(new File("css/alerts.css")));
			alert.setTitle("Rescisão de Contrato");
			alert.setHeaderText("Confirmar Rescisão do Contrato Selecionado?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				List<Duplicata> dups = dupRepo.findByContrato(selectedContrato);
				List<Duplicata> dupsToDel = new ArrayList<>();
				Integer pendencias = 0;

				for (Duplicata duplicata : dups) {
					boolean mark = false;

					if (duplicata.getEstado().equals(EstadoPagamento.QUITADO)) {
						mark = false;
					}

					if (duplicata.getDataVencimento().after(cal.getTime())
							&& duplicata.getEstado().equals(EstadoPagamento.PENDENTE)) {
						mark = true;
					}
					if (duplicata.getDataVencimento().before(cal.getTime())
							&& duplicata.getEstado().equals(EstadoPagamento.PENDENTE)) {
						mark = false;
						pendencias++;
					}

					if (mark) {
						dupsToDel.add(duplicata);
					}

				}

				if (dupsToDel.size() > 0) {
					dupRepo.deleteAll(dupsToDel);
				}

				if (pendencias > 0) {
					Alert alert2 = new Alert(AlertType.WARNING);
					DialogPane dialogPane2 = alert2.getDialogPane();
					dialogPane2.getStylesheets().add(FileUtils.fileToString(new File("css/alerts.css")));
					alert2.setTitle("Rescisão de Contrato");
					alert2.setHeaderText(
							"O Contrato foi rescindido, no entando, há mensalidades em atraso do cliente. Regularize a situação.");

				}

				Cliente cliente = cs.find(selectedContrato.getCliente().getId());
				Imovel imv = is.find(selectedContrato.getImovel().getId());
				imv.setContrato(null);
				cliente.setContrato(null);
				cliRepo.save(cliente);
				imvRepo.save(imv);
				selectedContrato.setActive(false);
				contRepo.save(selectedContrato);
				contratoTable.getItems().remove(selectedContrato);

			}
		}

	}

	@FXML
	public void visualizaContrato() throws IOException {
		Contrato selectedContrato = contratoTable.getSelectionModel().getSelectedItem();
		File file = new File(FileUtils.pathContratos(selectedContrato));
		Desktop desktop = Desktop.getDesktop();
		desktop.open(file);
	}
	
	@FXML
	public void editaContrato() {
		Contrato selectedContrato = contratoTable.getSelectionModel().getSelectedItem();
		File file = new File(FileUtils.pathContratos(selectedContrato));

	}

	public void setMainApp(FrontApp frontApp) {
		this.frontApp = frontApp;
	}

}
