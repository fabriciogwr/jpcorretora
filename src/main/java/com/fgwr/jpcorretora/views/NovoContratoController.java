package com.fgwr.jpcorretora.views;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.domain.Duplicata;
import com.fgwr.jpcorretora.domain.Imovel;
import com.fgwr.jpcorretora.repositories.ClienteRepository;
import com.fgwr.jpcorretora.repositories.ContratoRepository;
import com.fgwr.jpcorretora.repositories.DuplicataRepository;
import com.fgwr.jpcorretora.repositories.ImovelRepository;
import com.fgwr.jpcorretora.services.DuplicataService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NovoContratoController {

	private Stage dialogStage;
	private boolean okClicked = false;
	Double primeiraParcela;
	 
	ObservableList<Imovel> imoveis;
	ObservableList<Cliente> clientes;
	@FXML
	private ComboBox<Cliente> clienteBox;
	@FXML
	private ComboBox<Imovel> imovelBox;
	@FXML
	private TextField tempoLocacaoField;
	@FXML
	private TextField valorField;
	@FXML
	private TextField vencimentosField;
	@FXML
	private TextField primeiraParcelaField;
	@FXML
	private Label primeiraParcelaLabel;
	
	ApplicationContext context = SpringContext.getAppContext();
	
	private List<String> imovelAux = new ArrayList<>();
	private List<String> clienteAux = new ArrayList<>();

	private Contrato contrato;
	
	public List<Imovel> getImovelData() {
		ImovelRepository imRepo = (ImovelRepository)context.getBean("imovelRepository");
    	List<Imovel>imoveis = imRepo.findAll();
			return imoveis;
	}
	
	public List<Cliente> getClienteData() {
		ClienteRepository cliRepo = (ClienteRepository)context.getBean("clienteRepository");
		List<Cliente> clientes = cliRepo.findAll();
		return clientes;
	}
	
	public void initialize () {
		
		
	
	imoveis = FXCollections.observableArrayList(getImovelData());
	
	for (Imovel imovel : imoveis) {
		imovelAux.add(imovel.getProprietario().getNome());
	}
	imovelBox.setItems(FXCollections.observableArrayList(getImovelData()));
	
	Callback<ListView<Imovel>, ListCell<Imovel>> imovelCellFactory = new Callback<ListView<Imovel>, ListCell<Imovel>>() {

	    @Override
	    public ListCell<Imovel> call(ListView<Imovel> l) {
	        return new ListCell<Imovel>() {

	            @Override
	            protected void updateItem(Imovel imovel, boolean empty) {
	                super.updateItem(imovel, empty);
	                if (imovel == null || empty) {
	                    setGraphic(null);
	                } else {
	                    setText(imovel.getProprietario().getNome());
	                }
	            }
	        }; 
	    }
	


	};

	imovelBox.setButtonCell(imovelCellFactory.call(null));
	imovelBox.setCellFactory(imovelCellFactory);
	
	clientes = FXCollections.observableArrayList(getClienteData());
	
	for (Cliente cliente : clientes) {
		clienteAux.add(cliente.getNome());
	}
	clienteBox.setItems(FXCollections.observableArrayList(getClienteData()));
	
	Callback<ListView<Cliente>, ListCell<Cliente>> clienteCellFactory = new Callback<ListView<Cliente>, ListCell<Cliente>>() {

	    @Override
	    public ListCell<Cliente> call(ListView<Cliente> l) {
	        return new ListCell<Cliente>() {

	            @Override
	            protected void updateItem(Cliente cliente, boolean empty) {
	                super.updateItem(cliente, empty);
	                if (cliente == null || empty) {
	                    setGraphic(null);
	                } else {
	                    setText(cliente.getNome());
	                }
	            }
	        }; 
	    }
	


	};

	clienteBox.setButtonCell(clienteCellFactory.call(null));
	clienteBox.setCellFactory(clienteCellFactory);
	
Calendar cal = Calendar.getInstance();
	
	Date now = new Date();
	
	
	vencimentosField.textProperty().addListener((observable, oldValue, newValue) -> {
		cal.setTime(now);
		if (valorField.getText() != "" && vencimentosField.getText() != "" && Integer.parseInt(vencimentosField.getText()) > cal.get(Calendar.DAY_OF_MONTH)) {
		Double valorPorDia = Double.parseDouble(valorField.getText())/cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		DecimalFormatSymbols separador = new DecimalFormatSymbols();
		separador.setDecimalSeparator('.');				
		DecimalFormat df = new DecimalFormat("0.00", separador);
		String valorPorDiaString = df.format(valorPorDia);
		Double diferencaDeVencimento = Double.parseDouble(valorPorDiaString) * (Integer.parseInt(vencimentosField.getText()) - cal.get(Calendar.DAY_OF_MONTH));
		primeiraParcela = Double.parseDouble(valorField.getText()) + diferencaDeVencimento;
	    primeiraParcelaField.setText("R$ " + primeiraParcela.toString());
		} else if (valorField.getText() != "" && vencimentosField.getText() != "" && Integer.parseInt(vencimentosField.getText()) <= cal.get(Calendar.DAY_OF_MONTH)){
			Double valorPorDia = Double.parseDouble(valorField.getText())/cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			DecimalFormatSymbols separador = new DecimalFormatSymbols();
			separador.setDecimalSeparator('.');				
			DecimalFormat df = new DecimalFormat("0.00", separador);
			String valorPorDiaString = df.format(valorPorDia);
			
			LocalDate date = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			cal.add(Calendar.MONTH, 1);
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), Integer.parseInt(vencimentosField.getText()));
			LocalDate date2 = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			Double diferencaDeVencimento = Double.parseDouble(valorPorDiaString) * Math.toIntExact(ChronoUnit.DAYS.between(date, date2));
			System.out.println(Math.toIntExact(ChronoUnit.DAYS.between(date, date2)));
			primeiraParcela = Double.parseDouble(valorField.getText()) + diferencaDeVencimento;
		    primeiraParcelaField.setText("R$ " + primeiraParcela.toString());
			
		} else {
			primeiraParcelaField.setText("");
		}
	});
	
	
	
	
	
	
}
	
	@FXML
	public void corrigirPrimeiraParcela () {
		
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
    
    private boolean isInputValid() {
        String errorMessage = "";

        if (clienteBox.getValue() == null) {
            errorMessage += "Selecione um cliente\n"; 
        }
        if (imovelBox.getValue() == null) {
            errorMessage += "Selecione um Imovel\n"; 
        }
        if (tempoLocacaoField.getText() == "") {
            errorMessage += "Informe um tempo de locação\n"; 
        }

        if (valorField.getText() == "") {
            errorMessage += "Informe o valor da mensalidade\n"; 
        }

        if (vencimentosField.getText().length() >= 3) {
            errorMessage += "Data de vencimentos inválida\n"; 
        }

        

        if (errorMessage.length() == 0) {
            return true;
        } else {
        	Alert alert = new Alert(AlertType.ERROR);
            	      alert.setTitle("Campos Inválidos");
            	      alert.setHeaderText("Por favor, corrija os campos inválidos");
            	      alert.setContentText(errorMessage);
                alert.showAndWait();
                
            return false;
        }
    }
    
    public void setContrato(Contrato contrato) {
    	this.contrato = contrato;
    	
    }
    
    @FXML
    @Transactional
    private void handleOk() {
        if (isInputValid()) {
        	
        	DuplicataService ds = (DuplicataService)context.getBean("duplicataService");
        	ContratoRepository contRepo = (ContratoRepository)context.getBean("contratoRepository");
        	DuplicataRepository dupRepo = (DuplicataRepository)context.getBean("duplicataRepository");
        	ClienteRepository cliRepo = (ClienteRepository)context.getBean("clienteRepository");
        	ImovelRepository imRepo = (ImovelRepository)context.getBean("imovelRepository");
        	Date date = new Date();
        	
        	contrato.setCliente(clienteBox.getValue());
        	contrato.setImovel(imovelBox.getValue());
        	contrato.setData(date);
        	contrato.setId(null);
        	contrato.setQtdParcelas(Integer.parseInt(tempoLocacaoField.getText()));
        	contrato.setValorDeCadaParcela(Double.parseDouble(valorField.getText()));
        	
        	List<Duplicata> dups = ds.preencherDuplicata(contrato, Integer.parseInt(vencimentosField.getText()));
        	
        	for (Duplicata duplicata : dups) {
    			duplicata.setContrato(contrato);
    		}
        	

        	Cliente cliente = clienteBox.getValue();
        	
        	Imovel imovel = imovelBox.getValue();
        	cliente.setContrato(contrato);
        	cliRepo.save(cliente);
        	System.out.println(imovel);
        	imovel.setContrato(contrato);
        	contrato.setImovel(imovel);
        	imRepo.save(imovel);
        	contRepo.save(contrato);
        	contrato.setDuplicatas(dups);
        	contRepo.save(contrato);
        	
        	    	
        	dupRepo.saveAll(dups);
        	
        	
        	
        }
    }
    
        
}