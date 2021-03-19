package com.fgwr.jpcorretora.views;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
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
import com.fgwr.jpcorretora.repositories.DuplicataRepository;
import com.fgwr.jpcorretora.repositories.ReciboRepository;
import com.fgwr.jpcorretora.services.PdfGen;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ConfiguraPagamentoController {

	PdfGen pdfGen = new PdfGen();
	
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

	private Double juros;

	private Stage dialogStage;
	private boolean okClicked = false;

	private Duplicata duplicata;

	Calendar cal = Calendar.getInstance();

	@FXML
	private void initialize() {

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

		Date now = new Date();

		cal.setTime(now);
		Double valorPorDia = duplicata.getValor() / cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		DecimalFormatSymbols separador = new DecimalFormatSymbols();
		separador.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("0.00", separador);
		String valorPorDiaString = df.format(valorPorDia);

		dataPagamentoField.setValue(Instant.now().atZone(ZoneId.systemDefault()).toLocalDate());
		Integer dia = dataPagamentoField.getValue().getDayOfMonth();
		Integer mes = dataPagamentoField.getValue().getMonthValue();
		Integer ano = dataPagamentoField.getValue().getYear();

		LocalDate date = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		cal.set(ano, mes - 1, dia);
		LocalDate date2 = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		juros = Double.parseDouble(valorPorDiaString) * Math.toIntExact(ChronoUnit.DAYS.between(date, date2));

		vencimentoField.setText(duplicata.getDataVencimento().toString());

		Double valorCorrigido = duplicata.getValor() + juros;
		valorCorrigidoField.setText(valorCorrigido.toString());

	}

	@FXML
	private void handleOk() {
		ApplicationContext context = SpringContext.getAppContext();
		DuplicataRepository dupRepo = (DuplicataRepository) context.getBean("duplicataRepository");
		ReciboRepository recRepo = (ReciboRepository) context.getBean("reciboRepository");

		Date dataPgto = Date.from(dataPagamentoField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		Recibo rec = new Recibo(null, duplicata.getContrato().getCliente(), duplicata.getValor(),
				duplicata.getParcela(), duplicata.getDataVencimento(), dataPgto);
		duplicata.setDataPagamento(dataPgto);
		duplicata.setRecibo(rec);
		duplicata = dupRepo.save(duplicata);
	//	rec = recRepo.save(rec);
		 rec = duplicata.getRecibo();
		 
		 pdfGen.geraRecibo(rec);
		 
		 okClicked = true;
		 dialogStage.close();
	}

}
