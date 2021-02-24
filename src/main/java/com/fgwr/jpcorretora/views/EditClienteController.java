package com.fgwr.jpcorretora.views;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.enums.EstadoCivil;
import com.fgwr.jpcorretora.repositories.ClienteRepository;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
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
    private ChoiceBox<EstadoCivil> estadoCivilBox;
    @FXML
    private TextField profissaoField;

    private Stage dialogStage;
    private Cliente cliente;
    private boolean okClicked = false;
    
    
	
    @FXML
    private void initialize() {
    	
    	estadoCivilBox.setItems(FXCollections.observableArrayList(EstadoCivil.values()));
    	
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
        estadoCivilBox.setValue(cliente.getEstadoCivil());
        profissaoField.setText(cliente.getProfissao());
       
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
            cliente.setEstadoCivil(estadoCivilBox.getValue());
            
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