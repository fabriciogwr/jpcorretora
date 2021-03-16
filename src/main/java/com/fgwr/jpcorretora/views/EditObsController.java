package com.fgwr.jpcorretora.views;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Proprietario;
import com.fgwr.jpcorretora.repositories.ClienteRepository;
import com.fgwr.jpcorretora.repositories.ProprietarioRepository;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EditObsController {

	private Stage dialogStage;
    private Cliente cliente;
    private boolean okClicked = false;
    ApplicationContext context = SpringContext.getAppContext();
    
	@FXML
    private TextField obsField;
	private Proprietario proprietario;
	
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
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
		
		obsField.setText(cliente.getObs());		
	}
	
	public void setProprietario(Proprietario proprietario) {
		this.proprietario = proprietario;
		
		obsField.setText(proprietario.getObs());		
	}
	
	@FXML
	private void handleOk() {
		
		if (proprietario == null) {
		ClienteRepository cliRepo = (ClienteRepository)context.getBean("clienteRepository");
				cliente.setObs(obsField.getText());
				cliRepo.save(cliente);	
		}
		
		if (cliente == null) {
			ProprietarioRepository propRepo = (ProprietarioRepository)context.getBean("proprietarioRepository");
			proprietario.setObs(obsField.getText());
					propRepo.save(proprietario);	
			}
			
			okClicked = true;
			dialogStage.close();
		

	}

}
