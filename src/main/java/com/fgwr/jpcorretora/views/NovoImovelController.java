package com.fgwr.jpcorretora.views;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Endereco;
import com.fgwr.jpcorretora.domain.Imovel;
import com.fgwr.jpcorretora.domain.Proprietario;
import com.fgwr.jpcorretora.enums.Banco;
import com.fgwr.jpcorretora.enums.EstadoImovel;
import com.fgwr.jpcorretora.enums.TipoConta;
import com.fgwr.jpcorretora.enums.TipoEndereco;

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
public class NovoImovelController {

	@FXML
    private TextField proprietarioField;	
    @FXML
    private TextField logradouroField;
    @FXML
    private TextField numeroField;
    @FXML
    private TextField complementoField;
    @FXML
    private DatePicker dataAngariacaoField;
    @FXML
    private TextField bairroField;
    @FXML
    private TextField corretorField;
    @FXML
    private TextField cepField;
    @FXML
    private TextField cidadeField;
    @FXML
    private TextField estadoField;
    @FXML
    private ChoiceBox<String> estadoImovelBox;
    @FXML
    private TextField descricaoField;
    @FXML
    private TextField obsField;
    
private List<String> estadoImovelAux = new ArrayList<>();
    


    private Stage dialogStage;
    private Cliente cliente;
    private Endereco endereco;
    private boolean okClicked = false;
    
    private EstadoImovel[] estadoImovel = EstadoImovel.values();
    private TipoEndereco[] tipoEndereco = TipoEndereco.values();
	private Imovel imovel;
	private Proprietario proprietario;
    

    @FXML
    private void initialize() {
    	
    	for (EstadoImovel estadoImovel : estadoImovel) {
			estadoImovelAux.add(estadoImovel.getDescricao());
		}
    	
    	
    	estadoImovelBox.setItems(FXCollections.observableArrayList(estadoImovelAux)); 	
    	    	
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

        if (estadoImovelBox.getValue() == null) {
            errorMessage += "Selecione um Estado Imovel\n"; 
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

	public void setImovel(Imovel imovel, Proprietario proprietario, Endereco endereco) {
		this.imovel = imovel;
		this.proprietario = proprietario;
		this.endereco = endereco;

        proprietarioField.setText(proprietario.getNome());
        logradouroField.setText("");
        numeroField.setText("");
        complementoField.setText("");
        dataAngariacaoField.setValue(Instant.now().atZone(ZoneId.systemDefault()).toLocalDate());
        bairroField.setText("");
        cepField.setText("");
        
        estadoImovelBox.setValue(null);
        
        descricaoField.setText("");
        
        estadoImovelBox.setValue(null);
        
		
	}
	
	 @FXML
	    private void handleOk() {
	        if (isInputValid()) {
	        	endereco.setLogradouro(logradouroField.getText());
	        	endereco.setBairro(bairroField.getText());
	        	endereco.setCep(cepField.getText());
	        	endereco.setNumero(numeroField.getText());
	        	endereco.setComplemento(complementoField.getText());
	        	endereco.setTipo(TipoEndereco.IMOVELLOCACAO);
	        	endereco.setCidade(cidadeField.getText());
	        	endereco.setEstado(estadoField.getText());
	        	
	            imovel.setDataAngariacao(Date.from(dataAngariacaoField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
	            imovel.setCorretor(corretorField.getText());
	            imovel.setDescricao(descricaoField.getText());
	            imovel.setObs(obsField.getText());
	            
	            
	            
	            
	            okClicked = true;
	            dialogStage.close();
	        }
	    }
}
