package com.fgwr.jpcorretora.views;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Despesa;
import com.fgwr.jpcorretora.domain.Recibo;
import com.fgwr.jpcorretora.domain.ReciboAvulso;
import com.fgwr.jpcorretora.enums.EstadoPagamento;
import com.fgwr.jpcorretora.enums.MeioPagamento;
import com.fgwr.jpcorretora.repositories.DespesaRepository;
import com.fgwr.jpcorretora.services.ReciboAvulsoPdfGen;
import com.fgwr.jpcorretora.utils.FilesUtils;
import com.fgwr.jpcorretora.utils.StringsUtils;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ConfiguraPagamentoDespesaController {

	ReciboAvulsoPdfGen reciboPdfGen = new ReciboAvulsoPdfGen();
	Calendar cal = Calendar.getInstance();
	private LocalDate dataPgto;
	private LocalDate dataVcm;

	private Despesa despesa;
	
	private Stage dialogStage;
	private boolean okClicked = false;

	@FXML
	private CheckBox abaterJurosCheck;
	@FXML
	private CheckBox geraReciboChk;
	@FXML
	private DatePicker dataPagamentoField;
	@FXML
	private TextField vencimentoField;
	@FXML
	private TextField valorVencimentoField;
	@FXML
	private TextField valorCorrigidoField;
	@FXML
	private TextField totalPagoField;
	@FXML
	private ComboBox<MeioPagamento> meioPgtoBox;

	@FXML
	private void initialize() {

		abaterJurosCheck.setDisable(true);
		Callback<ListView<MeioPagamento>, ListCell<MeioPagamento>> meioPagamentoCellFactory = new Callback<ListView<MeioPagamento>, ListCell<MeioPagamento>>() {

			@Override
			public ListCell<MeioPagamento> call(ListView<MeioPagamento> l) {
				return new ListCell<MeioPagamento>() {

					@Override
					protected void updateItem(MeioPagamento meioPagamento, boolean empty) {
						super.updateItem(meioPagamento, empty);
						if (meioPagamento == null || empty) {
							setGraphic(null);
						} else {
							setText(meioPagamento.getCod() + " - " + meioPagamento.getDescricao());
						}
					}
				};
			}

		};
		meioPgtoBox.setButtonCell(meioPagamentoCellFactory.call(null));
		meioPgtoBox.setCellFactory(meioPagamentoCellFactory);
		meioPgtoBox.setItems(FXCollections.observableArrayList(MeioPagamento.values()));

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	public void setDespesa(Despesa despesa) {
		this.despesa = despesa;
		String valor = "R$ " + StringsUtils.formatarReal(despesa.getValor());
		valorVencimentoField.setText(valor);

		dataPgto = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		dataPagamentoField.setValue(dataPgto);

		vencimentoField.setText(despesa.getDataVencimentoForm());
		
		totalPagoField.setText(valor);
	}

		

	@FXML
	private void handleOk() throws IOException {
		if (isInputValid()) {
			ApplicationContext context = SpringContext.getAppContext();
			DespesaRepository desRepo = (DespesaRepository) context.getBean("despesaRepository");
			Double valorPago = Double.parseDouble(totalPagoField.getText().replace(",", ".").replace("R", " ").replace("$", " ").strip());

			Date dataPgto = Date.from(dataPagamentoField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			
			
			despesa.setDataPagamento(dataPgto);
			
			despesa.setMeioPagamento(meioPgtoBox.getValue());
			despesa.setValorPago(valorPago);
			despesa.setEstado(EstadoPagamento.QUITADO);
			
			despesa = desRepo.save(despesa);
			
			if (geraReciboChk.isSelected()) {
				ReciboAvulso rec = new ReciboAvulso("João Paulo Escritório Imobiliário", "12345678910", "Teste", "32165498710", "Teste", valorPago, despesa, dataPgto);
				reciboPdfGen.geraRecibo(rec);
				
			Alert alert2 = new Alert(AlertType.CONFIRMATION);
			alert2.initStyle(StageStyle.UNIFIED);
			DialogPane dialogPane2 = alert2.getDialogPane();
			dialogPane2.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
			alert2.setTitle("Recibo");
			alert2.setHeaderText("Visualizar o recibo do pagamento?");
			Optional<ButtonType> result2 = alert2.showAndWait();
			if (result2.get() == ButtonType.OK) {
				visualizaRecibo(rec);

			} else {
				alert2.close();
			}
			}

			okClicked = true;
			dialogStage.close();
		}
	}

	public void visualizaRecibo(ReciboAvulso rec) throws IOException {
		File file = new File(FilesUtils.pathRecibosAvulso(rec));
		Desktop desktop = Desktop.getDesktop();
		desktop.open(file);
	}
	
	private boolean isInputValid() {
		String errorMessage = "";

		if (dataPagamentoField.getValue() == null) {
			errorMessage += "Selecione uma data de pagamento\n";
		}

		if (meioPgtoBox.getValue() == null) {
			errorMessage += "Selecione um meio de pagamento\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initStyle(StageStyle.UNIFIED);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(FilesUtils.fileToString(new File("css/alerts.css")));
			alert.setTitle("Campos Inválidos");
			alert.setHeaderText("Por favor, corrija os campos inválidos");
			alert.setContentText(errorMessage);
			alert.showAndWait();

			return false;
		}
	}
}
