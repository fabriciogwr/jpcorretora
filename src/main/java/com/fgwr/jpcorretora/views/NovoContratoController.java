package com.fgwr.jpcorretora.views;

import java.util.ArrayList;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NovoContratoController {

	private Stage dialogStage;
	private boolean okClicked = false;
	 
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
		imovelAux.add(imovel.getProprietario());
	}
	imovelBox.setItems(FXCollections.observableArrayList(getImovelData()));
	
	clientes = FXCollections.observableArrayList(getClienteData());
	
	for (Cliente cliente : clientes) {
		clienteAux.add(cliente.getNome());
	}
	clienteBox.setItems(FXCollections.observableArrayList(getClienteData()));
	
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
/*
        if (nomeField.getText() == null || nomeField.getText().length() == 0) {
            errorMessage += "Nome inválido\n"; 
        }
        if (emailField.getText() == null || emailField.getText().length() == 0) {
            errorMessage += "Email inválido\n"; 
        }
        if (cpfField.getText() == null || cpfField.getText().length() == 0) {
            errorMessage += "CPF inválido\n"; 
        }

        if (rgField.getText() == null || rgField.getText().length() == 0) {
            errorMessage += "RG inválido\n"; 
        }

        if (estadoCivilBox.getValue() == null) {
            errorMessage += "Selecione um Estado Civil\n"; 
        }

        if (dataNascimentoField.getValue() == null) {
            errorMessage += "Data de Nascimento inválida\n";
        
        }
*/
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
        /*	Cliente cliente = clienteBox.getValue();
        	cliente.setContrato(contrato);
        	Imovel imovel = imovelBox.getValue();
        	imovel.setContrato(contrato);
        	contrato.setDuplicatas(dups);
        	cliRepo.save(cliente);
        	imRepo.save(imovel);
        	contRepo.save(contrato);        	
        	dupRepo.saveAll(dups);
        	
        	*/
        	
        }
    }
    
        
}