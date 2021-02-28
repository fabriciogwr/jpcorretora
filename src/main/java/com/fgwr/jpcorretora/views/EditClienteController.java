package com.fgwr.jpcorretora.views;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.enums.Banco;
import com.fgwr.jpcorretora.enums.EstadoCivil;
import com.fgwr.jpcorretora.enums.TipoConta;
import com.fgwr.jpcorretora.repositories.ClienteRepository;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EditClienteController {
	
    @FXML
    private TextField nomeField;
    @FXML
    private TextField emailField;
    @FXML
    private DatePicker dataNascimentoField;
    @FXML
    private TextField cpfField;
    @FXML
    private TextField rgField;
    @FXML
    private ChoiceBox<String> estadoCivilBox;
    @FXML
    private TextField profissaoField;
    @FXML
    private TextField agenciaField;
    @FXML
    private TextField contaField;
    @FXML
    private TextField titularField;
    @FXML
    private ComboBox<String> bancoBox;
    @FXML
    private ComboBox<String> tipoContaBox;

    private List<String> estadoCivilAux = new ArrayList<>();
    
    private List<String> bancoAux = new ArrayList<>();
    
    private List<String> tipoContaAux = new ArrayList<>();

    private Stage dialogStage;
    private Cliente cliente;
    private boolean okClicked = false;
    
    private EstadoCivil[] estadoCivil = EstadoCivil.values();
    private Banco[] banco = Banco.values();
    private TipoConta[] tipoConta = TipoConta.values();
    
    
	
    @FXML
    private void initialize() {
    	
    	for (EstadoCivil estadoCivil : estadoCivil) {
			estadoCivilAux.add(estadoCivil.getDescricao());
		}
    	
    	for (Banco banco : banco) {
    		bancoAux.add(banco.getFullCod() + " - " + banco.getDescricao());	
    	}
    	
    	for (TipoConta tipoConta : tipoConta) {
    		tipoContaAux.add(tipoConta.getDesc());
    		
    	}
    	
    	estadoCivilBox.setItems(FXCollections.observableArrayList(estadoCivilAux));
    	bancoBox.setItems(FXCollections.observableArrayList(bancoAux));
    	tipoContaBox.setItems(FXCollections.observableArrayList(tipoContaAux));
    	
    	
    	    	
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;

        nomeField.setText(cliente.getNome());
        emailField.setText(cliente.getEmail());
        dataNascimentoField.setValue(Instant.ofEpochMilli(cliente.getDataNascimento().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        cpfField.setText(cliente.getCpfOuCnpj());
        rgField.setText(cliente.getRg());
        estadoCivilBox.setValue(cliente.getEstadoCivil().getDescricao());
        profissaoField.setText(cliente.getProfissao());
        agenciaField.setText(cliente.getDadosBancarios().getAgencia());
        titularField.setText(cliente.getDadosBancarios().getTitular());
        contaField.setText(cliente.getDadosBancarios().getConta());
		
        bancoBox.setValue(cliente.getDadosBancarios().getBanco().getFullCod() + " - " + cliente.getDadosBancarios().getBanco().getDescricao());
        tipoContaBox.setValue(cliente.getDadosBancarios().getTipo().getDesc());
       
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    
    @FXML
    private void handleOk() {
        if (isInputValid()) {
        	ApplicationContext context = SpringContext.getAppContext();
        	ClienteRepository repo = (ClienteRepository)context.getBean("clienteRepository");
        	
            cliente.setNome(nomeField.getText());
            cliente.setEmail(emailField.getText());
            cliente.setDataNascimento(Date.from(dataNascimentoField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            cliente.setCpfOuCnpj(cpfField.getText());
            cliente.setRg(rgField.getText());
            cliente.setEstadoCivil(EstadoCivil.valueOfDescricao(estadoCivilBox.getValue()));
            cliente.getDadosBancarios().setAgencia(agenciaField.getText());
            cliente.getDadosBancarios().setConta(contaField.getText());
            cliente.getDadosBancarios().setTitular(titularField.getText());
            cliente.getDadosBancarios().setBanco(Banco.valueOfDescricao(bancoBox.getValue().substring(6)));
            cliente.getDadosBancarios().setTipo(TipoConta.valueOfDescricao(tipoContaBox.getValue()));
            repo.save(cliente);
            okClicked = true;
            dialogStage.close();
        }
    }

    
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

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
}