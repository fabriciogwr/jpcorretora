package com.fgwr.jpcorretora.views;

import java.io.File;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fgwr.jpcorretora.SpringContext;
import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Referencia;
import com.fgwr.jpcorretora.repositories.ClienteRepository;
import com.fgwr.jpcorretora.repositories.ReferenciaRepository;
import com.fgwr.jpcorretora.utils.FilesUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EditRefController {

	@FXML
	private TextField refNomeField;
	@FXML
	private TextField refFoneField;
	@FXML

	private Stage dialogStage;
	private Cliente cliente;
	private Referencia referencia;
	private boolean okClicked = false;

	ApplicationContext context = SpringContext.getAppContext();
	ClienteRepository cliRepo = (ClienteRepository) context.getBean("clienteRepository");
	ReferenciaRepository refRepo = (ReferenciaRepository) context.getBean("referenciaRepository");

	@FXML
	private void initialize() {

	}

	@FXML
	public void handleOnKeyPressed(KeyEvent e) {
		KeyCode code = e.getCode();		
		
		if (code == KeyCode.ENTER) {
			handleOk();
		}
		if (code == KeyCode.ESCAPE) {
			handleCancel();
		}
	}
    
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setClienteReferencia(Cliente cliente, Referencia referencia) {
		this.cliente = cliente;
		this.referencia = referencia;

		refNomeField.setText(referencia.getNome());
		refFoneField.setText(referencia.getTelefone());

	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() {
		if (isInputValid()) {

			if (!refNomeField.getText().isBlank()) {
				referencia.setNome(refNomeField.getText().trim());
				referencia.setTelefone(refFoneField.getText().trim());
				cliente.getReferencia().add(referencia);
				referencia.setCliente(cliente);
				cliRepo.save(cliente);
				refRepo.save(referencia);
			} else if (refNomeField.getText().isBlank()) {

				refRepo.delete(referencia);

			}
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

		if (refFoneField.getText() == null || refFoneField.getText().length() == 0
				|| refFoneField.getText().matches("[a-zA-Z_]+")) {
			errorMessage += "Telefone inválido\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initStyle(StageStyle.UNDECORATED);
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