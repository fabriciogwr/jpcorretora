package com.fgwr.jpcorretora.views;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Duplicata;
import com.fgwr.jpcorretora.domain.Recibo;
import com.fgwr.jpcorretora.enums.EstadoPagamento;
import com.fgwr.jpcorretora.enums.MeioPagamento;
import com.fgwr.jpcorretora.repositories.DuplicataRepository;
import com.fgwr.jpcorretora.services.ReciboPdfGen;
import com.fgwr.jpcorretora.utils.FilesUtils;
import com.fgwr.jpcorretora.utils.StringsUtils;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
public class ConfiguraPagamentoController {

	ReciboPdfGen reciboPdfGen = new ReciboPdfGen();
	Calendar cal = Calendar.getInstance();
	private LocalDate dataPgto;
	private LocalDate dataVcm;

	private Duplicata duplicata;
	private Double juros;
	private Double valorCorrigido;
	private Double valorCorrigidoCalc;
	private String valorCorrigidoStr;

	private Stage dialogStage;
	private boolean okClicked = false;

	@FXML
	private CheckBox abaterJurosCheck;
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

	public void setDuplicata(Duplicata duplicata) {
		this.duplicata = duplicata;
		String valor = "R$ " + StringsUtils.formatarReal(duplicata.getValor());
		valorVencimentoField.setText(valor);

		Calendar cal2 = Calendar.getInstance();

		Double pc1 = ((duplicata.getValor()) / 100) * 1;
		Double pc2 = ((duplicata.getValor()) / 100) * 2;

		cal2.setTime(duplicata.getDataVencimento());
		Double valorPorDia = pc1 / cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		dataPgto = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		dataPagamentoField.setValue(dataPgto);

		vencimentoField.setText(duplicata.getDataVencimentoForm());
		dataVcm = cal2.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		juros = 0.0;
		if (dataPgto.isBefore(dataVcm) || dataPgto.isEqual(dataVcm)) {
			juros = 0.0;
		} else {
			juros = (valorPorDia * Math.toIntExact(ChronoUnit.DAYS.between(dataVcm, dataPgto))) + pc2;
		}

		valorCorrigidoCalc = duplicata.getValor() + juros;
		valorCorrigido = valorCorrigidoCalc;
		valorCorrigidoStr = "R$ " + StringsUtils.formatarReal(valorCorrigidoCalc);
		valorCorrigidoField.setText(valorCorrigidoStr);

		abaterJurosCheck.selectedProperty().addListener((observable2, oldValue2, newValue2) -> {
			if (abaterJurosCheck.isSelected()) {
				valorCorrigido = duplicata.getValor();
				valorCorrigidoField.setText(valor);
			} else {
				valorCorrigido = valorCorrigidoCalc;
				valorCorrigidoField.setText(valorCorrigidoStr);
			}
		});

		dataPagamentoField.valueProperty().addListener((observable, oldValue, newValue) -> {
			dataPgto = dataPagamentoField.getValue();
			if (dataPgto.isBefore(dataVcm) || dataPgto.isEqual(dataVcm)) {
				juros = 0.0;
			} else {
				juros = (valorPorDia * Math.toIntExact(ChronoUnit.DAYS.between(dataVcm, dataPgto))) + pc2;
			}

			valorCorrigidoCalc = duplicata.getValor() + juros;
			valorCorrigido = valorCorrigidoCalc;
			valorCorrigidoStr = "R$ " + StringsUtils.formatarReal(valorCorrigidoCalc);
			valorCorrigidoField.setText(valorCorrigidoStr);

		});

		dataPagamentoField.editorProperty().addListener((observable, oldValue, newValue) -> {
			dataPgto = dataPagamentoField.getValue();
			if (dataPgto.isBefore(dataVcm) || dataPgto.isEqual(dataVcm)) {
				juros = 0.0;
			} else {
				juros = (valorPorDia * Math.toIntExact(ChronoUnit.DAYS.between(dataVcm, dataPgto))) + pc2;
			}

			valorCorrigidoCalc = duplicata.getValor() + juros;
			valorCorrigido = valorCorrigidoCalc;
			valorCorrigidoStr = "R$ " + StringsUtils.formatarReal(valorCorrigidoCalc);
			valorCorrigidoField.setText(valorCorrigidoStr);

		});

	}

	@FXML
	private void handleOk() throws FileNotFoundException {
		if (isInputValid()) {
			ApplicationContext context = SpringContext.getAppContext();
			DuplicataRepository dupRepo = (DuplicataRepository) context.getBean("duplicataRepository");
			Double valorPago;

			if (totalPagoField.getText().length() > 0) {
				valorPago = Double.parseDouble(totalPagoField.getText().replace(",", "."));
			} else {
				valorPago = valorCorrigido;
			}

			Date dataPgto = Date.from(dataPagamentoField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			Recibo rec = new Recibo(null, duplicata.getContrato().getCliente(), valorPago, duplicata.getParcela(),
					duplicata.getContrato().getQtdParcelas(), duplicata.getDataVencimento(), dataPgto);
			duplicata.setDataPagamento(dataPgto);
			duplicata.setRecibo(rec);
			duplicata.setMeioPagamento(meioPgtoBox.getValue());
			duplicata.setValorPago(valorPago);
			duplicata.setEstado(EstadoPagamento.QUITADO);
			rec.setDuplicata(duplicata);
			duplicata = dupRepo.save(duplicata);
			reciboPdfGen.geraRecibo(duplicata.getRecibo());

			okClicked = true;
			dialogStage.close();
		}
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
