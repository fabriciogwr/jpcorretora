package com.fgwr.jpcorretora.views;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
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
import com.fgwr.jpcorretora.services.ReciboPdfGen;
import com.fgwr.jpcorretora.utils.StringsUtils;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ConfiguraPagamentoController {

	ReciboPdfGen reciboPdfGen = new ReciboPdfGen();
	
	LocalDate dataPgto;
	LocalDate dataVcm;
	
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
	
	private Double valorCorrigido;
	private Double valorCorrigidoCalc;
	private String valorCorrigidoStr;
	
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
		
		String valor = "R$ " + StringsUtils.formatarReal(duplicata.getValor());
		valorVencimentoField.setText(valor);
		cal.setTime(duplicata.getDataVencimento());
		Double valorPorDia = duplicata.getValor() / cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		dataPagamentoField.setValue(Instant.now().atZone(ZoneId.systemDefault()).toLocalDate());
		
		dataPgto = dataPagamentoField.getValue();
		vencimentoField.setText(duplicata.getDataVencimentoForm());
		dataVcm = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		juros = valorPorDia * Math.toIntExact(ChronoUnit.DAYS.between(dataVcm, dataPgto));
		valorCorrigidoCalc = duplicata.getValor() + juros;
		valorCorrigido = valorCorrigidoCalc;
		valorCorrigidoStr = "R$ " + StringsUtils.formatarReal(valorCorrigidoCalc);
		valorCorrigidoField.setText(valorCorrigidoStr);
		
		dataPgto = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate();
		
		juros = 0.0;
		if (dataPgto.isBefore(dataVcm)) {
			juros = 0.0 ;
		} else {
			juros = valorPorDia * Math.toIntExact(ChronoUnit.DAYS.between(dataVcm, dataPgto));
		}
		
		valorCorrigidoCalc = (juros == 0.0 ) ? duplicata.getValor() : duplicata.getValor() + juros;
		valorCorrigido = valorCorrigidoCalc;
		valorCorrigidoStr = "R$ " + StringsUtils.formatarReal(valorCorrigidoCalc);
		valorCorrigidoField.setText(valorCorrigidoStr);
		
		dataPagamentoField.valueProperty().addListener((observable, oldValue, newValue) -> {
			dataPgto = dataPagamentoField.getValue();
			dataVcm = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if (dataPgto.isBefore(dataVcm)) {
				juros = 0.0 ;
			} else {
				juros = valorPorDia * Math.toIntExact(ChronoUnit.DAYS.between(dataVcm, dataPgto));
			}
			
			valorCorrigidoCalc = (juros == 0.0 ) ? duplicata.getValor() : duplicata.getValor() + juros;
			valorCorrigido = valorCorrigidoCalc;
			valorCorrigidoStr = "R$ " + StringsUtils.formatarReal(valorCorrigidoCalc);
			valorCorrigidoField.setText(valorCorrigidoStr);
		});

		
		
		abaterJurosCheck.armedProperty().addListener((observable, oldValue, newValue) -> {
			if (abaterJurosCheck.isSelected()) {
				valorCorrigido = duplicata.getValor();
			valorCorrigidoField.setText(valor);
			} else {
				valorCorrigido = valorCorrigidoCalc;
				valorCorrigidoField.setText(valorCorrigidoStr);
			}
			
		});

	}
	

	@FXML
	private void handleOk() throws FileNotFoundException {
		ApplicationContext context = SpringContext.getAppContext();
		DuplicataRepository dupRepo = (DuplicataRepository) context.getBean("duplicataRepository");
		ReciboRepository recRepo = (ReciboRepository) context.getBean("reciboRepository");
		
		Double valorPago;
		
		if (totalPagoField.getText().length() > 0) {
			valorPago = Double.parseDouble(totalPagoField.getText().replace(",","."));

		} else {
			valorPago = valorCorrigido;
			
		}
		
		
	
		Date dataPgto = Date.from(dataPagamentoField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		Recibo rec = new Recibo(null, duplicata.getContrato().getCliente(), valorPago,
				duplicata.getParcela(), duplicata.getDataVencimento(), dataPgto);
		duplicata.setDataPagamento(dataPgto);
		duplicata.setRecibo(rec);
		rec.setDuplicata(duplicata);
		duplicata = dupRepo.save(duplicata);
	//	rec = recRepo.save(rec);
		 rec = duplicata.getRecibo();
		 
		 reciboPdfGen.geraRecibo(rec);
		 
		 okClicked = true;
		 dialogStage.close();
	//	 return rec;
		 
		 
	}

}
