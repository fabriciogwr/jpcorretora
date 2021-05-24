package com.fgwr.jpcorretora.views;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Receita;
import com.fgwr.jpcorretora.domain.ReciboAvulso;
import com.fgwr.jpcorretora.enums.EstadoPagamento;
import com.fgwr.jpcorretora.enums.MeioPagamento;
import com.fgwr.jpcorretora.repositories.ReceitaRepository;
import com.fgwr.jpcorretora.services.ReciboAvulsoPdfGen;
import com.fgwr.jpcorretora.utils.FilesUtils;
import com.fgwr.jpcorretora.utils.StringsUtils;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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
public class ConfiguraPagamentoReceitaController {

	ReciboAvulsoPdfGen reciboPdfGen = new ReciboAvulsoPdfGen();
	Calendar cal = Calendar.getInstance();
	private LocalDate dataPgto;
	private LocalDate dataVcm;

	private Receita receita;
	
	
	private Double juros;
	private Double valorCorrigido;
	private Double valorCorrigidoCalc;
	private String valorCorrigidoStr;
	
	private Stage dialogStage;
	private boolean okClicked = false;

	@FXML
	private CheckBox abaterJurosCheck;
	@FXML
	private CheckBox geraReciboChk;
	@FXML
	private DatePicker dataRecebimentoField;
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

		Callback<ListView<MeioPagamento>, ListCell<MeioPagamento>> meioRecebimentoCellFactory = new Callback<ListView<MeioPagamento>, ListCell<MeioPagamento>>() {

			@Override
			public ListCell<MeioPagamento> call(ListView<MeioPagamento> l) {
				return new ListCell<MeioPagamento>() {

					@Override
					protected void updateItem(MeioPagamento meioRecebimento, boolean empty) {
						super.updateItem(meioRecebimento, empty);
						if (meioRecebimento == null || empty) {
							setGraphic(null);
						} else {
							setText(meioRecebimento.getCod() + " - " + meioRecebimento.getDescricao());
						}
					}
				};
			}

		};
		meioPgtoBox.setButtonCell(meioRecebimentoCellFactory.call(null));
		meioPgtoBox.setCellFactory(meioRecebimentoCellFactory);
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

	public void setReceita(Receita receita) {
		this.receita = receita;
		String valor = "R$ " + StringsUtils.formatarReal(receita.getValor());
		valorVencimentoField.setText(valor);

		Calendar cal2 = Calendar.getInstance();

		Double pc1 = ((receita.getValor()) / 100) * 1;
		Double pc2 = ((receita.getValor()) / 100) * 2;

		cal2.setTime(receita.getDataVencimento());
		Double valorPorDia = pc1 / cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		dataPgto = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		dataRecebimentoField.setValue(dataPgto);

		vencimentoField.setText(receita.getDataVencimentoForm());
		dataVcm = cal2.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		juros = 0.0;
		if (dataPgto.isBefore(dataVcm) || dataPgto.isEqual(dataVcm)) {
			juros = 0.0;
		} else {
			juros = (valorPorDia * Math.toIntExact(ChronoUnit.DAYS.between(dataVcm, dataPgto))) + pc2;
		}

		valorCorrigidoCalc = receita.getValor() + juros;
		valorCorrigido = valorCorrigidoCalc;
		valorCorrigidoStr = "R$ " + StringsUtils.formatarReal(valorCorrigidoCalc);
		valorCorrigidoField.setText(valorCorrigidoStr);

		abaterJurosCheck.selectedProperty().addListener((observable2, oldValue2, newValue2) -> {
			if (abaterJurosCheck.isSelected()) {
				valorCorrigido = receita.getValor();
				valorCorrigidoField.setText(valor);
			} else {
				valorCorrigido = valorCorrigidoCalc;
				valorCorrigidoField.setText(valorCorrigidoStr);
			}
		});

		dataRecebimentoField.valueProperty().addListener((observable, oldValue, newValue) -> {
			dataPgto = dataRecebimentoField.getValue();
			if (dataPgto.isBefore(dataVcm) || dataPgto.isEqual(dataVcm)) {
				juros = 0.0;
			} else {
				juros = (valorPorDia * Math.toIntExact(ChronoUnit.DAYS.between(dataVcm, dataPgto))) + pc2;
			}

			valorCorrigidoCalc = receita.getValor() + juros;
			valorCorrigido = valorCorrigidoCalc;
			valorCorrigidoStr = "R$ " + StringsUtils.formatarReal(valorCorrigidoCalc);
			valorCorrigidoField.setText(valorCorrigidoStr);

		});

		dataRecebimentoField.editorProperty().addListener((observable, oldValue, newValue) -> {
			dataPgto = dataRecebimentoField.getValue();
			if (dataPgto.isBefore(dataVcm) || dataPgto.isEqual(dataVcm)) {
				juros = 0.0;
			} else {
				juros = (valorPorDia * Math.toIntExact(ChronoUnit.DAYS.between(dataVcm, dataPgto))) + pc2;
			}

			valorCorrigidoCalc = receita.getValor() + juros;
			valorCorrigido = valorCorrigidoCalc;
			valorCorrigidoStr = "R$ " + StringsUtils.formatarReal(valorCorrigidoCalc);
			valorCorrigidoField.setText(valorCorrigidoStr);

		});
	}

		

	@FXML
	private void handleOk() throws IOException {
		if (isInputValid()) {
			ApplicationContext context = SpringContext.getAppContext();
			ReceitaRepository desRepo = (ReceitaRepository) context.getBean("receitaRepository");
			Double valorPago;

			if (totalPagoField.getText().length() > 0) {
				valorPago = Double.parseDouble(totalPagoField.getText().replace(",", "."));
			} else {
				valorPago = valorCorrigido;
			}
			
			Date dataPgto = Date.from(dataRecebimentoField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			
			
			receita.setDataRecebimento(dataPgto);
			
			receita.setMeioPagamento(meioPgtoBox.getValue());
			receita.setValorPago(valorPago);
			receita.setEstado(EstadoPagamento.QUITADO);
			
			receita = desRepo.save(receita);
			
			if (geraReciboChk.isSelected()) {
				ReciboAvulso rec = new ReciboAvulso("João Paulo Escritório Imobiliário", "12345678910", "Teste", "32165498710", "Teste", valorPago, receita, dataPgto);
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

		if (dataRecebimentoField.getValue() == null) {
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
